package se.anders_raberg.html_test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class InfiniteNestedMapHttpServer {

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/", new MyHandler());
		server.start();
		System.out.println("Server is listening on port 8080");
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			Map<String, Object> dataMap = new HashMap<>();

			Map<String, Object> jobInfo = new HashMap<>();
			jobInfo.put("Title", "Software Engineer");
			jobInfo.put("Company", "Tech Corp");
			jobInfo.put("Years", "5");

			Map<String, Object> moreJobInfo = new HashMap<>();
			moreJobInfo.put("Previous Title", "Junior Developer");
			moreJobInfo.put("Previous Company", "Old Corp");
			moreJobInfo.put("Previous Years", "2");
			jobInfo.put("Previous Job", moreJobInfo);

			dataMap.put("Job Information", jobInfo);

			String htmlResponse = generateHTMLFromNestedMap(dataMap, "root");

			exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
			OutputStream os = exchange.getResponseBody();
			os.write(htmlResponse.getBytes());
			os.close();
		}

		public String generateHTMLFromNestedMap(Map<String, Object> dataMap, String parentId) {
			StringBuilder htmlBuilder = new StringBuilder();

			if (parentId.equals("root")) {
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
			}

			for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String contentId = parentId + key.replaceAll("\\s", "");

				if (value instanceof Map) {
					htmlBuilder.append("<div class='expandable' onclick=\"toggleContent('").append(contentId)
							.append("')\">+ ").append(key).append("</div>");

					htmlBuilder.append("<div id='").append(contentId).append("' class='content'>");

					htmlBuilder.append(generateHTMLFromNestedMap((Map<String, Object>) value, contentId));

					htmlBuilder.append("</div>");
				} else {
					htmlBuilder.append("<table border=\"1\">");
					htmlBuilder.append("<tr>");
					htmlBuilder.append("<td>").append(key).append("</td>");
					htmlBuilder.append("<td>").append(value.toString()).append("</td>");
					htmlBuilder.append("</tr>");
					htmlBuilder.append("</table>");
				}
			}

			if (parentId.equals("root")) {
				htmlBuilder.append("</body>");
				htmlBuilder.append("</html>");
			}

			return htmlBuilder.toString();
		}
	}
}
