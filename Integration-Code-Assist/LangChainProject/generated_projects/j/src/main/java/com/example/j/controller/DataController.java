package com.example.j.controller;

import com.example.j.model.DataModel;
import com.example.j.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping
    public ResponseEntity<List<DataModel>> getAllData() {
        List<DataModel> data = dataService.getAllData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // Example endpoint - more to be added based on requirements
    @PostMapping
    public ResponseEntity<DataModel> createData(@RequestBody DataModel dataModel) {
        DataModel createdData = dataService.createData(dataModel);
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }
}