package com.ecommerce.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data


@NoArgsConstructor
public class TotalPriceByPayment {
    private String payMethod;
    private Double amount;

    public TotalPriceByPayment(String payMethod, Double amount) {
        this.payMethod = payMethod;
        this.amount = amount;
    }
}
