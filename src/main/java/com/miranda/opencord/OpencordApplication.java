package com.miranda.opencord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class OpencordApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpencordApplication.class, args);
    }

}
