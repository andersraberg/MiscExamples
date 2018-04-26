package se.anders_raberg.grpc;


import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.anders_raberg.grpc.generated.GreeterGrpc;
import se.anders_raberg.grpc.generated.HelloRequest;
import se.anders_raberg.grpc.generated.HelloResponse;

public class HelloWorldClient {
    private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

    private final ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloWorldClient(String hostname, int port) {
        channel = ManagedChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        try {
            HelloRequest request = HelloRequest.newBuilder().setName(name).build();
            HelloResponse response = blockingStub.sayHello(request);
            logger.info("Response: " + response.getMessage());
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "Request to grpc server failed", e);
        }
    }


    public static void main(String[] args) throws Exception {
        HelloWorldClient client = new HelloWorldClient(args[0], 42420);
        String name = args[1];

        try {
            client.greet(name);
        } finally {
            client.shutdown();
        }
    }
}
