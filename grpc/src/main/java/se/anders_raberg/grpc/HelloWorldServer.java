package se.anders_raberg.grpc;


import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloWorldServer {

    private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

    private int port = 42420;
    private Server server;

    private void start() throws IOException  {
        logger.info("Starting the grpc server");

        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();

        logger.info("Server started. Listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(HelloWorldServer.this::stop));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }


    public static void main(String[] args) throws Exception {
        logger.info("Server startup. Args = " + Arrays.toString(args));
        final HelloWorldServer helloWorldServer = new HelloWorldServer();

        helloWorldServer.start();
        helloWorldServer.blockUntilShutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
