package org.ikernits.sample.jetty;

import java.net.URL;

public class JettyWebConfigResolver {
    public String getConfigLocation() {
        URL warUrl = Thread.currentThread().getContextClassLoader().getResource("WEB-INF");
        if (warUrl == null) {
            throw new IllegalStateException("Failed to locate classpath root");
        }
        return warUrl.toString().substring(0, warUrl.toString().length() - 7);
    }
}
