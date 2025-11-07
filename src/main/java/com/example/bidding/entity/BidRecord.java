package com.example.bidding.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;


@Entity
@Table(name = "bid_record")
@Data
@NoArgsConstructor
public class BidRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String auctionId;
    private String bidderId;
    private long amount;
    private Instant timestamp;
    private String status; // e.g., "ACCEPTED", "REJECTED"
}