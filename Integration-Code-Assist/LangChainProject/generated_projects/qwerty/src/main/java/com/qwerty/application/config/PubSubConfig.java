package com.qwerty.application.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubSubConfig {

    // You can configure PubSubTemplate here if needed,
    // but the default configuration should be sufficient
    // if you have the necessary GCP credentials set up.

    @Bean
    public PubSubTemplate pubSubTemplate() {
        return new PubSubTemplate();
    }
}