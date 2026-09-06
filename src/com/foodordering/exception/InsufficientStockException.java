package com.foodordering.exception;

/**
 * Custom exception thrown when a customer tries to order more quantity
 * than the available stock of a food item.
 */
public class InsufficientStockException extends Exception {

    public InsufficientStockException(String message) {
        super(message);
    }
}
