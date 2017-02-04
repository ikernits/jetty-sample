package org.ikernits.sample.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.function.Predicate;

public class Caller {

    public interface CallableSetting<T> {
        ExceptionSetting<T> of(Callable<T> callable);
        Future<T> call();
    }

    public interface ExceptionSetting<T> {
        void handle(Function<Exception, T> handler);
        <E extends Exception> void handle(Class<E> eClass, Function<E, T> handler);

        void suppress(Predicate<Exception> e);
        void transform(Function<Exception, ? extends RuntimeException> e);
    }

    public interface LockSetting {
        void locked(Lock lock, long timeout, TimeUnit timeUnit);
    }

    public interface RetrySetting {
        void retry(int times);
        void retryWithDelay(int times, long timeout, TimeUnit timeUnit);
    }

    public interface AsyncSetting {

    }
}
