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

@Slf4j
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = com.eaglebank.model.dto.UserRequestDTO.class),
                examples = @ExampleObject(name = "UserRequest", value = "{\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"email\": \"john@example.com\",\n  \"phoneNumber\": \"1234567890\",\n  \"address\": \"123 Main St\",\n  \"dateOfBirth\": \"1990-01-01\"\n}")
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User created successfully",
                content = @Content(
                    schema = @Schema(implementation = com.eaglebank.model.dto.UserResponseDTO.class),
                    examples = @ExampleObject(name = "UserResponse", value = "{\n  \"id\": 1,\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"email\": \"john@example.com\",\n  \"phoneNumber\": \"1234567890\",\n  \"address\": \"123 Main St\",\n  \"dateOfBirth\": \"1990-01-01\"\n}")
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.debug("[createUser] Received request body: {}", dto);
        log.debug("Received UserRequestDTO: {}", dto);
        User user = toUser(dto);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(toUserResponseDTO(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        log.debug("[updateUser] Received request body: {}", dto);
        log.debug("Received UserRequestDTO for update: {}", dto);
        User user = toUser(dto);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(toUserResponseDTO(updatedUser));
    }

    @Operation(summary = "Get a user by ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User found",
                content = @Content(
                    schema = @Schema(implementation = com.eaglebank.model.dto.UserResponseDTO.class),
                    examples = @ExampleObject(name = "UserResponse", value = "{\n  \"id\": 1,\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"email\": \"john@example.com\",\n  \"phoneNumber\": \"1234567890\",\n  \"address\": \"123 Main St\",\n  \"dateOfBirth\": \"1990-01-01\"\n}")
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                    schema = @Schema(implementation = java.util.Map.class),
                    examples = @ExampleObject(name = "NotFoundError", value = "{\n  \"error\": \"User not found with id: 99\"\n}")
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(toUserResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> response = users.stream().map(this::toUserResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

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