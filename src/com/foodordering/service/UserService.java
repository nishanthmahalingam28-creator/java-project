package com.foodordering.service;

import com.foodordering.model.Admin;
import com.foodordering.model.Customer;
import com.foodordering.model.User;
import com.foodordering.util.FileManager;
import com.foodordering.exception.InvalidInputException;
import com.foodordering.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Service class for user registration, login, and management.
 * Demonstrates COLLECTIONS (ArrayList, HashMap), GENERICS, and STREAM API.
 */
public class UserService {

    private List<Customer> customers;
    private List<Admin> admins;
    private int nextCustomerId;

    // Map for fast user lookup by email — demonstrates HashMap usage
    private Map<String, User> userMap;

    public UserService() {
        this.customers = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.userMap = new HashMap<>();
        this.nextCustomerId = 1001;
        loadSampleAdmin();
    }

    /**
     * Creates a sample admin for testing (academic demo credential).
     */
    private void loadSampleAdmin() {
        Admin admin = new Admin(1, "System Admin", "admin@foodordering.com",
                "9999999999", "Admin@123");
        admins.add(admin);
        userMap.put(admin.getEmail().toLowerCase(), admin);
    }

    /**
     * Loads customers from file. If no file exists, no customers are loaded.
     */
    public void loadCustomersFromFile() {
        List<Customer> loaded = FileManager.loadCustomers();
        if (loaded != null && !loaded.isEmpty()) {
            customers = loaded;
            for (Customer c : customers) {
                userMap.put(c.getEmail().toLowerCase(), c);
                if (c.getUserId() >= nextCustomerId) {
                    nextCustomerId = c.getUserId() + 1;
                }
            }
        }
    }

    /**
     * Saves customers to file.
     */
    public void saveCustomersToFile() {
        FileManager.saveCustomers(customers);
    }

    /**
     * Registers a new customer after validating all inputs.
     * Demonstrates EXCEPTION HANDLING and input validation.
     */
    public Customer registerCustomer(Scanner scanner) {
        try {
            System.out.println("\n========================================");
            System.out.println("       CUSTOMER REGISTRATION");
            System.out.println("========================================");

            String name = InputValidator.readString(scanner, "Enter your name: ");
            InputValidator.validateName(name);

            String email = InputValidator.readString(scanner, "Enter your email: ");
            InputValidator.validateEmail(email);

            // Check if email already exists
            if (userMap.containsKey(email.toLowerCase())) {
                System.err.println("✗ This email is already registered. Please login instead.");
                return null;
            }

            String phone = InputValidator.readString(scanner, "Enter your phone (10 digits): ");
            InputValidator.validatePhone(phone);

            String password = InputValidator.readString(scanner, "Enter password (min 5 chars): ");
            InputValidator.validatePassword(password);

            Customer customer = new Customer(nextCustomerId, name.trim(), email.trim(),
                    phone.trim(), password);
            nextCustomerId++;
            customers.add(customer);
            userMap.put(email.toLowerCase(), customer);
            saveCustomersToFile();

            System.out.println("✓ Registration successful! Your Customer ID is: " + customer.getUserId());
            return customer;

        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles customer login.
     */
    public Customer loginCustomer(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("          CUSTOMER LOGIN");
        System.out.println("========================================");

        String email = InputValidator.readString(scanner, "Enter email: ");
        String password = InputValidator.readString(scanner, "Enter password: ");

        User user = userMap.get(email.toLowerCase());
        if (user != null && user instanceof Customer && user.getPassword().equals(password)) {
            System.out.println("✓ Login successful! Welcome, " + user.getName() + "!");
            return (Customer) user;
        }
        System.err.println("✗ Invalid email or password. Please try again.");
        return null;
    }

    /**
     * Handles admin login.
     */
    public Admin loginAdmin(Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("           ADMIN LOGIN");
        System.out.println("========================================");

        String email = InputValidator.readString(scanner, "Enter admin email: ");
        String password = InputValidator.readString(scanner, "Enter admin password: ");

        User user = userMap.get(email.toLowerCase());
        if (user != null && user instanceof Admin && user.getPassword().equals(password)) {
            System.out.println("✓ Admin login successful! Welcome, " + user.getName() + "!");
            return (Admin) user;
        }
        System.err.println("✗ Invalid admin credentials. Please try again.");
        return null;
    }

    /**
     * Returns all customers. Demonstrates COLLECTIONS usage.
     */
    public List<Customer> getAllCustomers() {
        return customers;
    }

    /**
     * Finds a customer by ID using Stream API.
     */
    public Customer findCustomerById(int customerId) {
        Optional<Customer> result = customers.stream()
                .filter(c -> c.getUserId() == customerId)
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Returns all admins.
     */
    public List<Admin> getAllAdmins() {
        return admins;
    }
}
