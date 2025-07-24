package com.qwerty.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.qwerty.application.model.UserData;
import com.qwerty.application.util.JsonUtils;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.springframework.messaging.MessageHandler;

@Service
public class PubSubConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(PubSubConsumerService.class);

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Value("${pubsub.topic}")
    private String pubSubTopic;

    @Value("${pubsub.subscription}")
    private String pubSubSubscription;

    // In a real application, you would inject your JPA repository here.
    // @Autowired
    // private UserRepository userRepository;

    @PostConstruct
    public void subscribe() {
        pubSubTemplate.subscribe(pubSubSubscription, messageHandler());
    }

    private MessageHandler messageHandler() {
        return message -> {
            BasicAcknowledgeablePubsubMessage pubsubMessage = message.getPayload();
            String messageId = pubsubMessage.getPubsubMessage().getMessageId();

            try {
                String payload = new String(pubsubMessage.getPubsubMessage().getData().toByteArray());
                logger.info("Received message: {} with payload: {}", messageId, payload);

                JsonNode jsonNode = JsonUtils.parseJson(payload);
                if (jsonNode != null) {
                    UserData userData = mapJsonToUserData(jsonNode);
                    saveUserData(userData);
                    pubsubMessage.ack(); // Acknowledge the message after successful processing.
                    logger.info("Message {} processed and acknowledged.", messageId);

                } else {
                    logger.error("Failed to parse JSON for message: {}", messageId);
                    handleError(pubsubMessage, "Failed to parse JSON");
                }
            } catch (Exception e) {
                logger.error("Error processing message {}: {}", messageId, e.getMessage(), e);
                handleError(pubsubMessage, "Error processing message: " + e.getMessage());
            }
        };
    }
    @Transactional
    public void saveUserData(UserData userData) {
        // In a real implementation, you would use your JPA repository to save the data.
        // userRepository.save(userData);
        logger.info("Simulating saving user data: {}", userData); // Placeholder
    }

    private UserData mapJsonToUserData(JsonNode jsonNode) {
        UserData userData = new UserData();
        userData.setUserId(JsonUtils.getText(jsonNode, "userId"));
        userData.setFullName(JsonUtils.getText(jsonNode, "name"));
        userData.setEmailAddress(JsonUtils.getText(jsonNode, "email"));

        String signupDateString = JsonUtils.getText(jsonNode, "signupDate");
        if (signupDateString != null) {
            try {
                userData.setSignupDate(Instant.parse(signupDateString));
            } catch (Exception e) {
                logger.warn("Invalid signupDate format: {}", signupDateString);
                userData.setSignupDate(null); // Or handle the error appropriately
            }
        }

        userData.setIsActive(JsonUtils.getBoolean(jsonNode, "isActive"));
        return userData;
    }

    private void handleError(BasicAcknowledgeablePubsubMessage message, String errorMessage) {
        // Nack the message to trigger redelivery, or publish to a dead-letter topic.
        // message.nack(); // Simple Nack.  Good for testing.

        // Example of publishing to a dead-letter topic (uncomment to use):
        // publishToDeadLetterTopic(message, errorMessage);

        logger.error("Error handling message: {}", errorMessage);
        message.ack(); // Acknowledge to prevent endless loop if dead letter not implemented.
    }

    private void publishToDeadLetterTopic(BasicAcknowledgeablePubsubMessage message, String errorMessage) {
        String deadLetterTopic = "projects/your-project-id/topics/dead-letter-topic";  // Replace with your dead-letter topic
        String payload = new String(message.getPubsubMessage().getData().toByteArray());

        CompletableFuture.runAsync(() -> {
            try {
                pubSubTemplate.publish(deadLetterTopic, payload, message.getPubsubMessage().getAttributes());
                message.ack(); // Acknowledge original message after publishing to dead-letter.
                logger.info("Message published to dead-letter topic: {}", deadLetterTopic);
            } catch (Exception e) {
                logger.error("Failed to publish to dead-letter topic: {}", e.getMessage(), e);
                // Consider retrying or logging the error persistently.  May need to Nack original message.
            }
        });
    }
}