package com.newapplication.pubsub;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.TopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PubSubService {

    private static final Logger logger = LoggerFactory.getLogger(PubSubService.class);

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    public void publishMessage(String topicId, String message) {
        TopicName topicName = TopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            publisher.publish(pubsubMessage).get(30, TimeUnit.SECONDS);
            logger.info("Published message to topic " + topicId + ": " + message);

        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            logger.error("Error publishing message: " + e.getMessage(), e);
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                    publisher.awaitTermination(5, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    logger.error("Error shutting down publisher: " + e.getMessage(), e);
                }
            }
        }
    }
}