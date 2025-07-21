import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateOrderConnectorPublisher {
	
	@Value("${spring.cloud.gcp.topic}")
	private String topic;
	
	@Autowired
	private PubSubTemplate pubSubTemplate;

	public void sendToPubSub(String message)
			throws InterruptedException, ExecutionException, TimeoutException, IOException {
	
		CompletableFuture<String> future = pubSubTemplate.publish(topic, message);
		String messageId = future.get();
		log.info("Published message to topic {} with message ID: {}",topic, messageId);
		
	}
}
