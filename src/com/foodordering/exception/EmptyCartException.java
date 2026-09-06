package com.foodordering.exception;

/**
 * Custom exception thrown when a customer tries to place an order
 * or make a payment with an empty cart.
 */
public class EmptyCartException extends Exception {

    public EmptyCartException(String message) {
        super(message);
    }
}
