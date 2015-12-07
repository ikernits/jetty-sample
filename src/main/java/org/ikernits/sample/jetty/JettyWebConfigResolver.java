package org.ikernits.sample.jetty;

import org.springframework.beans.factory.annotation.Required;

import java.net.URL;

public class JettyWebConfigResolver {
    private static String configName = "web.xml";

    @Required
    public void setConfigName(String configName) {
        JettyWebConfigResolver.configName = configName;
    }

    public String getConfigLocation() {
        URL warUrl = Thread.currentThread().getContextClassLoader().getResource("WEB-INF");
        if (warUrl == null) {
            throw new IllegalStateException("Failed to locate classpath root");
        }
        return warUrl.toString().substring(0, warUrl.toString().length() - 7);
    }

    public String getDescriptorName() {
        URL configUrl = Thread.currentThread().getContextClassLoader().getResource("WEB-INF/" + configName);
        if (configUrl == null) {
            throw new IllegalStateException("Failed to web application descriptor " + configName);
        }
        return configUrl.toString();
    }
}
