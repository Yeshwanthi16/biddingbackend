package com.example.bidding.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;

}
