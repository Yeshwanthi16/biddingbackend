package com.example.bidding.service;

import com.example.bidding.exceptions.NotFoundException;
import com.example.bidding.exceptions.PasswordNotMatchException;
import com.example.bidding.entity.user.User;
import com.example.bidding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;

    final PasswordEncoder encoder;

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new PasswordNotMatchException("Old password is incorrect");
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
