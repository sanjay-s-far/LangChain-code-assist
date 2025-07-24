package com.rl.integration.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PubSubSubscriberService {

    @Value("${pubsub.subscription-id}")
    private String subscriptionId;

    @Autowired
    private PubSubTemplate pubSubTemplate;


    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void receiveMessage(BasicAcknowledgeablePubsubMessage message, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) com.google.cloud.pubsub.v1.Message originalMessage) {
        log.info("Received message: {}", message.getPayload());
        log.info("Message Headers: {}", originalMessage.getAttributesMap());
        // TODO: Implement message processing logic here
        message.ack();
    }
}