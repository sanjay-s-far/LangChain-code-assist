package com.newapplication.controller;

import com.newapplication.fip.FipService;
import com.newapplication.pubsub.PubSubService;
import com.newapplication.repository.DataRepository;
import com.newapplication.service.DataTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DataIntegrationController {

    @Autowired
    private PubSubService pubSubService;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataTransformationService dataTransformationService;

    @Autowired
    private FipService fipService;

    // --- Pub/Sub ---
    @PostMapping("/pubsub/publish/{topic}")
    public ResponseEntity<String> publishMessage(@PathVariable String topic, @RequestBody String message) {
        pubSubService.publishMessage(topic, message);
        return ResponseEntity.ok("Message published to " + topic);
    }

    // --- PostgreSQL ---
    @GetMapping("/data/{id}")
    public ResponseEntity<String> getData(@PathVariable Long id) {
        // Example:  Retrieve data from PostgreSQL and return as JSON.
        return ResponseEntity.ok(dataRepository.findById(id).toString()); // Replace with actual JSON conversion
    }

    // --- External API ---
    @GetMapping("/external-api")
    public ResponseEntity<String> consumeExternalApi() {
        // TODO: Implement API call and error handling
        return ResponseEntity.ok("External API Response (Placeholder)");
    }

    // --- FIP ---
    @GetMapping("/fip/read")
    public ResponseEntity<String> readFipFile() {
        // TODO: Implement FIP file reading
        try {
            String content = fipService.readXmlFile("your_file.xml");  //Replace with actual file name
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error reading FIP file: " + e.getMessage());
        }
    }

    @PostMapping("/fip/write")
    public ResponseEntity<String> writeFipFile(@RequestBody String data) {
        // TODO: Implement FIP file writing
        try {
            fipService.writeXmlFile("your_file.xml", data); //Replace with actual file name
            return ResponseEntity.ok("FIP file written successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error writing FIP file: " + e.getMessage());
        }
    }

    // --- Data Transformation ---
    @PostMapping("/transform/xml-to-json")
    public ResponseEntity<String> xmlToJson(@RequestBody String xmlData) {
        try {
            String jsonData = dataTransformationService.xmlToJson(xmlData);
            return ResponseEntity.ok(jsonData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error transforming XML to JSON: " + e.getMessage());
        }
    }

    @PostMapping("/transform/xml-to-pdf")
    public ResponseEntity<byte[]> xmlToPdf(@RequestBody String xmlData) {
        try {
            byte[] pdfData = dataTransformationService.xmlToPdf(xmlData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .body(pdfData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // Consider a more informative error response
        }
    }
}