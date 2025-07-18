package com.eaglebank.model.dto;

import com.eaglebank.model.AccountType;
import com.eaglebank.model.AccountStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponseDTO {
    private Long id;
    private String accountNumber;
    private Long userId;
    private AccountType type;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 