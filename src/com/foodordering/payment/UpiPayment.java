package com.foodordering.payment;

import java.util.UUID;

/**
 * UPI payment implementation.
 * Demonstrates INTERFACE implementation and POLYMORPHISM.
 */
public class UpiPayment implements PaymentMethod {

    @Override
    public String pay(double amount) {
        // Simulated UPI payment - no real gateway used (academic project)
        return "UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getPaymentType() {
        return "UPI";
    }
}
