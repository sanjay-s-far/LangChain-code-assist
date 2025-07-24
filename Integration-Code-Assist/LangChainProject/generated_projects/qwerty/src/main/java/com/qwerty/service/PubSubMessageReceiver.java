package com.qwerty.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwerty.model.UserData;
import com.qwerty.repository.UserDataRepository;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;

@Service
public class PubSubMessageReceiver {

    private static final Logger logger = LoggerFactory.getLogger(PubSubMessageReceiver.class);

    @Autowired
    private UserDataRepository userDataRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void handleMessage(Message<?> message) {
        String payload = new String((byte[]) message.getPayload());
        BasicAcknowledgeablePubsubMessage originalMessage =
                message.getHeaders().get("originalMessage", BasicAcknowledgeablePubsubMessage.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            UserData userData = new UserData();
            userData.setUserId(jsonNode.get("userId").asText());
            userData.setFullName(jsonNode.get("name").asText());
            userData.setEmailAddress(jsonNode.get("email").asText());
            userData.setSignupDate(Instant.parse(jsonNode.get("signupDate").asText()));
            userData.setActive(jsonNode.get("isActive").asBoolean());

            userDataRepository.save(userData);
            logger.info("Successfully processed message for user ID: {}", userData.getUserId());
            originalMessage.ack(); // Acknowledge the message after successful processing
        } catch (IOException e) {
            logger.error("Error parsing JSON: {}", e.getMessage(), e);
            originalMessage.nack(); // Negative acknowledge the message on parsing error
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            originalMessage.nack(); // Negative acknowledge the message on any other error
        }
    }
}