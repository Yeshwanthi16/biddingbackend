package com.example.bidding.controller;

import com.example.bidding.Security.UserDetailsImpl;
import com.example.bidding.model.dto.ChangePassword;
import com.example.bidding.entity.user.User;
import com.example.bidding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    final UserService userService;

    @GetMapping()
    @Operation(summary = "Get all users (only for admin)")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            ChangePassword changePassword) {
        userService.changePassword(userDetails.getUsername(), changePassword.getOldPassword(),
                changePassword.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");

    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by id")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
