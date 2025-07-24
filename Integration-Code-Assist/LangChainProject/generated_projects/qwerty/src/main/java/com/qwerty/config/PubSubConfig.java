package com.qwerty.config;

import com.qwerty.service.PubSubMessageReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class PubSubConfig {

    @Value("${pubsub.subscription}")
    private String subscription;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private PubSubMessageReceiver pubSubMessageReceiver;

    @Bean
    public MessageChannel pubSubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(PubSubTemplate pubSubTemplate,
                                                            MessageChannel pubSubInputChannel) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, subscription);
        adapter.setOutputChannel(pubSubInputChannel);
        adapter.setAckMode(AckMode.MANUAL); // Important for at-least-once delivery
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            try {
                pubSubMessageReceiver.handleMessage(message);
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                throw e;
            }
        };
    }
}