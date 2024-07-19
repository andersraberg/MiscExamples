/*
 * This source file was generated by the Gradle 'init' task
 */
package se.anders_raberg.html_test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HtmlTest {

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
			// Sample data in a Map
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("Name", "John Doe");
			dataMap.put("Age", "30");
			dataMap.put("City", "New York");

			// Generate HTML page from the Map
			String htmlResponse = generateHTMLFromMap(dataMap);

			// Send HTTP response
			exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(htmlResponse.getBytes());
			os.close();
		}

		public String generateHTMLFromMap(Map<String, String> dataMap) {
			StringBuilder htmlBuilder = new StringBuilder();

			// Start HTML document
			htmlBuilder.append("<!DOCTYPE html>");
			htmlBuilder.append("<html>");
			htmlBuilder.append("<head>");
			htmlBuilder.append("<title>User Information</title>");
			htmlBuilder.append("</head>");
			htmlBuilder.append("<body>");

			// Create a table to display data
			htmlBuilder.append("<table border=\"1\">");

			// Iterate over the Map entries to create table rows
			for (Map.Entry<String, String> entry : dataMap.entrySet()) {
				String label = entry.getKey();
				String value = entry.getValue();

				// Create a table row with label and value
				htmlBuilder.append("<tr>");
				htmlBuilder.append("<td>").append(label).append("</td>");
				htmlBuilder.append("<td>").append(value).append("</td>");
				htmlBuilder.append("</tr>");
			}

			// Close the table and HTML body
			htmlBuilder.append("</table>");
			htmlBuilder.append("</body>");
			htmlBuilder.append("</html>");

			return htmlBuilder.toString();
		}
	}
}