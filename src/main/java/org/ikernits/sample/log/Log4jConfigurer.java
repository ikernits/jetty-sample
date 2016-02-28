package org.ikernits.sample.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Log4jConfigurer implements InitializingBean, ApplicationContextAware{

    private static final String DEFAULT_PATTERN = "%d [%t] %p %c %m%n";
    private static final String CONFIG_CLASSPATH = "META-INF/log4j.properties";
    private static final Logger log = Logger.getLogger(Log4jConfigurer.class);

    protected String propertyPrefix = "log";
    protected boolean resetSubsystem = false;
    protected String logList = "";
    protected ConfigurableListableBeanFactory beanFactory;

    public void setResetSubsystem(boolean resetSubsystem) {
        this.resetSubsystem = resetSubsystem;
    }

    public void setPropertyPrefix(String propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }

    @Required
    public void setLogList(String logList) {
        this.logList = logList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (resetSubsystem) {
            log.info("log4j config is reset");
            Logger.getRootLogger().removeAllAppenders();
        }
        configureFromContext();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof AbstractApplicationContext) {
            this.beanFactory = ((AbstractApplicationContext) applicationContext).getBeanFactory();
        } else {
            throw new IllegalStateException("unsupported application context type: " + applicationContext.getClass());
        }
    }

    private class PropertySource {
        private PropertySource fallback;
        private String prefix;

        public PropertySource(String prefix, PropertySource fallback) {
            this.prefix = prefix;
            this.fallback = fallback;
        }

        public PropertySource createNested(String prefix) {
            return new PropertySource(this.prefix + "." + prefix, fallback);
        }


        protected String getFullPropertyName(String name) {
            return prefix + "." + name;
        }

        public <T> T getPropertyRequired(String name, Class<T> clazz) {
            T propertyValue = getProperty(name, clazz, null);
            if (propertyValue != null) {
                return propertyValue;
            } else {
                throw new RuntimeException("required property not set: '" + getFullPropertyName(name) + "'");
            }
        }

        private <T> T getProperty(String name, Class<T> clazz) {
            String property;
            try {
                property = beanFactory.resolveEmbeddedValue("${" + name + "}");
            } catch (IllegalArgumentException ex) {
                property = null;
            }
            Object result;
            if (property == null) {
                result = null;
            } else if (String.class == clazz) {
                result = property;
            } else if (Integer.class == clazz) {
                result = Integer.valueOf(property);
            } else if (Boolean.class == clazz) {
                result = Boolean.valueOf(property);
            } else if (clazz.isEnum()) {
                result = Enum.valueOf((Class)clazz, property);
            } else {
                throw new IllegalStateException("unsupported property type: " + clazz.getName());
            }
            //noinspection unchecked
            return (T)result;
        }

        public <T> T getPropertyDirectRequired(String name, Class<T> clazz) {
            T propertyValue = getProperty(getFullPropertyName(name), clazz);
            if (propertyValue != null) {
                return propertyValue;
            } else {
                throw new RuntimeException("required property not set: '" + getFullPropertyName(name) + "'");
            }
        }

        public <T> T getProperty(String name, Class<T> clazz, T defaultValue) {
            T value = getProperty(getFullPropertyName(name), clazz);
            if (value != null) {
                return value;
            }
            if (fallback != null) {
                return fallback.getProperty(name, clazz, defaultValue);
            }
            return defaultValue;
        }
    }

    private abstract class BaseConfig {
        protected final String name;
        protected final PropertySource propertySource;

        public BaseConfig(String name, PropertySource propertySource) {
            this.name = name;
            this.propertySource = propertySource;
        }

        protected Logger setupLogger() {
            String loggerName = propertySource.getProperty("logger", String.class, "");
            if (StringUtils.isBlank(loggerName)) {
                return Logger.getRootLogger();
            } else {
                Logger logger = Logger.getLogger(loggerName);
                boolean additive = propertySource.getProperty("additive", Boolean.class, Boolean.TRUE);
                logger.setAdditivity(additive);
                return logger;
            }
        }

        protected abstract AppenderSkeleton createAppender();

        public final void configure() {
            AppenderSkeleton appender = createAppender();
            appender.setName(name);
            appender.setLayout(new PatternLayout(
                    propertySource.getProperty("pattern", String.class, DEFAULT_PATTERN)
            ));
            appender.setThreshold(
                    Level.toLevel(
                            propertySource.getProperty("level", String.class, "INFO"),
                            Level.INFO
                    )
            );
            appender.activateOptions();
            setupLogger().addAppender(appender);
        }
    }

    private class ConsoleConfig extends BaseConfig {
        public ConsoleConfig(String name, PropertySource propertySource) {
            super(name, propertySource);
        }

        @Override
        protected AppenderSkeleton createAppender() {
            return new ConsoleAppender();
        }
    }

    private class FileConfig extends BaseConfig {
        public FileConfig(String name, PropertySource propertySource) {
            super(name, propertySource);
        }

        protected AppenderSkeleton createAppender() {
            RollingFileAppender appender = new RollingFileAppender() {
                @Override
                public void append(LoggingEvent event) {
                    File file = new File(this.getFile());
                    if (!file.exists()) {
                        this.activateOptions();
                    }
                    super.append(event);
                }
            };
            appender.setFile(
                    propertySource.getPropertyRequired("path", String.class)
            );
            appender.setMaxFileSize(
                    propertySource.getProperty("size", String.class, "1M")
            );
            appender.setMaxBackupIndex(
                propertySource.getProperty("count", Integer.class, 10)
            );
            return appender;
        }
    }

    private void configureFromContext() {
        PropertySource defaultPropertySource = new PropertySource(propertyPrefix + ".default", null);
        PropertySource basePropertySource = new PropertySource(propertyPrefix, defaultPropertySource);

        Arrays.stream(logList.split(","))
                .map(String::trim)
                .map(logName -> {
                    PropertySource logPropertySource = basePropertySource.createNested(logName);
                    String logType = logPropertySource.getPropertyDirectRequired("type", String.class)
                            .toLowerCase();
                    switch (logType) {
                        case "file":
                            return new FileConfig(logName, logPropertySource);
                        case "stdout":
                        case "console":
                            return new ConsoleConfig(logName, logPropertySource);
                        default:
                            throw new RuntimeException("unsupported log configuration type: '" + logType + "'");
                    }
                })
                .forEach(BaseConfig::configure);
    }

    public static void configureWithDefaults() {
        Logger.getRootLogger().removeAllAppenders();
        ConsoleAppender console = new ConsoleAppender();
        console.setLayout(new PatternLayout(DEFAULT_PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
        log.info("log4j uses default configuration");
    }

    public static void configureFromFile(File propertyFile) {
        try (InputStream properties = new FileInputStream(propertyFile)) {
            PropertyConfigurator.configure(properties);
            log.info("log4j uses configuration from '" + propertyFile.getAbsolutePath() + "'");
        } catch (IOException e) {
            LogLog.warn("failed to load log4j configuration from property file", e);
            throw new RuntimeException("log4j configuration failed", e);
        }
    }

    public static void configureFromClasspath(String classpath) {
        try(InputStream properties = ClassLoader.getSystemResourceAsStream(classpath)) {
            PropertyConfigurator.configure(properties);
            log.info("loaded log4j configuration from classpath: '" + classpath + "'");
        } catch (IOException e) {
            LogLog.warn("failed to load log4j configuration from classpath: '" + classpath + "'", e);
            throw new RuntimeException("log4j configuration failed", e);
        }
    }

    public static void configureIfRequired() {
        if (Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            log.info("log4j uses startup configuration");
            return;
        }
        if (ClassLoader.getSystemResource(CONFIG_CLASSPATH) != null) {
            try {
                configureFromClasspath(CONFIG_CLASSPATH);
                return;
            } catch (Exception ex) {
                LogLog.warn("failed to configure log4j with classpath resource '" + CONFIG_CLASSPATH + "'");
            }
        }

        configureWithDefaults();
    }
}
