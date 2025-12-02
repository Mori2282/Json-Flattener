package com.jsonflattener.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoadAndGetProperty() throws Exception {
        // Создаем временный файл properties для теста
        File configFile = tempDir.resolve("test.properties").toFile();
        Files.write(configFile.toPath(), Collections.singletonList("some.key=someValue"));

        AppConfig config = new AppConfig();
        config.load(configFile.getAbsolutePath());

        assertThat(config.getProperty("some.key")).isEqualTo("someValue");
        assertThat(config.getProperty("missing.key")).isNull();
    }
}