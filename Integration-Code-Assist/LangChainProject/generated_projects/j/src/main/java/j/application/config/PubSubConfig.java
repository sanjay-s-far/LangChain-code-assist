package j.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;

@Configuration
public class PubSubConfig {

    @Value("${pubsub.subscription-id}")
    private String subscriptionId;

    @Bean
    public JacksonPubSubMessageConverter jacksonPubSubMessageConverter() {
        return new JacksonPubSubMessageConverter();
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

}