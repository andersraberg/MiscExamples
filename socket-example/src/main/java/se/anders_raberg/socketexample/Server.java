package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException {
        DatagramSocket s = new DatagramSocket(12345);
        Connection connection = new Connection(s);
        DatagramPacket p = connection.receive();
        LOGGER.info(() -> Arrays.toString(Arrays.copyOfRange(p.getData(), 0, p.getLength())));
    }

}
