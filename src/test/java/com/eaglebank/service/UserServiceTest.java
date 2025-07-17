package com.eaglebank.service;

import com.eaglebank.model.User;
import com.eaglebank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(user)).thenReturn(user);
        User saved = userService.saveUser(user);
        assertEquals(user, saved);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updated = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .phoneNumber("0987654321")
                .address("456 Elm St")
                .dateOfBirth(LocalDate.of(1992, 2, 2))
                .build();
        User result = userService.updateUser(1L, updated);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhoneNumber());
        assertEquals("456 Elm St", result.getAddress());
        assertEquals(LocalDate.of(1992, 2, 2), result.getDateOfBirth());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        User updated = User.builder().build();
        Exception exception = assertThrows(RuntimeException.class, () -> userService.updateUser(2L, updated));
        assertTrue(exception.getMessage().contains("User not found with id: 2"));
    }
} 