package com.rl.integration.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PubSubPublisherService {

    @Autowired
    private PubSubTemplate pubSubTemplate;

    public void publishMessage(String topic, String message) {
        log.info("Publishing message to topic {}: {}", topic, message);
        pubSubTemplate.publish(topic, message);
    }
}