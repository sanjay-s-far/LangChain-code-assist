package com.example.j.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class PubSubService {

    private final PubSubTemplate pubSubTemplate;

    public CompletableFuture<String> publishMessage(String topic, String message) {
        Message<String> pubsubMessage = MessageBuilder.withPayload(message).build();
        return pubSubTemplate.publishAsync(topic, pubsubMessage)
                .thenApply(messageId -> {
                    log.info("Published message ID: {}", messageId);
                    return messageId;
                });
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void handleMessage(
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        log.info("Received message: {}", message.getPayload());
        message.ack();
    }
}