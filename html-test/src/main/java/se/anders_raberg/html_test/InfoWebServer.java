package se.anders_raberg.html_test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class InfoWebServer {
    public static void main(String[] args) throws IOException {
        Map<String, Object> info3 = new HashMap<>();
        info3.put("Foxtrot", "1");
        info3.put("Golf", "2");

        Map<String, Object> info2 = new HashMap<>();
        info2.put("Charlie", "3");
        info2.put("Delta", "4");
        info2.put("Echo", info3);

        Map<String, Object> info1 = new HashMap<>();
        info1.put("Bravo", info2);
        info1.put("Alpha", 5);

        int port = 9090;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new InfoPageHandler(info1));
        server.createContext("/static", new StaticFileHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + port);
    }

    static class InfoPageHandler implements HttpHandler {
        private final Map<String, Object> _info;

        public InfoPageHandler(Map<String, Object> info) {
            _info = info;
        }

        public void handle(HttpExchange exchange) throws IOException {
            String response = generateInfoPage(_info);
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private String generateInfoPage(Map<String, Object> data) {
            return """
                        <html>
                        <head>
                            <link rel="stylesheet" href="/static/styles.css">
                            <script src="/static/script.js"></script>
                        </head>
                        <body>
                            <h1>Debug Information</h1>
                            <input type="text" id="searchInput" onkeyup="filterList()" placeholder="Search...">
                            <button onclick="expandAll(true)">Expand All</button>
                            <button onclick="expandAll(false)">Collapse All</button>
                            %s
                        </body>
                        </html>
                    """.formatted(generateHtmlList(data));
        }

        private String generateHtmlList(Map<String, Object> data) {
            StringBuilder html = new StringBuilder();
            html.append("<ul>");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                boolean isNested = entry.getValue() instanceof Map;

                html.append("<li>");

                if (isNested) {
                    html.append("<span class='toggle-icon' onclick='toggle(event)'>+</span>");
                } else {
                    html.append("<span style='display:inline-block; width:12px;'></span>");
                }

                html.append("<b>").append(entry.getKey()).append("</b>");

                if (isNested) {
                    html.append("<ul class='nested'>").append(generateHtmlList((Map<String, Object>) entry.getValue()))
                            .append("</ul>");
                } else {
                    html.append(" = ").append(entry.getValue());
                }
                html.append("</li>");
            }
            html.append("</ul>");
            return html.toString();
        }

    }

    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String requestedFile = exchange.getRequestURI().getPath().replace("/static/", "");
            String resourcePath = "/static/" + requestedFile;
            InputStream resourceStream = InfoWebServer.class.getResourceAsStream(resourcePath);

            if (resourceStream != null) {
                byte[] fileBytes = resourceStream.readAllBytes();
                exchange.sendResponseHeaders(200, fileBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileBytes);
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().close();
            }
        }
    }
}
