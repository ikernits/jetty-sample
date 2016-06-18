package org.ikernits.sample.exec;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public interface ProcessExecutionService {

    ExecutionResult execute(ExecutionConfig config);
    Future<ExecutionResult> executeAsync(ExecutionConfig config);

    class ExecutionConfig {
        private static final int DEFAULT_STDIO_LIMIT = 1024 * 1024;

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
        private boolean killAllowed = false;

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

            public Builder setKillAllowed(boolean killAllowed) {
                delegate.killAllowed = killAllowed;
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

        public boolean isKillAllowed() {
            return killAllowed;
        }
    }

    class IoResult {
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

        public boolean isEmpty() {
            return data.length == 0;
        }
    }


    class ExecutionResult {
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

        public boolean isSuccess() {
            return exitCode != null && exitCode == 0 && error == null;
        }

        public Throwable getError() {
            return error;
        }

        public String getErrorMessage() {
            StringBuilder message = new StringBuilder();
            message.append("failed to execute: ").append(config.getExecutablePath()).append("\n");
            message.append("arguments: ").append(config.getParameters()).append("\n");
            message.append("exit code: ").append(exitCode).append("\n");
            message.append("timeout: ").append(error instanceof TimeoutException).append("\n");
            if (!stderrResult.isEmpty()) {
                message.append("error output:\n").append(stderrResult.getDataAsString());
            }
            return message.toString();
        }

        public Map<String, String> getDetails() {
            Map<String, String> details = new HashMap<>();
            details.put("path", config.getExecutablePath());
            details.put("arguments", config.getParameters().stream().collect(Collectors.joining(" ")));
            details.put("exitCode", exitCode != null ? "" + exitCode : "none");
            details.put("stdout", stdoutResult.isEmpty() ? "none" : stdoutResult.getDataAsString());
            details.put("stderr", stderrResult.isEmpty() ? "none" : stderrResult.getDataAsString());
            return ImmutableMap.copyOf(details);
        }
    }
}
