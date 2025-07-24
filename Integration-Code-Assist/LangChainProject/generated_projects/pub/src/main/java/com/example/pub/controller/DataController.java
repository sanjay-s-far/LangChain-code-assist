package com.example.pub.controller;

import com.example.pub.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok(dataService.getData());
    }

    @PostMapping("/process")
    public ResponseEntity<String> processData(@RequestBody String data) {
        dataService.processData(data);
        return ResponseEntity.ok("Data processed");
    }
}