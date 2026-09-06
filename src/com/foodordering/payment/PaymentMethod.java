package com.foodordering.payment;

/**
 * Interface for all payment methods.
 * Demonstrates the OOP concept of INTERFACES and is used to demonstrate
 * runtime POLYMORPHISM (a PaymentMethod reference can hold UpiPayment,
 * CardPayment, or CashPayment objects).
 */
public interface PaymentMethod {

    /**
     * Processes the payment for the given amount.
     * @param amount the amount to pay
     * @return a unique payment ID
     */
    String pay(double amount);

    /** Returns the human-readable name of this payment type. */
    String getPaymentType();
}
