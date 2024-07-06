package com.example.bidding.controller;

import com.example.bidding.Security.JwtUtils;
import com.example.bidding.Security.RefreshTokenService;
import com.example.bidding.Security.UserDetailsImpl;
import com.example.bidding.entity.RefreshToken;
import com.example.bidding.entity.user.RoleEnum;
import com.example.bidding.entity.user.User;
import com.example.bidding.exceptions.DuplicatedEmailException;
import com.example.bidding.exceptions.DuplicatedLoginException;
import com.example.bidding.exceptions.NotFoundException;
import com.example.bidding.model.dto.*;
import com.example.bidding.service.UserDetailsServiceImpl;
import com.example.bidding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthenticationManager authenticationManager;

    final RefreshTokenService refreshTokenService;

    final UserDetailsServiceImpl userDetailsService;

    final UserService userService;

    final PasswordEncoder encoder;

    final JwtUtils jwtUtils;

    @PostMapping("/signin")
    @Operation(summary = "Sign in to the application")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken()));
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "Refresh token")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new NotFoundException("Refresh token is not in database!"));

    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up to the application")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            throw new DuplicatedLoginException("Error: Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicatedEmailException("Error: Email is already in use!");
        }

        User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getUsername()
        );

        ApiResponse apiResponse = new ApiResponse("User Created successfully");

        user.setRoleName(RoleEnum.ROLE_USER);
        userService.createUser(user);

        return  ResponseEntity.
                status(HttpStatus.CREATED).
                body(apiResponse);
    }
}
