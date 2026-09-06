package com.foodordering.payment;

import java.util.UUID;

/**
 * Cash payment implementation.
 * Demonstrates INTERFACE implementation and POLYMORPHISM.
 */
public class CashPayment implements PaymentMethod {

    @Override
    public String pay(double amount) {
        // Simulated cash payment - no real gateway used (academic project)
        return "CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getPaymentType() {
        return "Cash";
    }
}
