


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PubSubConfig {

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private MessageService messageService;
	
	@Value("${inbound.subscription}")
	private String subscription;
	@Value("${outbound.topic}") 
	String outboundTopic;

	@Bean
	public MessageChannel inputMessageChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public PubSubInboundChannelAdapter inboundChannelAdapter(
			@Qualifier("inputMessageChannel") MessageChannel messageChannel, PubSubTemplate pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, this.subscription);
		adapter.setOutputChannel(messageChannel);
		adapter.setAckMode(AckMode.MANUAL);
		adapter.setPayloadType(String.class);
		return adapter;
	}

	@Observed(name = "subscription")
	@ServiceActivator(inputChannel = "inputMessageChannel")
	public void messageReceiver(String payload,
			@Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
		String messageId = message.getPubsubMessage().getMessageId();
		
		log.info("Request: {}, Message ID: {}, Attributes: {}", CommonUtils.toSingleLine(payload), messageId,
				message.getPubsubMessage().getAttributesMap());

		if (isDuplicateMessageId(messageId)) {
			message.ack();
			log.warn("Duplicate message received and acknowledged without processing. Message ID: {}", messageId);
			return;
		}

		Map<String, String> attributes = message.getPubsubMessage().getAttributesMap();
		String cartonTypeValue = null;
		if (null != attributes && attributes.containsKey("CartonType")) {
			cartonTypeValue = attributes.get("CartonType");

			try {
				var inventoryUpdateModel = JsonUtils.deserializeFromJson(payload, InventoryUpdateModel.class);
				var inventoryEvent = InventoryEventMapper.map(inventoryUpdateModel, cartonTypeValue);

				String outboundMessage = JsonUtils.serializeToJson(inventoryEvent);
				log.info("Inventory Event Outbound topis json is " + outboundMessage);
				messageService.send(outboundTopic, outboundMessage);

				log.info("Message published successfully Message ID: {}", messageId);

				putMessageIdInCache(messageId);
				message.ack();
				log.info("Message is acknowledged. Message ID: {}", messageId);
			} catch (Exception e) {
				message.nack();
				log.error(
						"Failed to process store inventory event, message is not acknowledged. Message ID: {}, Exception: {}",
						messageId, e.getMessage(), e);
			}
		}

		else {
			log.warn("CartonType attribute is mandatory and the values are Received/Transfer");
			message.ack();
			log.info("Message is acknowledged for not existing CartonType case. Message ID: {}", messageId);
		}
	}

	private Cache getMessageIdCache() {
		return cacheManager.getCache(CACHE_MESSAGE_IDS);
	}

	private boolean isDuplicateMessageId(String messageId) {
		Cache cache = getMessageIdCache();
		return cache != null && cache.get(messageId) != null;
	}

	private void putMessageIdInCache(String messageId) {
		Cache cache = getMessageIdCache();

		if (cache != null) {
			cache.put(messageId, messageId);
		}
	}

}
