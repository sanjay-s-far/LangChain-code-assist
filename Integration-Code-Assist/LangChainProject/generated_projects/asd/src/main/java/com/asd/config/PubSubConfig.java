package com.asd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class PubSubConfig {

    private final String subscription = "user-updates-sub"; // Replace with your subscription name

    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(
            org.springframework.cloud.gcp.pubsub.core.PubSubTemplate pubSubTemplate,
            MessageChannel pubsubInputChannel) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, subscription);
        adapter.setOutputChannel(pubsubInputChannel);
        adapter.setAckMode(AckMode.MANUAL); // Important for at-least-once delivery
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver(com.asd.service.PubSubConsumerService pubSubConsumerService) {
        return message -> {
            String payload = new String((byte[]) message.getPayload());
            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE, BasicAcknowledgeablePubsubMessage.class);

            try {
                pubSubConsumerService.processMessage(payload);
                originalMessage.ack(); // Acknowledge the message after successful processing
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                originalMessage.nack(); //Nack message if processing fails
            }
        };
    }
}