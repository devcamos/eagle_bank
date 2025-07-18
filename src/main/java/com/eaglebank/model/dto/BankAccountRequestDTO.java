package com.eaglebank.model.dto;

import com.eaglebank.model.AccountType;
import com.eaglebank.model.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequestDTO {
    @NotBlank
    private String accountNumber;

    @NotNull
    private Long userId;

    @NotNull
    private AccountType type;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal balance;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    private AccountStatus status;
} 