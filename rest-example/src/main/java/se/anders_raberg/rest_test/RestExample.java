package se.anders_raberg.rest_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class RestExample {
    private static final Logger LOGGER = Logger.getLogger(RestExample.class.getName());
    private static final int PORT = 8090;
    private static final String BASE_URL = "http://localhost:" + PORT + "/api/hello/xxxxx";

    public static void main(String[] args) throws IOException, InterruptedException {
        if ("client".equals(args[0])) {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
            sendGetRequest(client);
            sendPostRequest(client);
        } else {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            server.createContext("/api/hello", new HelloHandler());
            server.setExecutor(null);
            server.start();
            LOGGER.info("Server started on port " + PORT);
        }
    }

    private static void sendGetRequest(HttpClient client) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/1")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info(() -> """

                   GET Response Status: %s
                   GET Response Body:   %s
                """.formatted(response.statusCode(), response.body()));
    }

    private static void sendPostRequest(HttpClient client) throws IOException, InterruptedException {
        String jsonBody = """
                {
                    "title": "foo",
                    "body": "bar",
                    "userId": 1
                }
                """;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).POST(BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json").build();

        LOGGER.info(() -> """

                   %s
                """.formatted(format(request)));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.info(() -> """

                   POST Response Status: %s
                   POST Response Body:   %s
                """.formatted(response.statusCode(), response.body()));
    }

    private static String format(HttpRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP Request:\n");
        sb.append(request.method()).append(" ").append(request.uri()).append("\n");

        sb.append("Headers:\n");
        request.headers().map().forEach((key, values) -> values
                .forEach(value -> sb.append("  ").append(key).append(": ").append(value).append("\n")));

        // Attempt to get the body (only for known types)
        request.bodyPublisher().ifPresentOrElse(publisher -> {
            Optional<Long> contentLength = publisher.contentLength() >= 0 ? Optional.of(publisher.contentLength())
                    : Optional.empty();

            sb.append("BodyPublisher: ").append(publisher.getClass().getSimpleName()).append("\n");
            contentLength.ifPresent(len -> sb.append("  Content-Length: ").append(len).append("\n"));
        }, () -> sb.append("No body\n"));

        return sb.toString();
    }

}