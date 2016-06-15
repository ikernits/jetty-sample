package org.ikernits.sample.exec;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public final class ProcessExecutor {

    private static final int IO_BUFFER_SIZE = 64 * 1024;
    private static final int DEFAULT_STDIO_LIMIT = 64 * 1024;
    private static final int IO_WAIT_MILLIS = 1000;

    private final ExecutorService executorService = new ThreadPoolExecutor(
        0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        new ThreadFactoryBuilder()
            .setDaemon(true)
            .build()
    );

    private static class IoCopyRunnable implements Runnable {
        private final String name;
        private final InputStream in;
        private final OutputStream out;
        private final int limit;
        private final byte[] buffer = new byte[IO_BUFFER_SIZE];

        private final ByteArrayOutputStream data = new ByteArrayOutputStream();
        private volatile int totalBytesRead = 0;
        private volatile int totalBytesWritten = 0;
        private volatile Throwable error = null;

        public IoCopyRunnable(String name, InputStream in, OutputStream out, int limit) {
            this.name = name;
            this.in = in;
            this.limit = limit;
            this.out = out != null ? out : data;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            Thread.currentThread().setName(name);
            try {
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    if (limit != 0 && totalBytesWritten < limit) {
                        int bytesToCopy = Math.min(bytesRead, limit - totalBytesWritten);
                        out.write(buffer, 0, bytesToCopy);
                        totalBytesWritten += bytesToCopy;
                    }
                }
            } catch (Throwable th) {
                error = th;
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
                Thread.currentThread().setName(threadName);
            }
        }

        public IoResult getIoResult() {
            return new IoResult(data.toByteArray(), totalBytesRead, totalBytesWritten, error);
        }
    }

    Future<ExecutionResult> executeAsync(ExecutionConfig config) {
        return executorService.submit(new Callable<ExecutionResult>() {
            private void waitIoFutureAndCloseStream(Future<?> future, Closeable stream) {
                try {
                    future.get(IO_WAIT_MILLIS, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    //ignored
                }

                IOUtils.closeQuietly(stream);

                try {
                    future.get(IO_WAIT_MILLIS, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException e) {
                    //ignored
                } catch (TimeoutException e) {
                    future.cancel(true);
                }
            }

            private ExecutionResult callImpl() {
                List<String> command = new ArrayList<>();
                command.add(config.getExecutablePath());
                command.addAll(config.getParameters());

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                if (config.getDirectoryPath() != null) {
                    processBuilder.directory(new File(config.getDirectoryPath()));
                }

                Process process;
                final long startTime = System.currentTimeMillis();
                try {
                    process = processBuilder.start();
                } catch (IOException e) {
                    return new ExecutionResult(config,
                        startTime, startTime, null, e,
                        IoResult.empty(), IoResult.empty(), IoResult.empty());
                }

                final Future<?> stdinFuture;
                final IoCopyRunnable stdinIoCopyRunnable;
                if (config.getStdin() == null) {
                    stdinIoCopyRunnable = null;
                    stdinFuture = Futures.immediateFuture(null);
                } else {
                    stdinIoCopyRunnable = new IoCopyRunnable(
                        "process-executor-" + config.getName() + "-stdin",
                        config.getStdin(),
                        process.getOutputStream(),
                        config.getStdinLimit()
                    );
                    stdinFuture = executorService.submit(stdinIoCopyRunnable);
                }

                final IoCopyRunnable stdoutIoCopyRunnable = new IoCopyRunnable(
                    "process-executor-" + config.getName() + "-stdout",
                    process.getInputStream(),
                    config.getStdout(),
                    config.getStdoutLimit()
                );
                final Future<?> stdoutFuture = executorService.submit(stdoutIoCopyRunnable);

                final IoCopyRunnable stderrIoCopyRunnable = new IoCopyRunnable(
                    "process-executor-" + config.getName() + "-stderr",
                    process.getErrorStream(),
                    config.getStderr(),
                    config.getStderrLimit()
                );
                final Future<?> stderrFuture = executorService.submit(stderrIoCopyRunnable);

                try {
                    if (config.getTimeout() <= 0) {
                        process.waitFor();
                    } else {
                        process.waitFor(config.getTimeout(), config.getTimeoutTimeUnit());
                    }
                } catch (InterruptedException e) {
                    //ignored
                }

                final boolean timeout;
                final Integer exitCode;
                if (!process.isAlive()) {
                    timeout = false;
                    exitCode = process.exitValue();
                } else {
                    process.destroyForcibly();
                    timeout = true;
                    exitCode = null;
                }

                final long endTime = System.currentTimeMillis();

                waitIoFutureAndCloseStream(stdinFuture, process.getOutputStream());
                waitIoFutureAndCloseStream(stdoutFuture, process.getInputStream());
                waitIoFutureAndCloseStream(stderrFuture, process.getErrorStream());

                return new ExecutionResult(config, startTime, endTime, exitCode,
                    timeout ? new TimeoutException() : null,
                    stdinIoCopyRunnable != null ? stdinIoCopyRunnable.getIoResult() : IoResult.EMPTY_RESULT,
                    stdoutIoCopyRunnable.getIoResult(),
                    stderrIoCopyRunnable.getIoResult()
                );
            }

            @Override
            public ExecutionResult call() {
                final String threadName = Thread.currentThread().getName();
                Thread.currentThread().setName("process-executor-" + config.getName() + "-main");
                try {
                    return callImpl();
                } finally {
                    Thread.currentThread().setName(threadName);
                }

            }
        });
    }

    public ExecutionResult execute(ExecutionConfig config) {
        try {
            return executeAsync(config).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static class ExecutionConfig {
        private String name;
        private String executablePath;
        private String directoryPath = null;
        private List<String> parameters = Collections.emptyList();

        private InputStream stdin = null;
        private OutputStream stdout = null;
        private OutputStream stderr = null;

        private int stdinLimit = Integer.MAX_VALUE;
        private int stderrLimit = DEFAULT_STDIO_LIMIT;
        private int stdoutLimit = DEFAULT_STDIO_LIMIT;

        private long timeout = 0;
        private TimeUnit timeoutTimeUnit = TimeUnit.SECONDS;

        private ExecutionConfig(String name, String executablePath) {
            this.name = name;
            this.executablePath = executablePath;
        }

        public static Builder builder(String executablePath) {
            return new Builder(executablePath);
        }

        public static class Builder {
            private ExecutionConfig delegate;

            public Builder(String executablePath) {
                delegate = new ExecutionConfig(
                    new File(executablePath).getName(),
                    executablePath
                );
            }

            public Builder(String executablePath, String internalName) {
                delegate = new ExecutionConfig(
                    internalName,
                    executablePath
                );
            }

            public Builder setName(String name) {
                delegate.name = name;
                return this;
            }

            public Builder setDirectoryPath(String directoryPath) {
                delegate.directoryPath = directoryPath;
                return this;
            }

            public Builder setParameters(String... parameters) {
                delegate.parameters = new ArrayList<>(Arrays.asList(parameters));
                return this;
            }

            public Builder addParameters(String... parameters) {
                delegate.parameters.addAll(Arrays.asList(parameters));
                return this;
            }

            public Builder setStdin(InputStream stdin) {
                delegate.stdin = stdin;
                return this;
            }

            public Builder setStdout(OutputStream stdout) {
                delegate.stdout = stdout;
                return this;
            }

            public Builder setStderr(OutputStream stderr) {
                delegate.stderr = stderr;
                return this;
            }

            public Builder setStdinLimit(int stdinLimit) {
                delegate.stdinLimit = stdinLimit;
                return this;
            }

            public Builder setStderrLimit(int stderrLimit) {
                delegate.stderrLimit = stderrLimit;
                return this;
            }

            public Builder setStdoutLimit(int stdoutLimit) {
                delegate.stdoutLimit = stdoutLimit;
                return this;
            }

            public Builder setTimeout(long timeout, TimeUnit timeUnit) {
                delegate.timeout = timeout;
                delegate.timeoutTimeUnit = timeUnit;
                return this;
            }

            public ExecutionConfig build() {
                try {
                    return delegate;
                } finally {
                    delegate = new ExecutionConfig(delegate.getName(), delegate.getExecutablePath());
                }
            }
        }

        public String getName() {
            return name;
        }

        public String getExecutablePath() {
            return executablePath;
        }

        public String getDirectoryPath() {
            return directoryPath;
        }

        public String getCommandLine() {
            return getExecutablePath() + " " + getParameters().stream().collect(Collectors.joining(" "));
        }

        public List<String> getParameters() {
            return parameters;
        }

        public InputStream getStdin() {
            return stdin;
        }

        public OutputStream getStdout() {
            return stdout;
        }

        public OutputStream getStderr() {
            return stderr;
        }

        public int getStdinLimit() {
            return stdinLimit;
        }

        public int getStderrLimit() {
            return stderrLimit;
        }

        public int getStdoutLimit() {
            return stdoutLimit;
        }

        public long getTimeout() {
            return timeout;
        }

        public TimeUnit getTimeoutTimeUnit() {
            return timeoutTimeUnit;
        }
    }

    public static class IoResult {
        private final byte[] data;
        private final int totalBytesRead;
        private final int totalBytesWritten;
        private final Throwable error;

        private static final IoResult EMPTY_RESULT = new IoResult(
            ArrayUtils.EMPTY_BYTE_ARRAY, 0, 0, null
        );

        public static IoResult empty() {
            return EMPTY_RESULT;
        }

        public IoResult(byte[] data, int totalBytesRead, int totalBytesWritten, Throwable error) {
            this.data = data;
            this.totalBytesRead = totalBytesRead;
            this.totalBytesWritten = totalBytesWritten;
            this.error = error;
        }

        public byte[] getData() {
            return data;
        }

        public String getDataAsString() {
            return new String(data);
        }

        public int getTotalBytesRead() {
            return totalBytesRead;
        }

        public int getTotalBytesWritten() {
            return totalBytesWritten;
        }

        public Throwable getError() {
            return error;
        }
    }


    public static class ExecutionResult {
        private final ExecutionConfig config;
        private final long startTime;
        private final long endTime;
        private final Integer exitCode;
        private final IoResult stdinResult;
        private final IoResult stdoutResult;
        private final IoResult stderrResult;
        private final Throwable error;

        public ExecutionResult(ExecutionConfig config, long startTime, long endTime,
                               Integer exitCode, Throwable error,
                               IoResult stdinResult, IoResult stdoutResult, IoResult stderrResult) {
            this.config = config;
            this.startTime = startTime;
            this.endTime = endTime;
            this.exitCode = exitCode;
            this.stdinResult = stdinResult;
            this.stdoutResult = stdoutResult;
            this.stderrResult = stderrResult;
            this.error = error;
        }

        public ExecutionConfig getConfig() {
            return config;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public Integer getExitCode() {
            return exitCode;
        }

        public IoResult getStdinResult() {
            return stdinResult;
        }

        public IoResult getStdoutResult() {
            return stdoutResult;
        }

        public IoResult getStderrResult() {
            return stderrResult;
        }

        public Throwable getError() {
            return error;
        }
    }
}
