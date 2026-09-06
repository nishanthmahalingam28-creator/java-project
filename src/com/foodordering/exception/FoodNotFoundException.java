package com.foodordering.exception;

/**
 * Custom exception thrown when a food item is not found in the menu
 * by ID or by name.
 */
public class FoodNotFoundException extends Exception {

    public FoodNotFoundException(String message) {
        super(message);
    }
}
