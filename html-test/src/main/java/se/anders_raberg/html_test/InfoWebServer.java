package se.anders_raberg.html_test;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class InfoWebServer {
    public static void main(String[] args) throws IOException {
        int port = 9090;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

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

        server.createContext("/", new InfoHandler(info1));
        server.setExecutor(null);
        server.start();
        System.out.println("Debug server started at http://localhost:" + port);
    }

    static class InfoHandler implements HttpHandler {
        private final Map<String, Object> debugData;

        public InfoHandler(Map<String, Object> info) {
            this.debugData = info;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = generateDebugPage(debugData);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        private String generateDebugPage(Map<String, Object> data) {
            StringBuilder html = new StringBuilder();
            html.append("<html><head><style>")
                .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                .append("ul { list-style-type: none; padding-left: 0; }")
                .append("li { margin: 5px 0; cursor: pointer; padding: 5px; border: 1px solid #ccc; background: transparent; border-radius: 5px; }")
                .append(".nested { display: none; margin-left: 15px; padding: 5px; border-left: 3px solid #ddd; }")
                .append(".active { display: block; }")
                .append(".toggle-icon { font-weight: bold; margin-right: 5px; color: #007bff; cursor: pointer; display: inline-block; width: 12px; }")
                .append("li:hover { background: #f1f1f1; }")
                .append("</style>")
                .append("<script>")
                .append("function toggle(event) {")
                .append("  event.stopPropagation();")
                .append("  let parent = event.target.closest('li');")
                .append("  let children = parent.querySelector('.nested');")
                .append("  let icon = parent.querySelector('.toggle-icon');")
                .append("  if (children) {")
                .append("    children.classList.toggle('active');")
                .append("    icon.textContent = children.classList.contains('active') ? '-' : '+';")
                .append("  }")
                .append("}")
                .append("")
                .append("function filterList() {")
                .append("  let searchValue = document.getElementById('searchInput').value.toLowerCase();")
                .append("  let items = document.querySelectorAll('li');")
                .append("  items.forEach(item => {")
                .append("    let text = item.textContent.toLowerCase();")
                .append("    let match = text.includes(searchValue);")
                .append("    if (match) {")
                .append("      item.style.display = 'block';")
                .append("      expandParents(item);") 
                .append("    } else {")
                .append("      item.style.display = 'none';")
                .append("    }")
                .append("  });")
                .append("}")
                .append("")
                .append("function expandParents(item) {")
                .append("  let parentUl = item.closest('ul.nested');")
                .append("  while (parentUl) {")
                .append("    parentUl.classList.add('active');")
                .append("    let parentLi = parentUl.closest('li');")
                .append("    if (parentLi) {")
                .append("      let icon = parentLi.querySelector('.toggle-icon');")
                .append("      if (icon) icon.textContent = '-';")
                .append("    }")
                .append("    parentUl = parentLi ? parentLi.closest('ul.nested') : null;")
                .append("  }")
                .append("}")
                .append("")
                .append("function expandAll(expand) {")
                .append("  let nestedLists = document.querySelectorAll('.nested');")
                .append("  nestedLists.forEach(nested => {")
                .append("    if (expand) {")
                .append("      nested.classList.add('active');")
                .append("      let parentIcon = nested.closest('li').querySelector('.toggle-icon');")
                .append("      if (parentIcon) parentIcon.textContent = '-';")
                .append("    } else {")
                .append("      nested.classList.remove('active');")
                .append("      let parentIcon = nested.closest('li').querySelector('.toggle-icon');")
                .append("      if (parentIcon) parentIcon.textContent = '+';")
                .append("    }")
                .append("  });")
                .append("}")
                .append("")
                .append("</script>")
                .append("</head><body>")
                .append("<h1>Debug Information</h1>")
                .append("<input type='text' id='searchInput' onkeyup='filterList()' placeholder='Search...' ")
                .append("style='width: 100%; padding: 8px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 5px;'>")
                .append("<button onclick='expandAll(true)' style='margin-right: 10px;'>Expand All</button>")
                .append("<button onclick='expandAll(false)'>Collapse All</button>")
                .append(generateHtmlList(data))
                .append("</body></html>");

            return html.toString();
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
                    html.append("<ul class='nested'>")
                        .append(generateHtmlList((Map<String, Object>) entry.getValue()))
                        .append("</ul>");
                } else {
                    html.append(": ").append(entry.getValue());
                }
                html.append("</li>");
            }
            html.append("</ul>");
            return html.toString();
        }
    }
}
