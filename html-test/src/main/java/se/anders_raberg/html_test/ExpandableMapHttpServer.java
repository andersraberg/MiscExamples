package se.anders_raberg.html_test;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ExpandableMapHttpServer {

    public static void main(String[] args) throws IOException {
        // Create a new HttpServer instance listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define a context that listens for requests at the root URI "/"
        server.createContext("/", new MyHandler());

        // Start the server
        server.start();
        System.out.println("Server is listening on port 8080");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Sample data in a nested Map
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

            // Generate HTML page from the nested Map
            String htmlResponse = generateHTMLFromNestedMap(dataMap);

            // Send HTTP response
            exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(htmlResponse.getBytes());
            os.close();
        }

        public String generateHTMLFromNestedMap(Map<String, Map<String, String>> dataMap) {
            StringBuilder htmlBuilder = new StringBuilder();

            // Start HTML document
            htmlBuilder.append("<!DOCTYPE html>");
            htmlBuilder.append("<html>");
            htmlBuilder.append("<head>");
            htmlBuilder.append("<title>User Information</title>");
            htmlBuilder.append("<style>");
            htmlBuilder.append(".expandable { cursor: pointer; }");
            htmlBuilder.append(".content { display: none; margin-left: 20px; }");
            htmlBuilder.append("</style>");
            htmlBuilder.append("<script>");
            htmlBuilder.append("function toggleContent(id) {");
            htmlBuilder.append("  var content = document.getElementById(id);");
            htmlBuilder.append("  if (content.style.display === 'none') {");
            htmlBuilder.append("    content.style.display = 'block';");
            htmlBuilder.append("  } else {");
            htmlBuilder.append("    content.style.display = 'none';");
            htmlBuilder.append("  }");
            htmlBuilder.append("}");
            htmlBuilder.append("</script>");
            htmlBuilder.append("</head>");
            htmlBuilder.append("<body>");

            // Iterate over the outer Map entries
            for (Map.Entry<String, Map<String, String>> categoryEntry : dataMap.entrySet()) {
                String category = categoryEntry.getKey();
                Map<String, String> subMap = categoryEntry.getValue();

                // Add an expandable heading for each category
                String contentId = category.replaceAll("\\s", "") + "Content";
                htmlBuilder.append("<div class='expandable' onclick=\"toggleContent('")
                           .append(contentId)
                           .append("')\">+ ")
                           .append(category)
                           .append("</div>");

                // Create a hidden div to display data for each category
                htmlBuilder.append("<div id='")
                           .append(contentId)
                           .append("' class='content'>");

                // Create a table to display data for each category
                htmlBuilder.append("<table border=\"1\">");

                // Iterate over the inner Map entries to create table rows
                for (Map.Entry<String, String> entry : subMap.entrySet()) {
                    String label = entry.getKey();
                    String value = entry.getValue();

                    // Create a table row with label and value
                    htmlBuilder.append("<tr>");
                    htmlBuilder.append("<td>").append(label).append("</td>");
                    htmlBuilder.append("<td>").append(value).append("</td>");
                    htmlBuilder.append("</tr>");
                }

                // Close the table for the current category
                htmlBuilder.append("</table>");
                htmlBuilder.append("</div>");
            }

            // Close the HTML body and document
            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");

            return htmlBuilder.toString();
        }
    }
}
