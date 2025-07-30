package se.anders_raberg.rest_test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class HelloHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;

        if ("GET".equalsIgnoreCase(method)) {
            response = "{\"message\": \"Hello, GET request received!\"}";
            exchange.sendResponseHeaders(200, response.length());
        } else if ("POST".equalsIgnoreCase(method)) {
            InputStream requestBody = exchange.getRequestBody();
            String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            response = "{\"message\": \"Hello, POST received!\", \"data\": " + body + "}";
            exchange.sendResponseHeaders(200, response.length());
        } else {
            response = "{\"error\": \"Unsupported request method\"}";
            exchange.sendResponseHeaders(405, response.length());
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}