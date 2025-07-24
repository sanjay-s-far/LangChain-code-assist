package com.ff.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ApiService {

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${external.api.username}")
    private String externalApiUsername;

    @Value("${external.api.password}")
    private String externalApiPassword;

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchDataFromExternalApi() {
        try {
            HttpHeaders headers = new HttpHeaders();
            // Basic Authentication
            String auth = externalApiUsername + ":" + externalApiPassword;
            byte[] encodedAuth = java.util.Base64.getEncoder().encode(auth.getBytes());
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("Authorization", authHeader);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(externalApiUrl, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched data from external API.");
                return response.getBody();
            } else {
                log.error("Failed to fetch data from external API. Status code: " + response.getStatusCode());
                throw new RuntimeException("Failed to fetch data from external API. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error fetching data from external API: " + e.getMessage(), e);
            throw new RuntimeException("Error fetching data from external API: " + e.getMessage());
        }
    }
}