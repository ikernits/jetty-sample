package org.ikernits.sample.jetty;

import org.eclipse.jetty.server.Server;
import org.ikernits.sample.log.Log4jConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by KerneL on 2015-10-03.
 */
public class JettyMain {
    public static void main(String[] args) throws Exception {
        Log4jConfigurer.configureIfRequired();
        if (args.length == 0) {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/spring-context-jetty.xml");
            Server server = applicationContext.getBean("jettyServer", Server.class);
            server.start();
            server.join();
            applicationContext.destroy();
        } else {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(new DatagramPacket(args[0].getBytes(),
                    args[0].getBytes().length,
                    InetAddress.getLocalHost(), 18080));
        }
    }
}
