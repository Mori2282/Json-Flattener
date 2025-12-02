package com.jsonflattener.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonflattener.model.JsonNodeType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Service for flattening and processing JSON data.
 */
public class JsonFlattenerService {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode readJson(File file) throws IOException {
        return mapper.readTree(file);
    }

    /**
     * Determines the type of the JSON node using our custom enum.
     */
    public JsonNodeType determineType(JsonNode node) {
        if (node.isObject()) return JsonNodeType.OBJECT;
        if (node.isArray()) return JsonNodeType.ARRAY;
        return JsonNodeType.VALUE;
    }

    public Map<String, Object> flatten(JsonNode node) {
        Map<String, Object> flatMap = new HashMap<>();
        flattenRecursively("", node, flatMap);
        return flatMap;
    }

    private void flattenRecursively(String currentPath, JsonNode node, Map<String, Object> map) {
        JsonNodeType type = determineType(node);

        if (type == JsonNodeType.OBJECT) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String newPath = currentPath.isEmpty() ? field.getKey() : currentPath + "." + field.getKey();
                flattenRecursively(newPath, field.getValue(), map);
            }
        } else if (type == JsonNodeType.ARRAY) {
            map.put(currentPath, node.toString());
        } else {
            map.put(currentPath, node.asText());
        }
    }

    public boolean filter(JsonNode root, String filterKey, String filterValue) {
        String jsonPointer = "/" + filterKey.replace('.', '/');
        JsonNode targetNode = root.at(jsonPointer);
        return !targetNode.isMissingNode() && targetNode.asText().equals(filterValue);
    }
}