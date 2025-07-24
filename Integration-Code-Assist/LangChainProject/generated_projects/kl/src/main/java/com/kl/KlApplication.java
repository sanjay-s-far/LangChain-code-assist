package com.kl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KlApplication {

    public static void main(String[] args) {
        SpringApplication.run(KlApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Hello from kl Application!";
    }
}