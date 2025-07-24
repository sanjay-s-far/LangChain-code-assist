package rl.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rl.application.service.DataIntegrationService;

@RestController
public class DataController {

    @Autowired
    private DataIntegrationService dataIntegrationService;

    @GetMapping("/api/data")
    public ResponseEntity<String> getData() {
        // Call a service method to retrieve and process data
        String result = dataIntegrationService.processData();
        return ResponseEntity.ok(result);
    }
}