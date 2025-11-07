package com.example.bidding.dto;


import lombok.Data;


@Data
public class BidRequest {
    private String bidderId;
    private long amount; // amount in cents / smallest unit
}