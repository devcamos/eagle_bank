package com.eaglebank.service;

import com.eaglebank.model.User;

public interface UserService {
    User saveUser(User user);
    User updateUser(Long id, User user);
} 