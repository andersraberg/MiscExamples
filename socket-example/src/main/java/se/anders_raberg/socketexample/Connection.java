package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Connection {
    private final byte[] buf = new byte[16];
    private final DatagramSocket _socket;

    public Connection(DatagramSocket socket) {
        _socket = socket;
    }

    public void send(DatagramPacket p) throws IOException {
        _socket.send(p);
    }

    public DatagramPacket receive() throws IOException {
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        _socket.receive(p);
        return p;
    }
}
