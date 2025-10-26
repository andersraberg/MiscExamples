package se.anders_raberg.socketexample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

public class DatagramChannelSender {
    private static final int INPUT_PORT = 11111;

    public static void main(String[] args) throws IOException {
        List<SocketAddress> recepients = List.of( //
                new InetSocketAddress("127.0.0.1", 12345), //
                new InetSocketAddress("127.0.0.1", 12346));

        try (DatagramChannel outputChannel = DatagramChannel.open();
                DatagramChannel inputChannel = DatagramChannel.open()) {

            inputChannel.bind(new InetSocketAddress(INPUT_PORT));
            inputChannel.configureBlocking(true);

            outputChannel.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(512);

            while (true) {
                inputChannel.receive(buffer);
                buffer.flip();
                try {
                    for (SocketAddress socketAddress : recepients) {
                        outputChannel.send(buffer, socketAddress);
                        buffer.rewind();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.clear();
            }
        }
    }
}
