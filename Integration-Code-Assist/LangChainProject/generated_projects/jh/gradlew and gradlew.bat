package com.jh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.jh")
public class JhApplication {

    public static void main(String[] args) {
        SpringApplication.run(JhApplication.class, args);
    }

}