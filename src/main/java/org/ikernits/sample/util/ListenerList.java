package org.ikernits.sample.util;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class ListenerList<T> {
    private final Map<Object, ListenerWrapper<T>> listeners = new ConcurrentHashMap<>();
    private final Executor executor;
    private final Consumer<Throwable> errorHandler;

    private static class ListenerWrapper<T> {
        private final Object key;
        private final Consumer<T> listener;

        public ListenerWrapper(Object key, Consumer<T> listener) {
            this.key = key;
            this.listener = listener;
        }

        public Object getKey() {
            return key;
        }

        public Consumer<T> getListener() {
            return listener;
        }
    }

    public ListenerList(Executor executor, Consumer<Throwable> errorHandler) {
        this.executor = executor == null
            ? MoreExecutors.directExecutor()
            : executor;
        this.errorHandler = errorHandler == null
            ? th -> {}
            : errorHandler;
    }

    public void add(Consumer<T> listener) {
        add(listener, listener);
    }

    public void add(Object key, Consumer<T> listener) {
        listeners.put(key, new ListenerWrapper<>(key, listener));
    }

    public void remove(Object key) {
        listeners.remove(key);
    }

    public void fire(T value) {
        for (ListenerWrapper<T> wrapper : listeners.values()) {
            executor.execute(() -> {
                try {
                    wrapper.getListener().accept(value);
                } catch (Throwable th) {
                    errorHandler.accept(th);
                    remove(wrapper.getKey());
                }
            });
        }
    }
}
