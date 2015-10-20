package org.ikernits.sample.jetty;

import org.eclipse.jetty.server.Server;
import org.ikernits.sample.log.Log4jConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by KerneL on 2015-10-03.
 */
public class JettyMain {
    public static void main(String[] args) throws Exception {
        Log4jConfigurer.configureIfRequired();
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/spring-context-jetty.xml");
        Server server = applicationContext.getBean("jettyServer", Server.class);
        server.start();
        server.join();
    }
}
