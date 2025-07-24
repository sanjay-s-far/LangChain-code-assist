package com.asd.service;

import com.asd.model.UserData;
import com.asd.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
import java.time.Instant;
import java.util.Set;

@Service
public class PubSubMessageReceiver {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processMessage(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UserData userData = new UserData();
            userData.setUserId(jsonNode.get("userId").asText());
            userData.setFullName(jsonNode.get("name").asText());
            userData.setEmailAddress(jsonNode.get("email").asText());

            // Handle signupDate conversion (example)
            String signupDateString = jsonNode.get("signupDate").asText();
            userData.setSignupDate(Instant.parse(signupDateString)); // Assuming ISO-8601 format

            userData.setActive(jsonNode.get("isActive").asBoolean());

            Set<ConstraintViolation<UserData>> violations = validator.validate(userData);
            if (!violations.isEmpty()) {
                System.err.println("Validation errors: " + violations);
                return; // Or push to dead letter queue
            }

            userRepository.save(userData);
            System.out.println("Saved user: " + userData.getUserId());

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            // Optionally push to dead letter queue
        }
    }
}