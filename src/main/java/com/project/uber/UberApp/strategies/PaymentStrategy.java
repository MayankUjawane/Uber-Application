package com.project.uber.UberApp.strategies;

import com.project.uber.UberApp.entities.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
