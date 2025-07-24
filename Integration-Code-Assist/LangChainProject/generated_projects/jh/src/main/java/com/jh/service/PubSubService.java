package com.jh.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PubSubService {

    private final PubSubTemplate pubSubTemplate;

    public void publishMessage(String topic, String message) {
        pubSubTemplate.publish(topic, message);
    }

    //TODO: Implement subscriber logic
}