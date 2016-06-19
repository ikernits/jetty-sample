package org.ikernits.sample.exec;

import com.google.common.collect.ImmutableList;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.ikernits.sample.log.Log4jConfigurer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.ikernits.sample.exec.ProcessExecutionService.ExecutionConfig;
import static org.ikernits.sample.exec.ProcessExecutionService.ExecutionResult;
import static org.ikernits.sample.exec.ProcessExecutionService.IoResult;

public class TestProcessExecutionService {
    static {
        Log4jConfigurer.configureWithDefaults();
    }

    private ProcessExecutionServiceImpl executor;

    private static final String testExecutable = "./src/test/resources/exec-test.sh";
    private static final String tempPath = "./local/test";
    private String testLockPath;

    private void assertResultBase(ExecutionResult result, Integer exitCode, Class<?> errorClass) {
        Assert.assertEquals(exitCode, result.getExitCode());
        if (errorClass == null) {
            Assert.assertNull(result.getError());
        } else {
            Assert.assertEquals(errorClass, result.getError().getClass());
        }
    }

    private void assertIo(IoResult io, String data, int bytesRead, int bytesWritten, Class<?> errorClass) {
        if (data != null) {
            Assert.assertEquals(data, io.getDataAsString());
        }
        if (bytesRead != -1) {
            Assert.assertEquals(bytesRead, io.getTotalBytesRead());
        }
        if (bytesWritten != -1) {
            Assert.assertEquals(bytesWritten, io.getTotalBytesWritten());
        }
        if (errorClass == null) {
            Assert.assertNull(io.getError());
        } else {
            Assert.assertEquals(errorClass, io.getError().getClass());
        }
    }

    private void assertResult(ExecutionResult result, Integer exitCode, Class<?> errorClass,
                              int inR, int inW, Class<?> inC,
                              String outD, int outR, int outW, Class<?> outC,
                              String errD, int errR, int errW, Class<?> errC) {
        assertResultBase(result, exitCode, errorClass);
        assertIo(result.getStdinResult(), "", inR, inW, inC);
        assertIo(result.getStdoutResult(), outD, outR, outW, outC);
        assertIo(result.getStderrResult(), errD, errR, errW, errC);
    }

    @BeforeMethod
    public void init() throws Exception {
        testLockPath = tempPath + "/test.lock." + RandomStringUtils.randomAlphanumeric(8);
        executor = new ProcessExecutionServiceImpl();
        executor.afterPropertiesSet();
    }

    @AfterMethod
    public void destroy() throws Exception {
        executor.destroy();
        FileUtils.deleteQuietly(new File(testLockPath));
    }

    @Test
    public void testNormalExecution() {
        ExecutionConfig config = ExecutionConfig.builder("sleep")
            .addParameters("0")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionAsync() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutionConfig config = ExecutionConfig.builder("sleep")
            .addParameters("0")
            .build();

        ExecutionResult result = executor.executeAsync(config).get(1, TimeUnit.SECONDS);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionStdout() {
        ExecutionConfig config = ExecutionConfig.builder("echo")
            .addParameters("123")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "123\n", 4, 4, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionAddParameters() {
        ExecutionConfig config = ExecutionConfig.builder("echo")
            .addParameters("123")
            .addParameters(ImmutableList.of("456", "789"))
            .addParameters("123", "456")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "123 456 789 123 456\n", 20, 20, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionStderr() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "echo 123 >&2")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "123\n", 4, 4, null);
    }

    @Test
    public void testNormalExecutionStdin() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "cat")
            .setStdin(new ByteArrayInputStream("123".getBytes()))
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            3, 3, null,
            "123", 3, 3, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionStdinToStderr() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "cat >&2")
            .setStdin(new ByteArrayInputStream("123".getBytes()))
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            3, 3, null,
            "", 0, 0, null,
            "123", 3, 3, null);
    }

    @Test
    public void testNormalExecutionStdoutLimit() {
        ExecutionConfig config = ExecutionConfig.builder("echo")
            .addParameters("123")
            .setStdoutLimit(2)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "12", 4, 2, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionStderrLimit() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "echo 123 >&2")
            .setStderrLimit(2)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "12", 4, 2, null);
    }

    @Test
    public void testNormalExecutionStdinLimit() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "cat")
            .setStdin(new ByteArrayInputStream("123".getBytes()))
            .setStdinLimit(2)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            3, 2, null,
            "12", 2, 2, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionStdoutLarge() {
        ExecutionConfig config = ExecutionConfig.builder("dd")
            .addParameters("if=/dev/zero", "bs=1024", "count=2048")
            .setStdoutLimit(1024 * 1024)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            new String(new byte[1024*1024]), 2048*1024, 1024*1024, null,
            null, -1, -1, null);
    }

    @Test
    public void testNormalDirectory() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "ls -1 / | grep -oe bin | uniq")
            .setDirectoryPath("/")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "bin\n", 4, 4, null,
            "", 0, 0, null);
    }

    @Test
    public void testNormalExecutionCustomStdout() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExecutionConfig config = ExecutionConfig.builder("echo")
            .addParameters("123")
            .setStdout(out)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 4, 4, null,
            "", 0, 0, null);
        Assert.assertEquals("123\n", new String(out.toByteArray()));
    }

    @Test
    public void testNormalExecutionCustomStderr() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "echo 123 >&2")
            .setStderr(out)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "", 4, 4, null);
        Assert.assertEquals("123\n", new String(out.toByteArray()));
    }

    @Test
    public void testErrorExecutionCustomStdout() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        };
        ExecutionConfig config = ExecutionConfig.builder("dd")
            .addParameters("if=/dev/zero", "bs=1024", "count=2048")
            .setStdout(out)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 141, null,
            0, 0, null,
            "", -1, 0, IOException.class,
            "", 0, 0, null);
    }

    @Test
    public void testErrorExecutionCustomStderr() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        };
        ExecutionConfig config = ExecutionConfig.builder("dd")
            .addParameters("if=/dev/zero", "bs=1024", "count=2048", "of=/dev/stderr")
            .setStderr(out)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 141, null,
            0, 0, null,
            "", 0, 0, null,
            "", 65536, 0, IOException.class
        );
    }

    @Test
    public void testErrorExecutableNotFound() {
        ExecutionConfig config = ExecutionConfig.builder("not-found!")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, null, IOException.class,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testErrorDirectoryNotExist() {
        ExecutionConfig config = ExecutionConfig.builder("echo")
            .setDirectoryPath("not-exist")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, null, IOException.class,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testErrorExitCode() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "exit 100")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 100, null,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testErrorTimeout() {
        ExecutionConfig config = ExecutionConfig.builder("sleep")
            .addParameters("5")
            .setTimeout(1, TimeUnit.SECONDS)
            .setKillAllowed(true)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, null, TimeoutException.class,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
    }

    @Test
    public void testErrorTimeoutStdout() {
        ExecutionConfig config = ExecutionConfig.builder("bash")
            .addParameters("-c", "echo 123 && sleep 5")
            .setTimeout(1, TimeUnit.SECONDS)
            .setKillAllowed(true)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, null, TimeoutException.class,
            0, 0, null,
            "123\n", 4, 4, null,
            "", 0, 0, null);
    }

    @Test
    public void testStartEndTime() {
        ExecutionConfig config = ExecutionConfig.builder("sleep")
            .addParameters("1")
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, 0, null,
            0, 0, null,
            "", 0, 0, null,
            "", 0, 0, null);
        Assert.assertTrue(result.getEndTime() - result.getStartTime() > 1000);
    }

    @Test
    public void testErrorInterruptAndNormalExit() throws InterruptedException {
        FileUtils.deleteQuietly(new File(testLockPath));

        ExecutionConfig config = ExecutionConfig.builder(testExecutable)
            .addParameters(testLockPath, "5", "0")
            .setTimeout(2, TimeUnit.SECONDS)
            .setKillAllowed(true)
            .build();

        AtomicReference<ExecutionResult> result = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            result.set(executor.execute(config));
        });
        thread.start();

        Thread.sleep(500);
        Assert.assertTrue(new File(testLockPath).isFile());
        thread.interrupt();
        Thread.sleep(1500);

        Assert.assertFalse(thread.isAlive());
        assertResult(result.get(), null, InterruptedException.class,
            0, 0, null,
            null, -1, -1, null,
            null, -1, -1, null);
        Assert.assertFalse(new File(testLockPath).isFile());
    }

    @Test
    public void testErrorInterruptAndKill() throws InterruptedException {
        FileUtils.deleteQuietly(new File(testLockPath));

        ExecutionConfig config = ExecutionConfig.builder(testExecutable)
            .addParameters(testLockPath, "5", "5")
            .setTimeout(2, TimeUnit.SECONDS)
            .setKillAllowed(true)
            .build();

        AtomicReference<ExecutionResult> result = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            result.set(executor.execute(config));
        });
        thread.start();

        Thread.sleep(500);
        Assert.assertTrue(new File(testLockPath).isFile());
        thread.interrupt();
        Thread.sleep(1500);

        Assert.assertFalse(thread.isAlive());
        assertResult(result.get(), null, InterruptedException.class,
            0, 0, null,
            null, -1, -1, null,
            null, -1, -1, null);
        Assert.assertTrue(new File(testLockPath).isFile());
    }

    @Test
    public void testExecInterruptAndShutdown() throws Exception {
        ExecutionConfig config = ExecutionConfig.builder(testExecutable)
            .addParameters(testLockPath, "5", "1")
            .setTimeout(2, TimeUnit.SECONDS)
            .setKillAllowed(true)
            .build();

        Future<ExecutionResult> result = executor.executeAsync(config);

        Thread.sleep(500);
        Assert.assertTrue(new File(testLockPath).isFile());
        result.cancel(true);
        Thread.sleep(500);
        executor.destroy();
        Thread.sleep(1000);
        Assert.assertFalse(new File(testLockPath).isFile());
    }

    @Test
    public void testErrorTimeoutAndKillNotAllowed() throws Exception {
        ExecutionConfig config = ExecutionConfig.builder(testExecutable)
            .addParameters(testLockPath, "4", "0")
            .setTimeout(1, TimeUnit.SECONDS)
            .setKillAllowed(false)
            .build();

        ExecutionResult result = executor.execute(config);
        assertResult(result, null, TimeoutException.class,
            0, 0, null,
            null, -1, -1, null,
            null, -1, -1, null);
        Assert.assertTrue(new File(testLockPath).isFile());
        Thread.sleep(2500);
        Assert.assertFalse(new File(testLockPath).isFile());
    }
}
