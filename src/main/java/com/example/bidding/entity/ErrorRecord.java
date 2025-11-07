package com.example.bidding.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;


@Entity
@Table(name = "error_record")
@Data
@NoArgsConstructor
public class ErrorRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String auctionId;
    private String errorMessage;
    private Instant timestamp;
}