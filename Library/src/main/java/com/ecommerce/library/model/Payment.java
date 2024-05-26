package com.ecommerce.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // e.g., "success", "failed", "pending"
    private Double amount;

    // Constructors, getters, and setters

    public Payment() {}

    public Payment(String status, Double amount) {
        this.status = status;
        this.amount = amount;
    }

}
