package com.theproject.theproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TheprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheprojectApplication.class, args);
    }

    @RestController
    class HelloController {
        @GetMapping("/")
        String hello() {
            return "Welcome to the Online Bookstore Platform!";
        }
    }
}