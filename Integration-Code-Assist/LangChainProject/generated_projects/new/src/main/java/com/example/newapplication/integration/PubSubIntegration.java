package com.example.newapplication.integration;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class PubSubIntegration {

    private static final Logger logger = LoggerFactory.getLogger(PubSubIntegration.class);

    @Value("${pubsub.project-id}")
    private String projectId;

    @Value("${pubsub.subscription-id}")
    private String subscriptionId;

    @PostConstruct
    public void subscribe() throws IOException, InterruptedException, TimeoutException {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    logger.info("Received message: " + message.getData().toStringUtf8());
                    // Acknowledge the message
                    consumer.ack();
                    //TODO: Trigger downstream processing here
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning(5, TimeUnit.SECONDS);
            logger.info("Listening for messages on " + subscriptionName.toString());
            subscriber.awaitTerminated();

        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }
}