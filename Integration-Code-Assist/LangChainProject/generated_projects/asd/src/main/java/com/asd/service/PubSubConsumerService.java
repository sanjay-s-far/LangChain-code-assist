package com.asd.service;

import com.asd.model.UserData;
import com.asd.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
public class PubSubConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(PubSubConsumerService.class);

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public PubSubConsumerService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public void processMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract data from JSON
            String userId = jsonNode.get("userId").asText();
            String name = jsonNode.get("name").asText();
            String email = jsonNode.get("email").asText();
            Instant signupDate = Instant.parse(jsonNode.get("signupDate").asText());
            boolean isActive = jsonNode.get("isActive").asBoolean();

            // Create UserData object
            UserData userData = new UserData();
            userData.setUserId(userId);
            userData.setFullName(name);
            userData.setEmailAddress(email);
            userData.setSignupDate(signupDate);
            userData.setActive(isActive);

            // Save to database
            userRepository.save(userData);

            logger.info("Processed and saved user data: {}", userData);

        } catch (IOException e) {
            logger.error("Error parsing JSON message: {}", e.getMessage());
            // Optionally send to Dead Letter Topic
        } catch (Exception e) {
            logger.error("Error saving user data: {}", e.getMessage());
            // Optionally send to Dead Letter Topic
        }
    }
}