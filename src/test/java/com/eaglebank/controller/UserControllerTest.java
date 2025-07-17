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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc.perform(post("/users")
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
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    // Helper to convert UserResponseDTO to User entity for mocking
    private com.eaglebank.model.User userResponseDTOToUser(UserResponseDTO dto) {
        return com.eaglebank.model.User.builder()
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