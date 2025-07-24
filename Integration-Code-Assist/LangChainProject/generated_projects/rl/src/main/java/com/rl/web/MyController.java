package com.rl.web;

import com.rl.integration.RestApiService;
import com.rl.integration.pubsub.PubSubPublisherService;
import com.rl.model.MyData;
import com.rl.service.MyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class MyController {

    @Autowired
    private MyDataService myDataService;

    @Autowired
    private PubSubPublisherService pubSubPublisherService;

    @Autowired
    private RestApiService restApiService;

    @GetMapping("/data")
    public List<MyData> getAllData() {
        return myDataService.getAllData();
    }

    @PostMapping("/data")
    public MyData saveData(@RequestBody MyData data) {
        return myDataService.saveData(data);
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String topic, @RequestParam String message) {
        pubSubPublisherService.publishMessage(topic, message);
        return ResponseEntity.ok("Message published to " + topic);
    }

    @GetMapping("/external-api")
    public ResponseEntity<String> fetchDataFromExternalApi() {
        String data = restApiService.fetchDataFromApi();
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.internalServerError().body("Failed to fetch data from external API");
        }
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }
}