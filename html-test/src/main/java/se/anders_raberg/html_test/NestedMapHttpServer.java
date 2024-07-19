package se.anders_raberg.html_test;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NestedMapHttpServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());

        server.start();
        System.out.println("Server is listening on port 8080");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Map<String, String>> dataMap = new HashMap<>();
            
            Map<String, String> personalInfo = new HashMap<>();
            personalInfo.put("Name", "John Doe");
            personalInfo.put("Age", "30");
            personalInfo.put("City", "New York");

            Map<String, String> jobInfo = new HashMap<>();
            jobInfo.put("Title", "Software Engineer");
            jobInfo.put("Company", "Tech Corp");
            jobInfo.put("Years", "5");

            dataMap.put("Personal Information", personalInfo);
            dataMap.put("Job Information", jobInfo);

            String htmlResponse = generateHTMLFromNestedMap(dataMap);

            exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(htmlResponse.getBytes());
            os.close();
        }

        public String generateHTMLFromNestedMap(Map<String, Map<String, String>> dataMap) {
            StringBuilder htmlBuilder = new StringBuilder();

            htmlBuilder.append("<!DOCTYPE html>");
            htmlBuilder.append("<html>");
            htmlBuilder.append("<head>");
            htmlBuilder.append("<title>User Information</title>");
            htmlBuilder.append("</head>");
            htmlBuilder.append("<body>");

            for (Map.Entry<String, Map<String, String>> categoryEntry : dataMap.entrySet()) {
                String category = categoryEntry.getKey();
                Map<String, String> subMap = categoryEntry.getValue();

                htmlBuilder.append("<h2>").append(category).append("</h2>");

                htmlBuilder.append("<table border=\"1\">");

                for (Map.Entry<String, String> entry : subMap.entrySet()) {
                    String label = entry.getKey();
                    String value = entry.getValue();

                    htmlBuilder.append("<tr>");
                    htmlBuilder.append("<td>").append(label).append("</td>");
                    htmlBuilder.append("<td>").append(value).append("</td>");
                    htmlBuilder.append("</tr>");
                }

                htmlBuilder.append("</table>");
            }

            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");

            return htmlBuilder.toString();
        }
    }
}
