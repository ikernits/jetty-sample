package org.ikernits.sample;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ikernits on 06/10/15.
 */
public class AsyncSample {

    interface Config {
        String getMegaConfigValue();
    }

    interface ConfigService {
        Config getConfig();
    }

    interface ConfigServiceConnection {
        ConfigService getConfigService();
    }


    private volatile long configConnectDelayMs = 3000;
    private volatile long configLoadDelayMs = 2000;
    private static void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(String line) {
        System.out.println(new Date().toString() + ": " + line);
    }

    class ConfigImpl implements Config {
        private String value = new Date().toString();
        @Override
        public String getMegaConfigValue() {
            return value;
        }
    }

    class ConfigServiceImpl implements ConfigService {
        @Override
        public Config getConfig() {
            log("loading config...");
            sleepSilently(configLoadDelayMs);
            log("config loaded");
            return new ConfigImpl();
        }
    }

    class ConfigServiceConnectionImpl implements ConfigServiceConnection {
        @Override
        public ConfigService getConfigService() {
            log("connecting to service...");
            sleepSilently(configConnectDelayMs);
            log("service connected");
            return new ConfigServiceImpl();
        }
    }

    //main code

    interface Loader<T> {
        T load();
    }

    class DclLazySingleton<T> {
        private volatile T value;
        private Loader<T> loader;
        private final Object lock = new Object();
        private AtomicInteger version = new AtomicInteger();

        public DclLazySingleton(Loader<T> loader) {
            this.loader = loader;
        }

        T get() {
            if (value != null) {
                return value;
            } else {
                synchronized (lock) {
                    if (value != null) {
                        return value;
                    } else {
                        value = loader.load();
                        return value;
                    }
                }
            }
        }

        void reload() {
            final int currentVersion = version.get();
            synchronized (lock) {
                if (currentVersion == version.get()) {
                    value = loader.load();
                }
                version.incrementAndGet();
            }
        }
    }

    private ScheduledExecutorService executorService;
    private DclLazySingleton<Config> configCache;


    private long configReloadDelayMs = 5000;
    public void init() {
        ConfigServiceConnection configServiceConnection = new ConfigServiceConnectionImpl();

        DclLazySingleton<ConfigService> configServiceCache = new DclLazySingleton<>(
                //binding code
                configServiceConnection::getConfigService
        );

        configCache = new DclLazySingleton<>(
                //config service call
                () -> configServiceCache.get().getConfig()
        );

        executorService = new ScheduledThreadPoolExecutor(4);
        executorService.scheduleWithFixedDelay(configCache::reload, 0, configReloadDelayMs, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void runScenario(long initialDelay) {
        log("started: initial delay = " + initialDelay);
        init();
        sleepSilently(initialDelay);
        for (int i = 0; i < 10; ++i) {
            log("usage#" + i);
            Config config = configCache.get();
            log("config = " + config.getMegaConfigValue());
            sleepSilently(1000);
        }
        shutdown();
        log("done -------------------------------------");
    }

    public static void main(String[] args) {
        new AsyncSample().runScenario(0);
        new AsyncSample().runScenario(5000);
        new AsyncSample().runScenario(10000);
    }
}
