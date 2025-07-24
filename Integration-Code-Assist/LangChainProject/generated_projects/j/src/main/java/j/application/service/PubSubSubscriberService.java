package j.application.service;

import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;

@Service
public class PubSubSubscriberService {

    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            BasicAcknowledgeablePubsubMessage pubsubMessage = (BasicAcknowledgeablePubsubMessage) message.getPayload();
            String payload = new String(pubsubMessage.getPubsubMessage().getData().toByteArray());
            System.out.println("Received message: " + payload);
            pubsubMessage.ack();
        };
    }
}