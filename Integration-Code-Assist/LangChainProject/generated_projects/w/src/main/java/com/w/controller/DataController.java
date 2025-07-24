package com.w.controller;

import com.w.model.SomeData;
import com.w.service.DataIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataIntegrationService dataIntegrationService;

    @Autowired
    public DataController(DataIntegrationService dataIntegrationService) {
        this.dataIntegrationService = dataIntegrationService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/postgres")
    public ResponseEntity<String> getPostgresData() {
        return ResponseEntity.ok(dataIntegrationService.readDataFromPostgres());
    }

    @GetMapping("/external-api")
    public ResponseEntity<String> getExternalApiData() {
        return ResponseEntity.ok(dataIntegrationService.fetchDataFromExternalApi());
    }

    @PostMapping("/transform")
    public ResponseEntity<String> transformData(@RequestBody SomeData data) {
        return ResponseEntity.ok(dataIntegrationService.transformDataToJson(data));
    }

    @GetMapping("/fip")
    public ResponseEntity<String> readFipFile() {
        return ResponseEntity.ok(dataIntegrationService.readXmlFromFip());
    }

}