package com.foodordering.util;

import com.foodordering.model.Customer;
import com.foodordering.model.FoodItem;
import com.foodordering.model.Order;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all file I/O for the application.
 * Demonstrates FILE HANDLING using BufferedReader/BufferedWriter
 * and modern java.nio.file APIs.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String BILLS_DIR = "data/bills";
    private static final String CUSTOMERS_FILE = "data/customers.txt";
    private static final String FOOD_FILE = "data/food_items.txt";
    private static final String ORDERS_FILE = "data/orders.txt";

    /**
     * Creates the data directory and bills subdirectory if they don't exist.
     */
    public static void ensureDirectories() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Files.createDirectories(Paths.get(BILLS_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create data directories: " + e.getMessage());
        }
    }

    // --- Food Items ---

    /**
     * Saves the list of food items to file.
     */
    public static void saveFoodItems(List<FoodItem> foodItems) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FOOD_FILE))) {
            for (FoodItem item : foodItems) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving food items: " + e.getMessage());
        }
    }

    /**
     * Loads food items from file. Returns null if file doesn't exist.
     */
    public static List<FoodItem> loadFoodItems() {
        Path path = Paths.get(FOOD_FILE);
        if (!Files.exists(path)) {
            return null;
        }
        List<FoodItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FOOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    FoodItem item = new FoodItem(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4]),
                            parts[5]
                    );
                    items.add(item);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading food items: " + e.getMessage());
        }
        return items;
    }

    // --- Customers ---

    /**
     * Saves the list of customers to file.
     */
    public static void saveCustomers(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                String address = c.getAddress() != null ? c.getAddress() : "N/A";
                writer.write(c.getUserId() + "|" + c.getName() + "|" + c.getEmail() + "|" +
                        c.getPhone() + "|" + c.getPassword() + "|" + address);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Loads customers from file. Returns null if file doesn't exist.
     */
    public static List<Customer> loadCustomers() {
        Path path = Paths.get(CUSTOMERS_FILE);
        if (!Files.exists(path)) {
            return null;
        }
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    Customer c = new Customer(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4]
                    );
                    if (!parts[5].equals("N/A")) {
                        c.setAddress(parts[5]);
                    }
                    customers.add(c);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    // --- Orders ---

    /**
     * Saves the list of orders to file.
     */
    public static void saveOrders(List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order o : orders) {
                writer.write(o.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    /**
     * Loads orders from file. Returns null if file doesn't exist.
     * Note: Orders are loaded with a reference to the customer object.
     */
    public static List<Order> loadOrders(List<Customer> customers) {
        Path path = Paths.get(ORDERS_FILE);
        if (!Files.exists(path)) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] mainParts = line.split("\\|");
                if (mainParts.length < 9) continue;

                int orderId = Integer.parseInt(mainParts[0]);
                int custId = Integer.parseInt(mainParts[1]);
                String dateStr = mainParts[2];
                double total = Double.parseDouble(mainParts[3]);
                String payStatus = mainParts[4];
                String orderStatus = mainParts[5];
                String payMethod = mainParts[6];
                String payId = mainParts[7];

                Customer matched = null;
                for (Customer c : customers) {
                    if (c.getUserId() == custId) {
                        matched = c;
                        break;
                    }
                }
                if (matched == null) continue;

                Order order = new Order(orderId, matched);
                order.setTotalAmount(total);
                order.setPaymentStatus(payStatus);
                order.setOrderStatus(orderStatus);
                order.setPaymentMethod(payMethod);
                order.setPaymentId(payId);

                // Parse order items
                if (mainParts.length >= 9 && !mainParts[8].equals("N/A")) {
                    String[] itemStrs = mainParts[8].split(";");
                    for (String is : itemStrs) {
                        String[] ip = is.split(",");
                        if (ip.length >= 4) {
                            order.addOrderItem(new com.foodordering.model.OrderItem(
                                    Integer.parseInt(ip[0]),
                                    ip[1],
                                    Integer.parseInt(ip[2]),
                                    Double.parseDouble(ip[3])
                            ));
                        }
                    }
                }
                orders.add(order);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
        return orders;
    }

    // --- Bills ---

    /**
     * Saves a bill to a text file in the bills directory.
     */
    public static void saveBill(String billContent, int orderId) {
        String filename = BILLS_DIR + "/bill_" + orderId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(billContent);
        } catch (IOException e) {
            System.err.println("Error saving bill: " + e.getMessage());
        }
    }
}
