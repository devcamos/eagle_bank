package com.eaglebank.controller;

import com.eaglebank.model.User;
import com.eaglebank.model.dto.UserRequestDTO;
import com.eaglebank.model.dto.UserResponseDTO;
import com.eaglebank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user = toUser(userRequestDTO);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(toUserResponseDTO(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        User user = toUser(userRequestDTO);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(toUserResponseDTO(updatedUser));
    }

    private User toUser(UserRequestDTO dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
} 