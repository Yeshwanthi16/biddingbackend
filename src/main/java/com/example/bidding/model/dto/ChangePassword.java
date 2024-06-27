package com.example.bidding.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}