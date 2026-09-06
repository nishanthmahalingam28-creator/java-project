package com.foodordering.exception;

/**
 * Custom exception thrown when user provides invalid input such as
 * negative quantity, invalid email, invalid menu choice, etc.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}
