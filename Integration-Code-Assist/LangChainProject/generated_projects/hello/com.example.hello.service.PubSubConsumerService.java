package com.example.hello.service;

import com.example.hello.model.UserData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import com.example.hello.repository.UserDataRepository;

import java.io.IOException;
import java.time.Instant;

@Service
public class PubSubConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubSubConsumerService.class);

    @Autowired
    UserDataRepository userDataRepository;

    @Value("${pubsub.dead-letter-topic}")
    private String deadLetterTopic;

    @Value("${pubsub.subscription}")
    private String subscription;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        LOGGER.info("Starting PubSubConsumerService for subscription: {}", subscription);
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            AcknowledgeablePubsubMessage ackableMessage = (AcknowledgeablePubsubMessage) message.getPayload();
            try {
                String payload = new String(ackableMessage.getPubsubMessage().getData().toByteArray());
                LOGGER.info("Received message: {}", payload);

                JsonNode jsonNode = objectMapper.readTree(payload);
                UserData userData = parseUserData(jsonNode);

                if (userData != null) {
                    userDataRepository.save(userData);
                    LOGGER.info("Saved UserData: {}", userData.getUserId());
                    ackableMessage.ack(); // Acknowledge the message on success
                } else {
                    LOGGER.warn("Invalid message format. Sending to dead-letter topic.");
                    //TODO: Implement Dead Letter Topic logic (e.g., using PubSubTemplate.publish())
                    ackableMessage.nack(); //Nack the message, can be re-processed or sent to dead letter
                }

            } catch (IOException e) {
                LOGGER.error("Error processing message: {}", e.getMessage(), e);
                //TODO: Implement Dead Letter Topic logic
                ackableMessage.nack();
            }
        };
    }


    private UserData parseUserData(JsonNode jsonNode) {
        try {
            UserData userData = new UserData();
            userData.setUserId(jsonNode.get("userId").asText());
            userData.setFullName(jsonNode.get("name").asText());
            userData.setEmailAddress(jsonNode.get("email").asText());

            //Handle potential nulls or invalid formats
            JsonNode signupDateNode = jsonNode.get("signupDate");
            if (signupDateNode != null && !signupDateNode.isNull()) {
                userData.setSignupDate(Instant.parse(signupDateNode.asText()));
            }

            userData.setActive(jsonNode.get("isActive").asBoolean());

            return userData;
        } catch (Exception e) {
            LOGGER.error("Error parsing JSON: {}", e.getMessage(), e);
            return null; // Indicate parsing failure
        }
    }
}