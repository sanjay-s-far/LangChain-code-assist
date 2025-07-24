package com.example.newapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.newapplication")
public class NewApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewApplication.class, args);
    }
}