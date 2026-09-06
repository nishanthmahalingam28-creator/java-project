package com.foodordering.util;

import com.foodordering.exception.InvalidInputException;

import java.util.regex.Pattern;
import java.util.Scanner;

/**
 * Utility class for validating all user inputs.
 * Demonstrates ENCAPSULATION of validation logic and EXCEPTION HANDLING.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z ]{2,50}$");

    /**
     * Validates a person's name.
     */
    public static void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new InvalidInputException(
                    "Invalid name. Use only letters and spaces (2-50 characters).");
        }
    }

    /**
     * Validates an email address.
     */
    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidInputException("Invalid email format. Example: user@example.com");
        }
    }

    /**
     * Validates a 10-digit phone number.
     */
    public static void validatePhone(String phone) throws InvalidInputException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidInputException("Phone number cannot be empty.");
        }
        if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new InvalidInputException("Invalid phone number. Enter a 10-digit number.");
        }
    }

    /**
     * Validates a password (minimum 5 characters).
     */
    public static void validatePassword(String password) throws InvalidInputException {
        if (password == null || password.length() < 5) {
            throw new InvalidInputException(
                    "Password must be at least 5 characters long.");
        }
    }

    /**
     * Validates that a price is positive.
     */
    public static void validatePrice(double price) throws InvalidInputException {
        if (price <= 0) {
            throw new InvalidInputException("Price must be greater than zero.");
        }
    }

    /**
     * Validates that a quantity is positive.
     */
    public static void validateQuantity(int quantity) throws InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
    }

    /**
     * Validates a menu choice is within the given range.
     */
    public static void validateMenuChoice(int choice, int min, int max) throws InvalidInputException {
        if (choice < min || choice > max) {
            throw new InvalidInputException(
                    "Invalid choice. Please enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Safely reads an integer from the Scanner, consuming the newline.
     * Returns the default value if input is not a valid integer.
     * This avoids the common Scanner nextInt/nextLine problem.
     */
    public static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Safely reads a double from the Scanner.
     */
    public static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    /**
     * Reads a non-empty string from the Scanner.
     */
    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input;
    }
}
