package com.example.bidding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BiddingApplication {

    public static void main(String[] args) {
        log.debug("Starting service with args...");
        SpringApplication.run(BiddingApplication.class, args);
        log.debug("Service started successfully!");
    }

}
