package com.eaglebank.controller;

import com.eaglebank.model.dto.TransactionRequestDTO;
import com.eaglebank.model.dto.TransactionResponseDTO;
import com.eaglebank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.eaglebank.exceptions.InsufficientFundsException;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private String readJson(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())));
    }

    @Test
    void testDeposit_Success() throws Exception {
        TransactionRequestDTO request = objectMapper.readValue(readJson("/payloads/transaction-deposit-request.json"), TransactionRequestDTO.class);
        TransactionResponseDTO response = objectMapper.readValue(readJson("/payloads/transaction-response.json"), TransactionResponseDTO.class);
        Mockito.when(transactionService.deposit(eq(1L), any())).thenReturn(response);
        mockMvc.perform(post("/v1/accounts/1/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testWithdraw_Success() throws Exception {
        TransactionRequestDTO request = objectMapper.readValue(readJson("/payloads/transaction-withdraw-request.json"), TransactionRequestDTO.class);
        TransactionResponseDTO response = objectMapper.readValue(readJson("/payloads/transaction-response.json"), TransactionResponseDTO.class);
        Mockito.when(transactionService.withdraw(eq(1L), any())).thenReturn(response);
        mockMvc.perform(post("/v1/accounts/1/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testWithdraw_InsufficientFunds() throws Exception {
        TransactionRequestDTO request = objectMapper.readValue(readJson("/payloads/transaction-withdraw-request.json"), TransactionRequestDTO.class);
        TransactionResponseDTO failedResponse = objectMapper.readValue(readJson("/payloads/transaction-failed-response.json"), TransactionResponseDTO.class);
        Mockito.when(transactionService.withdraw(eq(1L), any())).thenThrow(new InsufficientFundsException("Insufficient funds"));
        mockMvc.perform(post("/v1/accounts/1/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTransactionById_Success() throws Exception {
        TransactionResponseDTO response = objectMapper.readValue(readJson("/payloads/transaction-response.json"), TransactionResponseDTO.class);
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(response);
        mockMvc.perform(get("/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testListTransactionsForAccount() throws Exception {
        TransactionResponseDTO response = objectMapper.readValue(readJson("/payloads/transaction-response.json"), TransactionResponseDTO.class);
        Mockito.when(transactionService.getTransactionsForAccount(1L)).thenReturn(List.of(response));
        mockMvc.perform(get("/v1/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
} 