package com.example.pub.integration.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PubSubService {

    private final PubSubTemplate pubSubTemplate;

    @Autowired
    public PubSubService(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void sendMessage(String topic, String message) {
        pubSubTemplate.publish(topic, message);
        System.out.println("Sent message to Pub/Sub topic " + topic + ": " + message);
    }

    // TODO: Implement a message listener using @PubSubListener
}