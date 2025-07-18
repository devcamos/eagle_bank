package com.eaglebank.service;

import com.eaglebank.model.User;
import com.eaglebank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.eaglebank.exceptions.NotFoundException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found with id: {}", id);
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }
} 