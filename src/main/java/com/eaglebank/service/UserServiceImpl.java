package com.eaglebank.service;

import com.eaglebank.model.User;
import com.eaglebank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        log.info("Saving new user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Updating user with id {}: {}", id, user);
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            User updated = userRepository.save(existingUser);
            log.info("Updated user: {}", updated);
            return updated;
        } else {
            log.warn("User not found with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
    }
} 