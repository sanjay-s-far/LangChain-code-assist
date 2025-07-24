package com.qwerty.application.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (IOException e) {
            logger.error("Error parsing JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public static String getText(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && fieldNode.isTextual()) {
            return fieldNode.asText();
        }
        return null;
    }

    public static Boolean getBoolean(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && fieldNode.isBoolean()) {
            return fieldNode.asBoolean();
        }
        return null;
    }
}