package com.example.newapplication.controller;

import com.example.newapplication.service.FipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FipController {

    @Autowired
    private FipService fipService;

    @GetMapping("/read-fip-xml")
    public ResponseEntity<String> readFipXml() {
        try {
            return ResponseEntity.ok(fipService.readXmlFileFromFip());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error reading FIP XML: " + e.getMessage());
        }
    }

    @PostMapping("/write-fip-xml")
    public ResponseEntity<String> writeFipXml(@RequestBody String xmlData) {
        try {
            fipService.writeXmlFileToFip(xmlData);
            return ResponseEntity.ok("XML written to FIP successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error writing to FIP: " + e.getMessage());
        }
    }
}