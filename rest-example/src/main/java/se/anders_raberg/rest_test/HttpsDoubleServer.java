package se.anders_raberg.rest_test;

import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;

public class HttpsDoubleServer {
    public static void main(String[] args) throws Exception {
        char[] passphrase = "changeit".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(HttpsDoubleServer.class.getResourceAsStream("/server.keystore"), passphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext));

        server.createContext("/double", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            if (parts.length < 3) {
                exchange.sendResponseHeaders(400, 0);
                exchange.getResponseBody().close();
                return;
            }

            try {
                int x = Integer.parseInt(parts[2]);
                int result = 2 * x;
                String json = "{ \"result\": " + result + " }";

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                exchange.getResponseBody().write(json.getBytes());
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, 0);
            }
            exchange.close();
        });

        server.start();
        System.out.println("HTTPS server running at https://localhost:8443/double/{x}");
    }
}
