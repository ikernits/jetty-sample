package org.ikernits.sample.util;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

public class ConcurrentUtils {
    public static <T> T performLocked(Lock lock, Callable<T> action) {
        lock.lock();
        try {
            return action.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public static void performLocked(Lock lock, Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }
}
