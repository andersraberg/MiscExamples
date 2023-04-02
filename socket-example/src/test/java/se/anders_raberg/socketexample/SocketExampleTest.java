package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocketExampleTest {
    private static final InetSocketAddress DUMMY_SOCK_ADDRESS = new InetSocketAddress("localhost", 12345);
    private Connection _connectionA;
    private Connection _connectionB;

    @BeforeEach
    void setUp() throws IOException {
        BlockingQueue<DatagramPacket> aToB = new LinkedBlockingQueue<>();
        BlockingQueue<DatagramPacket> bToA = new LinkedBlockingQueue<>();
        SimulatedSocket socketA = new SimulatedSocket(bToA, aToB);
        SimulatedSocket socketB = new SimulatedSocket(aToB, bToA);
        _connectionA = new Connection(socketA);
        _connectionB = new Connection(socketB);
    }

    @Test
    void test() throws IOException {
        final byte[] buf1 = { 10, 20, 30 };
        DatagramPacket out1 = new DatagramPacket(buf1, buf1.length, DUMMY_SOCK_ADDRESS);
        _connectionA.send(out1);
        DatagramPacket in1 = _connectionB.receive();
        Assertions.assertArrayEquals(out1.getData(), in1.getData());

        final byte[] buf2 = { 11, 22, 33 };
        DatagramPacket out2 = new DatagramPacket(buf2, buf2.length, DUMMY_SOCK_ADDRESS);
        _connectionB.send(out2);
        DatagramPacket in2 = _connectionA.receive();
        Assertions.assertArrayEquals(out2.getData(), in2.getData());
    }
}
