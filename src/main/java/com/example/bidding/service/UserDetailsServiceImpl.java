package com.example.bidding.service;

import com.example.bidding.Security.UserDetailsImpl;
import com.example.bidding.entity.user.User;
import com.example.bidding.exceptions.NotFoundException;
import com.example.bidding.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

     private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        "User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
