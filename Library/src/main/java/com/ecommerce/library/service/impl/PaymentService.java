package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Payment;
import com.ecommerce.library.repository.PaymentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Payment processPayment(Payment payment) {
        try {
            // Simulate payment processing logic
            if (simulatePaymentProcessing(payment)) {
                payment.setStatus("success");
            } else {
                payment.setStatus("failed");
            }
            return paymentRepository.save(payment);
        } catch (Exception e) {
            // Update status to 'pending' in case of error
            payment.setStatus("pending");
            return paymentRepository.save(payment);
        }
    }

    private boolean simulatePaymentProcessing(Payment payment) {
        // Simulate some payment processing logic that could fail
        return Math.random() > 0.5; // 50% chance of success
    }

    public void updatePayment(Payment payment) {
        // Your logic to update the payment status in the database
        // Merge the payment object to update its status in the database
        entityManager.merge(payment);
    }
}

