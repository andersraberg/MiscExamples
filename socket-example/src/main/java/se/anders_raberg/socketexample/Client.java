package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) throws IOException {
        DatagramSocket s = new DatagramSocket();
        Connection connection = new Connection(s);
        final byte[] buf = { 1, 2, 3 };
        DatagramPacket p = new DatagramPacket(buf, buf.length, new InetSocketAddress("localhost", 12345));
        connection.send(p);
    }

}
