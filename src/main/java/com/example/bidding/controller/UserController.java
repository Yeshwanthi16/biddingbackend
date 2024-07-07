package com.example.bidding.controller;

import com.example.bidding.Entity.User;
import com.example.bidding.Service.UserService;
import com.example.bidding.model.dto.ApiResponse;
import com.example.bidding.model.dto.LoginRequest;
import com.example.bidding.model.dto.SignUpRequest;
import com.example.bidding.model.dto.TokenRefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    @Operation(summary = "Register User")
    public ApiResponse registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "User to Login")
    public ApiResponse login(@Valid @RequestBody LoginRequest loginRequest)
    {
        return userService.login(loginRequest);
    }

    @PostMapping("/refreshtoken")
    @Operation(summary = "Refresh token")
//    @SecurityRequirement(name = "Bearer Authentication")
    public ApiResponse refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest)
    {
       return userService.refreshToken(tokenRefreshRequest);
    }

}
