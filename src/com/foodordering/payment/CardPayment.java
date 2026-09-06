package com.foodordering.payment;

import java.util.UUID;

/**
 * Card payment implementation.
 * Demonstrates INTERFACE implementation and POLYMORPHISM.
 */
public class CardPayment implements PaymentMethod {

    @Override
    public String pay(double amount) {
        // Simulated card payment - no real gateway used (academic project)
        return "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getPaymentType() {
        return "Card";
    }
}
