package com.eaglebank.controller;

import com.eaglebank.service.BankAccountService;
import com.eaglebank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.eaglebank.model.dto.BankAccountRequestDTO;
import com.eaglebank.model.dto.BankAccountResponseDTO;
import com.eaglebank.model.AccountType;
import com.eaglebank.model.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.data.domain.PageImpl;
import com.eaglebank.model.User;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserService userService;

    private BankAccountRequestDTO bankAccountRequestDTO = BankAccountRequestDTO.builder()
            .accountNumber("1234567890")
            .userId(1L)
            .type(AccountType.CHECKING)
            .balance(new BigDecimal("100.00"))
            .currency("USD")
            .status(AccountStatus.ACTIVE)
            .build();
    private BankAccountResponseDTO bankAccountResponseDTO = BankAccountResponseDTO.builder()
            .id(1L)
            .accountNumber("1234567890")
            .userId(1L)
            .type(AccountType.CHECKING)
            .balance(new BigDecimal("100.00"))
            .currency("USD")
            .status(AccountStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    private ObjectMapper objectMapper = new ObjectMapper();

    private User testUser = User.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .phoneNumber("1234567890")
            .address("123 Main St")
            .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
            .build();

    private com.eaglebank.model.BankAccount buildBankAccount() {
        return com.eaglebank.model.BankAccount.builder()
                .id(1L)
                .accountNumber("1234567890")
                .user(testUser)
                .type(AccountType.CHECKING)
                .balance(new BigDecimal("100.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testDeleteBankAccount_Success() throws Exception {
        Mockito.doNothing().when(bankAccountService).deleteBankAccountById(1L);
        mockMvc.perform(delete("/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBankAccount_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Bank account not found with id: 99")).when(bankAccountService).deleteBankAccountById(99L);
        mockMvc.perform(delete("/v1/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBankAccount() throws Exception {
        Mockito.when(bankAccountService.createBankAccount(any())).thenReturn(buildBankAccount());
        mockMvc.perform(post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateBankAccount() throws Exception {
        Mockito.when(bankAccountService.updateBankAccount(eq(1L), any())).thenReturn(buildBankAccount());
        mockMvc.perform(put("/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bankAccountRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBankAccountById() throws Exception {
        Mockito.when(bankAccountService.getBankAccountById(1L)).thenReturn(buildBankAccount());
        mockMvc.perform(get("/v1/accounts/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllBankAccounts() throws Exception {
        Mockito.when(bankAccountService.getAllBankAccounts(any())).thenReturn(new PageImpl<>(List.of(buildBankAccount())));
        mockMvc.perform(get("/v1/accounts"))
                .andExpect(status().isOk());
    }
} 