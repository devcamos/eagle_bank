package com.eaglebank.controller;

import com.eaglebank.model.User;
import com.eaglebank.model.dto.UserRequestDTO;
import com.eaglebank.model.dto.UserResponseDTO;
import com.eaglebank.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details.")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.debug("Received UserRequestDTO: {}", dto);
        User user = toUser(dto);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(toUserResponseDTO(savedUser));
    }

    @Operation(summary = "Update a user", description = "Updates an existing user by ID with the provided details.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        log.debug("Received UserRequestDTO for update: {}", dto);
        User user = toUser(dto);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(toUserResponseDTO(updatedUser));
    }

    @Operation(summary = "Get a user by ID", description = "Fetches a user by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(toUserResponseDTO(user));
    }

    @Operation(summary = "Get all users", description = "Fetches a list of all users.")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> response = users.stream().map(this::toUserResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by their unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
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