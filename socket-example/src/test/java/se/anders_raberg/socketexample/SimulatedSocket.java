package se.anders_raberg.socketexample;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class SimulatedSocket extends DatagramSocket {

    private final BlockingQueue<DatagramPacket> _input;
    private final BlockingQueue<DatagramPacket> _output;

    public SimulatedSocket(BlockingQueue<DatagramPacket> input, BlockingQueue<DatagramPacket> output)
            throws SocketException {
        _input = input;
        _output = output;
    }

    @Override
    public void send(DatagramPacket p) {
        _output.add(p);
    }

    @Override
    public void receive(DatagramPacket p) {
        try {
            DatagramPacket packet = _input.take();
            p.setData(packet.getData(), 0, packet.getLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}