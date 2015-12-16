package org.ikernits.sample.jetty;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JettyServerLifecycleService implements InitializingBean, DisposableBean {
    private static final Logger log = Logger.getLogger(JettyServerLifecycleService.class);

    protected int shutdownPort;
    protected String shutdownCommand;
    protected Server server;
    private Thread shutdownThread;

    @Required
    public void setShutdownPort(int shutdownPort) {
        this.shutdownPort = shutdownPort;
    }

    @Required
    public void setShutdownCommand(String shutdownCommand) {
        this.shutdownCommand = shutdownCommand;
    }

    @Required
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Listening for shutdown command on {localhost:" + shutdownPort + "}");

        final DatagramSocket datagramSocket;
        try {
            datagramSocket = new DatagramSocket(shutdownPort, InetAddress.getLocalHost());
            datagramSocket.setSoTimeout(0);
        } catch (BindException ex) {
            log.warn("Shutdown port " + shutdownPort + " is already in use");
            return;
        }

        shutdownThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                try {
                    datagramSocket.receive(packet);
                    byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                    String command = new String(data);
                    if (command.equals(shutdownCommand)) {
                        log.info("Valid shutdown command received on {localhost:" + shutdownPort + "}");
                        server.stop();
                        return;
                    } else {
                        log.warn("Unsupported server command: '" + command + "'");
                    }
                } catch (Exception e) {
                    log.error("Problem occured when listening for shutdown command", e);
                }
            }
        });
        shutdownThread.setDaemon(true);
        shutdownThread.setName("jetty-shutdown-listener");
        shutdownThread.start();
    }

    @Override
    public void destroy() throws Exception {
        shutdownThread.interrupt();
    }

    public void startServer() {
        if (shutdownThread == null) {
            throw new IllegalStateException("Server cannot be started, " +
                    "looks like shutdown port: " + shutdownPort +
                    " is used by another application");
        }


        try {
            log.info("server start requested");
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("failed to start server", e);
        }
    }

    public void sendShutdown() {
        try {
            log.info("server stop requested");
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(new DatagramPacket(
                    shutdownCommand.getBytes(),
                    shutdownCommand.getBytes().length,
                    InetAddress.getLocalHost(),
                    shutdownPort
            ));
            log.info("shutdown command sent to port " + shutdownPort);
        } catch (IOException e) {
            throw new RuntimeException("failed to send shutdown command", e);
        }

    }
}
