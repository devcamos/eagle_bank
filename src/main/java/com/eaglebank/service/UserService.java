package com.eaglebank.service;

import com.eaglebank.model.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    User updateUser(Long id, User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUserById(Long id);
} 