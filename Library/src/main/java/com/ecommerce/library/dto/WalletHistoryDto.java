package com.ecommerce.library.dto;

import com.ecommerce.library.enums.WalletTransactionType;
import com.ecommerce.library.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletHistoryDto {

    private Long id;

    private double amount;

    private WalletTransactionType type;

    private String transactionStatus;

    private Wallet wallet;
}
