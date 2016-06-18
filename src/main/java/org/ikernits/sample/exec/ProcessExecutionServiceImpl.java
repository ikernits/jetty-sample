package org.ikernits.sample.exec;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class ProcessExecutionServiceImpl implements ProcessExecutionService, InitializingBean, DisposableBean {

    private static final int IO_BUFFER_SIZE = 64 * 1024;
    private static final int IO_WAIT_MILLIS = 1000;
    private static final int KILL_WAIT_MILLIS = 1000;
    private static final Logger log = Logger.getLogger(ProcessExecutionServiceImpl.class);

    private ExecutorService executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder()
                .setDaemon(true)
                .build()
        );
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdownNow();
    }

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
                log.warn("stream IO operation failed", th);
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

    private class ExecutionCallable implements Callable<ExecutionResult> {
        private final ExecutionConfig config;

        public ExecutionCallable(ExecutionConfig config) {
            this.config = config;
        }

        private void waitIoFutureAndCloseStream(String name, Future<?> future, Closeable stream) {
            try {
                future.get(IO_WAIT_MILLIS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.warn("stream IO: " + name + "does not stop normally after process end");
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

            Throwable error = null;
            try {
                if (config.getTimeout() <= 0) {
                    process.waitFor();
                } else {
                    process.waitFor(config.getTimeout(), config.getTimeoutTimeUnit());
                }
            } catch (InterruptedException e) {
                error = e;
            }

            final Integer exitCode;
            if (!process.isAlive()) {
                exitCode = process.exitValue();
            } else {
                if (config.isKillAllowed()) {
                    log.warn("process is destroyed: '" + config.getExecutablePath() + "'");
                    process.destroy();

                    //loop is required because interrupt and happen in several cases
                    //future.cancel, executor shutdown
                    for (int i = 0; i < 3; ++i) {
                        try {
                            process.waitFor(KILL_WAIT_MILLIS, TimeUnit.MILLISECONDS);
                            break;
                        } catch (InterruptedException e) {
                            log.warn("interrupted during process kill, ignored" + i);
                        }
                    }

                    if (!process.isAlive()) {
                        log.warn("process was destroyed normally");
                    } else {
                        log.warn("process is forcibly destroyed: '" + config.getExecutablePath() + "'");
                        process.destroyForcibly();
                    }
                }
                if (error == null) {
                    error = new TimeoutException();
                }
                exitCode = null;
            }

            final long endTime = System.currentTimeMillis();

            waitIoFutureAndCloseStream("stdin", stdinFuture, process.getOutputStream());
            waitIoFutureAndCloseStream("stdout", stdoutFuture, process.getInputStream());
            waitIoFutureAndCloseStream("stderr", stderrFuture, process.getErrorStream());

            return new ExecutionResult(config, startTime, endTime, exitCode,
                error,
                stdinIoCopyRunnable != null ? stdinIoCopyRunnable.getIoResult() : IoResult.empty(),
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
    }

    @Override
    public Future<ExecutionResult> executeAsync(ExecutionConfig config) {
        return executorService.submit(new ExecutionCallable(config));
    }

    @Override
    public ExecutionResult execute(ExecutionConfig config) {
        return new ExecutionCallable(config).call();
    }


}
