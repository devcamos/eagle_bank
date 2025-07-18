package com.eaglebank.service;

import com.eaglebank.model.dto.TransactionRequestDTO;
import com.eaglebank.model.dto.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {
    TransactionResponseDTO deposit(Long accountId, TransactionRequestDTO requestDTO);
    TransactionResponseDTO withdraw(Long accountId, TransactionRequestDTO requestDTO);
    TransactionResponseDTO getTransactionById(Long transactionId);
    List<TransactionResponseDTO> getTransactionsForAccount(Long accountId);
} 