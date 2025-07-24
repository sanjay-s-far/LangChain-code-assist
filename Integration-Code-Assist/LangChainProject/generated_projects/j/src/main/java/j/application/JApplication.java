package j.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enable scheduling for Pub/Sub listener
public class JApplication {

    public static void main(String[] args) {
        SpringApplication.run(JApplication.class, args);
    }

}