package j.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;

@Service
public class PubSubService {

    private final PubSubTemplate pubSubTemplate;

    @Autowired
    public PubSubService(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void publishMessage(String topic, String message) {
        pubSubTemplate.publish(topic, message);
        System.out.println("Published message to topic: " + topic + ", message: " + message);
    }
}