package com.example.brd1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class brd1Application {

    public static void main(String[] args) {
        SpringApplication.run(brd1Application.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "Hello from brd1 Spring Boot application!";
    }
}