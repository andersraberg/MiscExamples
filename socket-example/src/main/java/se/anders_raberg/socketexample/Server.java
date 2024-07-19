package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException {
    	MulticastSocket s = new MulticastSocket(12345);
        Connection connection = new Connection(s);
        for (int i = 0; i < 10; i++) {
			DatagramPacket p = connection.receive();
			LOGGER.info(() -> Arrays.toString(Arrays.copyOfRange(p.getData(), 0, p.getLength())));
		}
    }

}
