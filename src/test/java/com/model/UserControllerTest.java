package com.model;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private User user;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "john@example.com", "1234567890", "123 Main St", LocalDate.of(1990, 1, 1));
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.saveUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User updated = new User(1L, "Jane", "Smith", "jane@example.com", "0987654321", "456 Elm St", LocalDate.of(1992, 2, 2));
        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updated);
        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
} 