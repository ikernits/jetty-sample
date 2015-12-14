package org.ikernits.sample.jetty;

import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class JettyServerService {

    protected int shutdownPort;
    protected String shutdownCommand;
    protected Server server;

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

    public void startServer() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("failed to start server", e);
        }
    }

    public void sendShutdown() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.send(new DatagramPacket(
                    shutdownCommand.getBytes(),
                    shutdownCommand.getBytes().length,
                    InetAddress.getLocalHost(),
                    shutdownPort
            ));
        } catch (IOException e) {
            throw new RuntimeException("failed to send shutdown command", e);
        }

    }
}
