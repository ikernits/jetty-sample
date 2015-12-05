package org.ikernits.sample.jetty;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JettyShutdownListener implements InitializingBean, DisposableBean {
    private static final Logger log = Logger.getLogger(JettyShutdownListener.class);

    protected int shutdownPort;
    protected String shutdownCommand;
    protected Server server;

    private volatile DatagramSocket datagramSocket;
    private Thread shutdownThread;

    @Required
    public void setServer(Server server) {
        this.server = server;
    }

    @Required
    public void setShutdownPort(int shutdownPort) {
        this.shutdownPort = shutdownPort;
    }

    @Required
    public void setShutdownCommand(String shutdownCommand) {
        this.shutdownCommand = shutdownCommand;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Listening for shutdown command on {localhost:" + shutdownPort + "}");

        datagramSocket = new DatagramSocket(shutdownPort, InetAddress.getLocalHost());
        datagramSocket.setSoTimeout(0);

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
}
