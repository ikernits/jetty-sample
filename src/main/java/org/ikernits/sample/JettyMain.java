package org.ikernits.sample;

import org.eclipse.jetty.server.Server;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by KerneL on 2015-10-03.
 */
public class JettyMain {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/spring-context-main.xml");
        Server server = classPathXmlApplicationContext.getBean("jettyServer", Server.class);
        server.join();
    }
}
