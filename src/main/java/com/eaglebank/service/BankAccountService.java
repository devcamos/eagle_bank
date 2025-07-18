package com.eaglebank.service;

import com.eaglebank.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccount bankAccount);
    BankAccount updateBankAccount(Long id, BankAccount bankAccount);
    BankAccount getBankAccountById(Long id);
    Page<BankAccount> getAllBankAccounts(Pageable pageable);
    void deleteBankAccountById(Long id);
} 