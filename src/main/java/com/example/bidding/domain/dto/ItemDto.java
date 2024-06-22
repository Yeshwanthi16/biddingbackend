package com.example.bidding.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private String name;
    private Float price;
    private String description;
    private Boolean availability;
    private LocalDateTime startBiddingTime;
    private LocalDateTime endBiddingTime;
}
