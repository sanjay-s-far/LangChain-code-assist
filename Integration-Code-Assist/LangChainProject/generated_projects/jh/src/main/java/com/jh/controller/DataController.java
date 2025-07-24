package com.jh.controller;

import com.jh.model.SomeData;
import com.jh.repository.SomeDataRepository;
import com.jh.service.PubSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final PubSubService pubSubService;
    private final SomeDataRepository someDataRepository; // Example

    @GetMapping("/hello")
    public String hello() {
        return "Hello, authenticated user!";
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String message, @RequestParam String topic) {
        pubSubService.publishMessage(topic, message);
        return ResponseEntity.ok("Message published to " + topic);
    }

    @GetMapping("/db")
    public List<SomeData> getDataFromDb() {
        return someDataRepository.findAll();
    }

    @PostMapping("/db")
    public ResponseEntity<SomeData> saveDataToDb(@RequestBody SomeData someData) {
        return new ResponseEntity<>(someDataRepository.save(someData), HttpStatus.CREATED);
    }

    //TODO: Add endpoints for other functionalities as per the BRD
}