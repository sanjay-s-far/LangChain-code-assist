package j.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class ApiService {

    public String fetchDataFromExternalApi() {
        // TODO: Implement API call, authentication, error handling, and data parsing.
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://example.com/api/data"; // Replace with your API URL
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data from external API. Status code: " + response.getStatusCode());
        }
    }
}