package com.eaglebank.controller;

import com.eaglebank.model.dto.TransactionRequestDTO;
import com.eaglebank.model.dto.TransactionResponseDTO;
import com.eaglebank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Deposit to account", description = "Deposits funds into a bank account.")
    @PostMapping("/accounts/{accountId}/transactions/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequestDTO requestDTO) {
        log.debug("Deposit request: accountId={}, request={}", accountId, requestDTO);
        TransactionResponseDTO response = transactionService.deposit(accountId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Withdraw from account", description = "Withdraws funds from a bank account.")
    @PostMapping("/accounts/{accountId}/transactions/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequestDTO requestDTO) {
        log.debug("Withdraw request: accountId={}, request={}", accountId, requestDTO);
        TransactionResponseDTO response = transactionService.withdraw(accountId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction by ID", description = "Fetches a transaction by its unique ID.")
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long transactionId) {
        log.debug("Get transaction by id: {}", transactionId);
        TransactionResponseDTO response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List transactions for account", description = "Fetches all transactions for a bank account.")
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsForAccount(
            @PathVariable Long accountId) {
        log.debug("List transactions: accountId={}", accountId);
        List<TransactionResponseDTO> response = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(response);
    }
} 