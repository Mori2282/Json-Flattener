package com.jsonflattener;

import com.fasterxml.jackson.databind.JsonNode;
import com.jsonflattener.service.JsonFlattenerService;
import com.jsonflattener.util.AppConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            AppConfig config = new AppConfig();
            File configFile = new File("app.properties");
            if (configFile.exists()) {
                config.load("app.properties");
            }

            String searchKey = config.getProperty("search.key");
            String filterKey = config.getProperty("filter.key");
            String filterVal = config.getProperty("filter.value");

            String inputPath = System.getProperty("INPUT_JSON", "data/input.json");
            File inputFile = new File(inputPath);

            if (!inputFile.exists()) {
                System.err.println("Input file not found: " + inputPath);
                System.exit(1);
            }

            JsonFlattenerService service = new JsonFlattenerService();
            JsonNode root = service.readJson(inputFile);

            List<JsonNode> nodesToProcess = new ArrayList<>();
            if (root.isArray()) {
                root.forEach(nodesToProcess::add);
            } else {
                nodesToProcess.add(root);
            }

            System.out.println("Processing " + nodesToProcess.size() + " object(s)...");
            System.out.println("[");

            boolean first = true;
            for (JsonNode node : nodesToProcess) {
                boolean keep = true;
                if (filterKey != null && filterVal != null) {
                    keep = service.filter(node, filterKey, filterVal);
                }

                if (keep) {
                    if (!first) System.out.println(",");
                    first = false;
                    Map<String, Object> flattened = service.flatten(node);
                    
                    System.out.println("  {");
                    flattened.forEach((k, v) -> System.out.println("    \"" + k + "\": \"" + v + "\","));
                    
                    if (searchKey != null && flattened.containsKey(searchKey)) {
                        System.out.println("    \"_SEARCH_RESULT_" + searchKey + "\": \"" + flattened.get(searchKey) + "\"");
                    }
                    System.out.print("  }");
                }
            }
            System.out.println("\n]");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}