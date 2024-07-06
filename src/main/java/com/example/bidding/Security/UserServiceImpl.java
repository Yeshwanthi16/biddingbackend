package com.example.bidding.Security;

import com.example.bidding.entity.user.User;
import com.example.bidding.exceptions.NotFoundException;
import com.example.bidding.exceptions.PasswordNotMatchException;
import com.example.bidding.repository.UserRepository;

import com.example.bidding.service.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    final PasswordEncoder encoder;

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

}
