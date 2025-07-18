package com.eaglebank.service;

import com.eaglebank.model.*;
import com.eaglebank.model.dto.TransactionRequestDTO;
import com.eaglebank.model.dto.TransactionResponseDTO;
import com.eaglebank.repository.BankAccountRepository;
import com.eaglebank.repository.TransactionRepository;
import com.eaglebank.exceptions.NotFoundException;
import com.eaglebank.exceptions.InsufficientFundsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    @Transactional
    public TransactionResponseDTO deposit(Long accountId, TransactionRequestDTO requestDTO) {
        if (requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElse(null);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        account.setBalance(account.getBalance().add(requestDTO.getAmount()));
        bankAccountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .bankAccount(account)
                .amount(requestDTO.getAmount())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .description(requestDTO.getDescription())
                .build();
        transaction = transactionRepository.save(transaction);
        return toResponseDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO withdraw(Long accountId, TransactionRequestDTO requestDTO) {
        BigDecimal amount = requestDTO.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElse(null);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        Transaction transaction;
        if (account.getBalance().compareTo(amount) < 0) {
            // Record failed transaction
            transaction = Transaction.builder()
                    .bankAccount(account)
                    .amount(amount)
                    .type(TransactionType.WITHDRAWAL)
                    .status(TransactionStatus.FAILED)
                    .description("Failed withdrawal: insufficient funds. " + (requestDTO.getDescription() != null ? requestDTO.getDescription() : ""))
                    .build();
            transaction = transactionRepository.save(transaction);
            throw new InsufficientFundsException("Insufficient funds: cannot withdraw " + amount + " from account with balance " + account.getBalance());
        }
        account.setBalance(account.getBalance().subtract(amount));
        bankAccountRepository.save(account);
        transaction = Transaction.builder()
                .bankAccount(account)
                .amount(amount)
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .description(requestDTO.getDescription())
                .build();
        transaction = transactionRepository.save(transaction);
        return toResponseDTO(transaction);
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found with id: " + transactionId));
        return toResponseDTO(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsForAccount(Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElse(null);
        if (account == null) {
            throw new NotFoundException("Bank account not found");
        }
        return transactionRepository.findByBankAccount(account)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO toResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getTimestamp(),
                transaction.getBankAccount().getId()
        );
    }
} 