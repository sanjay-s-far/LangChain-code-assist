package com.ff.service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PubSubService {

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${pubsub.topic.id}")
    private String topicId;

    @Value("${pubsub.subscription.id}")
    private String subscriptionId;

    private Publisher publisher;
    private Subscriber subscriber;

    @PostConstruct
    public void init() throws IOException {
        // Initialize Publisher
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        publisher = Publisher.newBuilder(topicName).build();

        // Initialize Subscriber
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    log.info("Received message: " + message.getData().toStringUtf8());
                    consumer.ack(); // Acknowledge the message
                };

        subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
        subscriber.startAsync().awaitRunning();
        log.info("Pub/Sub Subscriber started.");
    }

    public void sendMessage(String message) {
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        try {
            publisher.publish(pubsubMessage).get();
            log.info("Published message to Pub/Sub: " + message);
        } catch (Exception e) {
            log.error("Error publishing message: " + e.getMessage(), e);
        }
    }

    public void receiveMessage() {
        // Message receiving is handled by the subscriber in the init() method.
        // This method is left empty as the subscriber runs asynchronously.
    }

    @PreDestroy
    public void shutdown() throws Exception {
        if (publisher != null) {
            publisher.shutdown();
            publisher.awaitTermination(5, TimeUnit.SECONDS);
        }
        if (subscriber != null) {
            subscriber.stopAsync().awaitTerminated();
        }
    }
}