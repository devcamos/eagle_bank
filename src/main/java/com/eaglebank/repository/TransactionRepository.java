package com.eaglebank.repository;

import com.eaglebank.model.Transaction;
import com.eaglebank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBankAccount(BankAccount bankAccount);
} 