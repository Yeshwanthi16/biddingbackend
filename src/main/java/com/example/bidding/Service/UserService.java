package com.example.bidding.Service;

import com.example.bidding.Entity.User;
import com.example.bidding.Security.JwtUtils;
import com.example.bidding.exceptions.NotFoundException;
import com.example.bidding.model.dto.ApiResponse;
import com.example.bidding.model.dto.LoginRequest;
import com.example.bidding.model.dto.SignUpRequest;
import com.example.bidding.model.dto.TokenRefreshRequest;
import com.example.bidding.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private JwtUtils jwtUtils;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public ApiResponse registerUser(SignUpRequest signUpRequest) {

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent())
            return new ApiResponse ("Account exists with this email already",HttpStatus.BAD_REQUEST);

        String encryptedPassword = new BCryptPasswordEncoder().encode(signUpRequest.getPassword());

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(encryptedPassword)
                .username(signUpRequest.getUsername())
                .userId(UUID.randomUUID().getLeastSignificantBits())
                .build();

        userRepository.save(user);

        return new ApiResponse("User created successfully",HttpStatus.OK);
    }

    public ApiResponse login(LoginRequest loginRequest) {

            Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

            if (optionalUser.isEmpty())
                throw new NotFoundException("Invalid email or password");

            User user = optionalUser.get();

            if (!new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword()))
                throw new NotFoundException("Invalid email or password");

            return new ApiResponse(jwtUtils.generateToken(user.getEmail()),HttpStatus.OK);
        }

    public ApiResponse refreshToken(TokenRefreshRequest tokenRefreshRequest)
    {
//        if(jwtUtils.isTokenExpired(tokenRefreshRequest.getRefreshToken()))
//            return new ApiResponse("Token is expired",HttpStatus.OK);

        if(jwtUtils.extractExpiration(tokenRefreshRequest.getRefreshToken()).compareTo(Date.from(Instant.now()))>0)
            return new ApiResponse(jwtUtils.generateToken(tokenRefreshRequest.getEmail()),HttpStatus.CREATED);

        return new ApiResponse("Token is not expired",HttpStatus.NOT_MODIFIED);
    }
}

