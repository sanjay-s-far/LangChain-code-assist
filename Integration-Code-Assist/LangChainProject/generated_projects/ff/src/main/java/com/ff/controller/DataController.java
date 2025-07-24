package com.ff.controller;

import com.ff.model.ExampleData;
import com.ff.service.ApiService;
import com.ff.service.DataTransformationService;
import com.ff.service.FipService;
import com.ff.service.PubSubService;
import com.ff.service.PostgresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    private final PubSubService pubSubService;
    private final PostgresService postgresService;
    private final ApiService apiService;
    private final DataTransformationService dataTransformationService;
    private final FipService fipService;

    @GetMapping("/pubsub/receive")
    public ResponseEntity<String> receivePubSubMessage() {
        // Placeholder - Implement Pub/Sub message receiving logic in PubSubService
        pubSubService.receiveMessage();
        return ResponseEntity.ok("Receiving Pub/Sub message (check logs).");
    }

    @PostMapping("/pubsub/send")
    public ResponseEntity<String> sendPubSubMessage(@RequestBody String message) {
        pubSubService.sendMessage(message);
        return ResponseEntity.ok("Message sent to Pub/Sub.");
    }

    @GetMapping("/postgres/data")
    public ResponseEntity<List<ExampleData>> getDataFromPostgres() {
        return ResponseEntity.ok(postgresService.getData());
    }

    @GetMapping("/external-api")
    public ResponseEntity<String> consumeExternalApi() {
        try {
            String apiResponse = apiService.fetchDataFromExternalApi();
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data: " + e.getMessage());
        }
    }

    @PostMapping("/xml-to-json")
    public ResponseEntity<String> convertXmlToJson(@RequestBody String xmlData) {
        try {
            String jsonData = dataTransformationService.convertXmlToJson(xmlData);
            return ResponseEntity.ok(jsonData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting XML to JSON: " + e.getMessage());
        }
    }

    @GetMapping("/fip/read-xml")
    public ResponseEntity<String> readXmlFromFip() {
        try {
            String xmlData = fipService.readXmlFromFip();
            return ResponseEntity.ok(xmlData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading XML from FIP: " + e.getMessage());
        }
    }
    @PostMapping("/fip/write-xml")
    public ResponseEntity<String> writeXmlToFip(@RequestBody String xmlData) {
        try {
            fipService.writeXmlToFip(xmlData);
            return ResponseEntity.ok("XML written to FIP.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing XML to FIP: " + e.getMessage());
        }
    }

    @GetMapping("/public/hello")
    public String helloPublic() {
        return "Hello, Public!";
    }

    @GetMapping("/secured/hello")
    public String helloSecured() {
        return "Hello, Secured!";
    }
}