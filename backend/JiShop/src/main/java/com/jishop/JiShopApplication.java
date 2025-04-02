package com.jishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JiShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiShopApplication.class, args);
    }
}


