package com.brd1.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class PubSubService {

    private static final Logger logger = LoggerFactory.getLogger(PubSubService.class);

    @Value("${pubsub.projectId}")
    private String projectId;

    @Value("${pubsub.subscriptionId}")
    private String subscriptionId;

    private Subscriber subscriber;

    @PostConstruct
    public void subscribe() throws Exception {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    logger.info("Received message: " + message.getData().toStringUtf8());
                    // Acknowledge the message
                    consumer.ack();
                };

        subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();

        subscriber.startAsync().awaitRunning();
        logger.info("Started listening for messages on " + subscriptionName);

    }

    @PreDestroy
    public void stop() throws TimeoutException {
        if (subscriber != null) {
            subscriber.stopAsync().awaitTerminated(5, TimeUnit.SECONDS);
            logger.info("Stopped listening for messages");
        }
    }

    public void publishMessage(String topic, String message) {
        // Placeholder for publishing messages.  Needs Pub/Sub publisher setup.
        logger.info("Publishing message: {} to topic: {}", message, topic);
    }
}