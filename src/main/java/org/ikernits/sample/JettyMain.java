package org.ikernits.sample;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by KerneL on 2015-10-03.
 */
public class JettyMain {

    static {
        ConsoleAppender console = new ConsoleAppender(); //create appender
        //configure the appender
        String PATTERN = "%d [%p] %c %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        //add appender to any Logger (here is root)
        Logger.getRootLogger().addAppender(console);
    }

    public static void main(String[] args) throws Exception {

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("WEB-INF/web.xml"));
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("");

        //String warUrl = Thread.currentThread().getContextClassLoader().getResource("WEB-INF/web.xml").toString();
        //webAppContext.setWar(warUrl.substring(0, warUrl.length() - 15));
        //webAppContext.setWar("/");
        //webAppContext.setBaseResource();

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/spring-context-jetty.xml");
        Server server = applicationContext.getBean("jettyServer", Server.class);
//
        //server.setHandler(webAppContext);
//        ServletContextHandler contextHandler = new ServletContextHandler();
//        ServletHolder dispatcherServletHolder = applicationContext.getBean("jettyDispatcherServletHolder", ServletHolder.class);
//        contextHandler.addServlet(dispatcherServletHolder, "/");
//        server.setHandler(contextHandler);
        server.start();
        server.join();


    }
}
