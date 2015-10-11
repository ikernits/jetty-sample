package org.ikernits.sample.jetty;

import java.net.URL;

/**
 * Created by ikernits on 04/10/15.
 */
public class JettyWebConfigResolver {
    public String getConfigLocation() {
        URL warUrl = Thread.currentThread().getContextClassLoader().getResource("WEB-INF/web.xml");
        if (warUrl == null) {
            throw new IllegalStateException("Failed to locate web.xml");
        }
        String warPath = warUrl.toString();
        return warPath.substring(0, warPath.length() - 15);
    }
}
