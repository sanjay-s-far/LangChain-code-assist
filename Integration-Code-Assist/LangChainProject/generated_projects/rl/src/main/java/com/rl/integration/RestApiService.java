package com.rl.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import java.nio.charset.StandardCharsets;
import org.springframework.util.Base64Utils;

@Service
@Slf4j
public class RestApiService {

    private final RestTemplate restTemplate;

    @Value("${external.api.url:}") //Default value is empty string
    private String externalApiUrl;

    @Value("${external.api.username:}")
    private String externalApiUsername;

    @Value("${external.api.password:}")
    private String externalApiPassword;


    @Autowired
    public RestApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchDataFromApi() {
        log.info("Fetching data from external API: {}", externalApiUrl);

        // Add basic authentication header if username and password are provided
        if (StringUtils.isNotBlank(externalApiUsername) && StringUtils.isNotBlank(externalApiPassword)) {
            String auth = externalApiUsername + ":" + externalApiPassword;
            byte[] encodedAuth = Base64Utils.encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);

            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().set("Authorization", authHeader);
                return execution.execute(request, body);
            });

        }


        try {
            ResponseEntity<String> response = restTemplate.getForEntity(externalApiUrl, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.error("Error fetching data from API. Status code: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Exception while fetching data from API: {}", e.getMessage());
            return null;
        }
    }
}