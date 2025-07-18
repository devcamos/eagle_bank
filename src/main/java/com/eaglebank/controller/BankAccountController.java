package com.eaglebank.controller;

import com.eaglebank.model.BankAccount;
import com.eaglebank.model.User;
import com.eaglebank.model.dto.BankAccountRequestDTO;
import com.eaglebank.model.dto.BankAccountResponseDTO;
import com.eaglebank.service.BankAccountService;
import com.eaglebank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/v1/accounts")
@Validated
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }

    @Operation(summary = "Delete a bank account", description = "Deletes a bank account by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccountById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a new bank account", description = "Creates a new bank account for a user.")
    @PostMapping
    public ResponseEntity<BankAccountResponseDTO> createBankAccount(@Valid @RequestBody BankAccountRequestDTO dto) {
        BankAccount created = bankAccountService.createBankAccount(toBankAccount(dto));
        return ResponseEntity.status(201).body(toBankAccountResponseDTO(created));
    }

    @Operation(summary = "Update a bank account", description = "Updates an existing bank account by ID.")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponseDTO> updateBankAccount(@PathVariable Long id, @Valid @RequestBody BankAccountRequestDTO dto) {
        BankAccount updated = bankAccountService.updateBankAccount(id, toBankAccount(dto));
        return ResponseEntity.ok(toBankAccountResponseDTO(updated));
    }

    @Operation(summary = "Get a bank account by ID", description = "Fetches a bank account by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponseDTO> getBankAccountById(@PathVariable Long id) {
        BankAccount account = bankAccountService.getBankAccountById(id);
        return ResponseEntity.ok(toBankAccountResponseDTO(account));
    }

    @Operation(summary = "Get all bank accounts", description = "Fetches a list of all bank accounts.")
    @GetMapping
    public ResponseEntity<List<BankAccountResponseDTO>> getAllBankAccounts(Pageable pageable) {
        return ResponseEntity.ok(
            bankAccountService.getAllBankAccounts(pageable)
                .stream()
                .map(this::toBankAccountResponseDTO)
                .collect(Collectors.toList())
        );
    }

    // Mapping methods
    private BankAccount toBankAccount(BankAccountRequestDTO dto) {
        User user = userService.getUserById(dto.getUserId());
        return BankAccount.builder()
                .accountNumber(dto.getAccountNumber())
                .user(user)
                .type(dto.getType())
                .balance(dto.getBalance())
                .currency(dto.getCurrency())
                .status(dto.getStatus())
                .build();
    }

    private BankAccountResponseDTO toBankAccountResponseDTO(BankAccount account) {
        return BankAccountResponseDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(account.getUser().getId())
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
} 