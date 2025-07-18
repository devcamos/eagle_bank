package com.eaglebank.controller;

import com.eaglebank.model.dto.UserRequestDTO;
import com.eaglebank.model.dto.UserResponseDTO;
import com.eaglebank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.eaglebank.model.User;
import java.util.List;
import com.eaglebank.exceptions.NotFoundException;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.saveUser(any())).thenReturn(userResponseDTOToUser(userResponseDTO));
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserRequestDTO updatedRequest = UserRequestDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .phoneNumber("0987654321")
                .address("456 Elm St")
                .dateOfBirth(LocalDate.of(1992, 2, 2))
                .build();
        UserResponseDTO updatedResponse = UserResponseDTO.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .phoneNumber("0987654321")
                .address("456 Elm St")
                .dateOfBirth(LocalDate.of(1992, 2, 2))
                .build();
        Mockito.when(userService.updateUser(eq(1L), any())).thenReturn(userResponseDTOToUser(updatedResponse));
        mockMvc.perform(put("/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void testCreateUser_MissingRequiredFields_Returns400() throws Exception {
        UserRequestDTO invalidRequest = UserRequestDTO.builder()
                .lastName("Doe")
                // firstName, email, dateOfBirth are missing
                .build();

        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").value("must not be blank"))
                .andExpect(jsonPath("$.email").value("must not be blank"))
                .andExpect(jsonPath("$.dateOfBirth").value("must not be null"));
    }

    @Test
    void testGetUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(userResponseDTOToUser(userResponseDTO));
        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = List.of(userResponseDTOToUser(userResponseDTO));
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(1L);
        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.doThrow(new NotFoundException("User not found with id: 99")).when(userService).deleteUserById(99L);
        mockMvc.perform(delete("/v1/users/99"))
                .andExpect(status().isNotFound());
    }

    // Helper to convert UserResponseDTO to User entity for mocking
    private User userResponseDTOToUser(UserResponseDTO dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }
} 