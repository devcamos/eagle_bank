package com.eaglebank.service;

import com.eaglebank.model.BankAccount;
import com.eaglebank.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.eaglebank.exceptions.NotFoundException;

@Slf4j
@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {
        log.info("Creating new bank account: {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateBankAccount(Long id, BankAccount bankAccount) {
        log.info("Updating bank account with id {}: {}", id, bankAccount);
        Optional<BankAccount> existingOpt = bankAccountRepository.findById(id);
        if (existingOpt.isPresent()) {
            BankAccount existing = existingOpt.get();
            existing.setAccountNumber(bankAccount.getAccountNumber());
            existing.setUser(bankAccount.getUser());
            existing.setType(bankAccount.getType());
            existing.setBalance(bankAccount.getBalance());
            existing.setCurrency(bankAccount.getCurrency());
            existing.setStatus(bankAccount.getStatus());
            return bankAccountRepository.save(existing);
        } else {
            log.warn("Bank account not found with id: {}", id);
            throw new RuntimeException("Bank account not found with id: " + id);
        }
    }

    @Override
    public BankAccount getBankAccountById(Long id) {
        log.info("Fetching bank account with id: {}", id);
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bank account not found with id: " + id));
    }

    @Override
    public Page<BankAccount> getAllBankAccounts(Pageable pageable) {
        log.info("Fetching all bank accounts with pagination");
        return bankAccountRepository.findAll(pageable);
    }

    @Override
    public void deleteBankAccountById(Long id) {
        log.info("Deleting bank account with id: {}", id);
        if (!bankAccountRepository.existsById(id)) {
            log.warn("Bank account not found with id: {}", id);
            throw new NotFoundException("Bank account not found with id: " + id);
        }
        bankAccountRepository.deleteById(id);
        log.info("Bank account deleted with id: {}", id);
    }
} 