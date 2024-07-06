package com.example.bidding.service.Interfaces;



import com.example.bidding.entity.user.User;

import java.util.List;

public interface UserService {

    void deleteUserById(Long id);

    void changePassword(Long userId, String oldPassword, String newPassword);

    boolean existsByUsername(String username);

    List<User> getAllUsers();

    boolean existsByEmail(String email);

    User createUser(User user);
}
