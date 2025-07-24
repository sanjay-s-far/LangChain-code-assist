package com.brd1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.brd1"})
public class brd1Application {

    public static void main(String[] args) {
        SpringApplication.run(brd1Application.class, args);
    }
}