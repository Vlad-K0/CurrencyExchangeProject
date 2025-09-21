package org.example.currencyexchangeproject.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class YamlUtil {
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static String get(String key) {
        try (InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (inputStream == null) {
                throw new IOException("Файл config.yaml не найден в ресурсах!");
            }

            JsonNode rootNode = mapper.readTree(inputStream);

            JsonNode result = rootNode.at(key);
            if (result.isMissingNode()) {
                throw new IOException("Ключ" + result + "не найден в config.yaml!");
            }
            return result.asText();

        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить конфигурацию из config.yaml", e);
        }
    }
}
