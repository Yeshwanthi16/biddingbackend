package com.example.bidding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories(basePackages = "com.example.bidding.repository")
public class BiddingApplication {

    public static void main(String[] args) {
        log.info("Starting service with args...");
        SpringApplication.run(BiddingApplication.class, args);
        log.info("Service started successfully!");
    }

}
