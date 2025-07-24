package com.example.newapplication.controller;

import com.example.newapplication.integration.PostgresIntegration;
import com.example.newapplication.service.ApiService;
import com.example.newapplication.service.DataTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @Autowired
    private PostgresIntegration postgresIntegration;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DataTransformationService dataTransformationService;

    @GetMapping("/data")
    public ResponseEntity<String> getDataFromPostgres() {
        return ResponseEntity.ok(postgresIntegration.readData());
    }

    @GetMapping("/external-api-data")
    public ResponseEntity<String> getExternalApiData() {
        return ResponseEntity.ok(apiService.fetchDataFromExternalApi());
    }

    @PostMapping("/xml-to-json")
    public ResponseEntity<String> convertXmlToJson(@RequestBody String xmlData) {
        return ResponseEntity.ok(dataTransformationService.convertXmlToJson(xmlData));
    }


    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}