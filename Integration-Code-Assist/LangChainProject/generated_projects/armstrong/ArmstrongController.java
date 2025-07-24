package com.example.armstrong.controller;

import com.example.armstrong.service.ArmstrongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/armstrong")
public class ArmstrongController {

    private static final Logger logger = LoggerFactory.getLogger(ArmstrongController.class);
    private final ArmstrongService armstrongService;

    public ArmstrongController(ArmstrongService armstrongService) {
        this.armstrongService = armstrongService;
    }

    @GetMapping("/validate/{number}")
    public ResponseEntity<String> validateArmstrong(@PathVariable int number) {
        logger.info("Received request to validate number: {}", number);
        if (number < 0) {
            logger.warn("Invalid input: Negative number received.");
            return ResponseEntity.badRequest().body("Invalid input: Number must be non-negative.");
        }
        boolean isArmstrong = armstrongService.isArmstrongNumber(number);
        String equation = armstrongService.getArmstrongEquation(number);
        String response = "Number " + number + " is " + (isArmstrong ? "" : "not ") + "an Armstrong number.\nEquation: " + equation;
        logger.info("Validation result: {}", response);
        return ResponseEntity.ok(response);
    }
}