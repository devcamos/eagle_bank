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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.eaglebank.model.dto.BankAccountRequestDTO;
import com.eaglebank.model.dto.BankAccountResponseDTO;
import com.eaglebank.model.AccountType;
import com.eaglebank.model.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.eaglebank.model.User;
import com.eaglebank.exceptions.NotFoundException;
import org.springframework.data.domain.PageImpl;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private BankAccountRequestDTO bankAccountRequestDTO;
    private BankAccountResponseDTO bankAccountResponseDTO;

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
        Mockito.doThrow(new NotFoundException("Bank account not found with id: 99")).when(bankAccountService).deleteBankAccountById(99L);
        mockMvc.perform(delete("/v1/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBankAccount() throws Exception {
        BankAccountRequestDTO requestFromFile = objectMapper.readValue(
            getClass().getResourceAsStream("/payloads/bank-account-request.json"),
            BankAccountRequestDTO.class
        );
        Mockito.when(userService.getUserById(1L)).thenReturn(testUser);
        Mockito.when(bankAccountService.createBankAccount(any())).thenReturn(buildBankAccount());
        mockMvc.perform(post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestFromFile)))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateBankAccount() throws Exception {
        BankAccountRequestDTO updatedRequest = objectMapper.readValue(
            getClass().getResourceAsStream("/payloads/bank-account-request.json"),
            BankAccountRequestDTO.class
        );
        Mockito.when(userService.getUserById(1L)).thenReturn(testUser);
        Mockito.when(bankAccountService.updateBankAccount(eq(1L), any())).thenReturn(buildBankAccount());
        mockMvc.perform(put("/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBankAccountById() throws Exception {
        BankAccountResponseDTO responseFromFile = objectMapper.readValue(
            getClass().getResourceAsStream("/payloads/bank-account-response.json"),
            BankAccountResponseDTO.class
        );
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

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        bankAccountRequestDTO = new BankAccountRequestDTO();
        bankAccountRequestDTO.setAccountNumber("1234567890");
        bankAccountRequestDTO.setUserId(1L);
        bankAccountRequestDTO.setType(AccountType.CHECKING);
        bankAccountRequestDTO.setBalance(new BigDecimal("100.00"));
        bankAccountRequestDTO.setCurrency("USD");
        bankAccountRequestDTO.setStatus(AccountStatus.ACTIVE);

        bankAccountResponseDTO = new BankAccountResponseDTO();
        bankAccountResponseDTO.setId(1L);
        bankAccountResponseDTO.setAccountNumber("1234567890");
        bankAccountResponseDTO.setUserId(1L);
        bankAccountResponseDTO.setType(AccountType.CHECKING);
        bankAccountResponseDTO.setBalance(new BigDecimal("100.00"));
        bankAccountResponseDTO.setCurrency("USD");
        bankAccountResponseDTO.setStatus(AccountStatus.ACTIVE);
        bankAccountResponseDTO.setCreatedAt(LocalDateTime.now());
        bankAccountResponseDTO.setUpdatedAt(LocalDateTime.now());
    }
} 