package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocketExampleTest {

    private class TestSocket extends DatagramSocket {

        private final BlockingQueue<DatagramPacket> _data = new LinkedBlockingQueue<>();

        public TestSocket() throws SocketException {
        }

        @Override
        public void send(DatagramPacket p) throws IOException {
            _data.add(p);
        }

        @Override
        public void receive(DatagramPacket p) throws IOException {
            try {
                DatagramPacket packet = _data.take();
                p.setData(packet.getData(), 0, packet.getLength());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private Connection _connection;

    @BeforeEach
    void setUp() throws IOException {
        TestSocket testSocket = new TestSocket();
        _connection = new Connection(testSocket);
    }

    @Test
    void test() throws IOException {
        final byte[] buf = { 10, 20, 30 };
        DatagramPacket p = new DatagramPacket(buf, buf.length, new InetSocketAddress("localhost", 12345));

        _connection.send(p);

        DatagramPacket p2 = _connection.receive();

        System.out.println(Arrays.toString(Arrays.copyOfRange(p2.getData(), 0, p2.getLength())));
        Assertions.assertEquals(p.getLength(), p2.getLength());
        Assertions.assertEquals(p.getOffset(), p2.getOffset());
        Assertions.assertArrayEquals(p.getData(), p2.getData());
    }

}
