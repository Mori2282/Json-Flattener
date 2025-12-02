package com.jsonflattener.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonFlattenerServiceTest {

    private final JsonFlattenerService service = new JsonFlattenerService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testFlattenSimple() throws Exception {
        String json = "{ \"user\": { \"name\": \"Test\" } }";
        JsonNode node = mapper.readTree(json);
        
        Map<String, Object> result = service.flatten(node);
        
        assertThat(result).containsEntry("user.name", "Test");
    }

    @Test
    void testFlattenComplexAndArrays() throws Exception {
        String json = "{ \"a\": { \"b\": 10 }, \"list\": [1, 2], \"str\": \"val\" }";
        JsonNode node = mapper.readTree(json);
        
        Map<String, Object> result = service.flatten(node);
        
        assertThat(result)
            .containsEntry("a.b", "10")
            .containsEntry("str", "val");
            
        assertThat(result.get("list").toString()).contains("[1,2]");
    }

    @Test
    void testFilterActive() throws Exception {
        String json = "{ \"user\": { \"status\": \"ACTIVE\" } }";
        JsonNode node = mapper.readTree(json);
        
        boolean keep = service.filter(node, "user.status", "ACTIVE");
        
        assertThat(keep).isTrue();
    }

    @Test
    void testFilterInactive() throws Exception {
        String json = "{ \"user\": { \"status\": \"BLOCKED\" } }";
        JsonNode node = mapper.readTree(json);
        
        boolean keep = service.filter(node, "user.status", "ACTIVE");
        
        assertThat(keep).isFalse();
    }

    @Test
    void testFilterMissingKey() throws Exception {
        String json = "{ \"user\": { \"name\": \"NoStatus\" } }";
        JsonNode node = mapper.readTree(json);
        
        boolean keep = service.filter(node, "user.status", "ACTIVE");
        
        assertThat(keep).isFalse();
    }
    
    @Test
    void testReadFile(@org.junit.jupiter.api.io.TempDir Path tempDir) throws IOException {
        File jsonFile = tempDir.resolve("input.json").toFile();
        Files.writeString(jsonFile.toPath(), "{}");
        
        JsonNode node = service.readJson(jsonFile);
        assertThat(node).isNotNull();
    }
}