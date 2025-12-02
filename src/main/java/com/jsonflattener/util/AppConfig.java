package com.jsonflattener.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private final Properties properties = new Properties();

    public void load(String configPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}