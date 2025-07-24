package com.example.newapplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${external.api.username}")
    private String apiUsername;

    @Value("${external.api.password}")
    private String apiPassword;

    public String fetchDataFromExternalApi() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // Basic Authentication Header (if required)
        headers.setBasicAuth(apiUsername, apiPassword);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(externalApiUrl, HttpMethod.GET, entity, String.class);
            logger.info("External API Response: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error fetching data from external API: ", e);
            return "Error: " + e.getMessage();
        }
    }
}