package com.jsonflattener.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Custom JSON object representation class.
 */
public class JsonObject {
    private final Map<String, Object> properties;
    
    public JsonObject() {
        this.properties = new HashMap<>();
    }
    
    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return properties.get(key);
    }
    
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
    
    public Set<String> getKeys() {
        return properties.keySet();
    }
    
    public boolean isEmpty() {
        return properties.isEmpty();
    }
    
    @Override
    public String toString() {
        return "JsonObject{properties=" + properties + "}";
    }
}