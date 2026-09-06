package com.foodordering.main;

import com.foodordering.exception.EmptyCartException;
import com.foodordering.exception.FoodNotFoundException;
import com.foodordering.exception.InsufficientStockException;
import com.foodordering.exception.InvalidInputException;
import com.foodordering.model.Admin;
import com.foodordering.model.Cart;
import com.foodordering.model.Customer;
import com.foodordering.model.FoodItem;
import com.foodordering.model.Order;
import com.foodordering.model.OrderItem;
import com.foodordering.service.CartService;
import com.foodordering.service.FoodService;
import com.foodordering.service.OrderService;
import com.foodordering.service.UserService;
import com.foodordering.util.FileManager;
import com.foodordering.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Main entry point of the Food Ordering and Management System.
 * Contains the menu-driven console interface.
 *
 * Main.java primarily starts the application and coordinates the menus.
 * Business logic is delegated to the service classes.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static FoodService foodService;
    private static OrderService orderService;
    private static CartService cartService;

    public static void main(String[] args) {
        // Ensure data directories exist
        FileManager.ensureDirectories();

        // Initialize services
        userService = new UserService();
        foodService = new FoodService();
        orderService = new OrderService(foodService);
        cartService = new CartService(foodService);

        // Load data from files
        foodService.loadFoodItems();
        userService.loadCustomersFromFile();
        orderService.loadOrders(userService.getAllCustomers());

        System.out.println("========================================");
        System.out.println("  FOOD ORDERING & MANAGEMENT SYSTEM");
        System.out.println("  (Academic Java OOP Demo Project)");
        System.out.println("========================================");

        // Main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = InputValidator.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        customerLogin();
                        break;
                    case 2:
                        customerRegistration();
                        break;
                    case 3:
                        adminLogin();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Thank you for using the Food Ordering System. Goodbye!");
                        break;
                    default:
                        throw new InvalidInputException(
                                "Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (InvalidInputException e) {
                System.err.println("✗ " + e.getMessage());
            }
        }
        scanner.close();
    }

    // --- Main Menu ---

    private static void displayMainMenu() {
        System.out.println("\n========================================");
        System.out.println("              MAIN MENU");
        System.out.println("========================================");
        System.out.println("1. Customer Login");
        System.out.println("2. Customer Registration");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
        System.out.println("========================================");
    }

    // --- Customer Flow ---

    private static void customerRegistration() {
        userService.registerCustomer(scanner);
    }

    private static void customerLogin() {
        Customer customer = userService.loginCustomer(scanner);
        if (customer != null) {
            // Each customer gets their own cart
            cartService = new CartService(foodService);
            customerMenu(customer);
        }
    }

    private static void customerMenu(Customer customer) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========================================");
            System.out.println("          CUSTOMER MENU");
            System.out.println("========================================");
            System.out.println("1.  View Food Menu");
            System.out.println("2.  Search Food");
            System.out.println("3.  Add Food to Cart");
            System.out.println("4.  Remove Food from Cart");
            System.out.println("5.  View Cart");
            System.out.println("6.  Place Order");
            System.out.println("7.  Make Payment");
            System.out.println("8.  View Order History");
            System.out.println("9.  Cancel Order");
            System.out.println("10. View Profile");
            System.out.println("11. Logout");
            System.out.println("========================================");

            int choice = InputValidator.readInt(scanner, "Enter your choice: ");
            try {
                InputValidator.validateMenuChoice(choice, 1, 11);
                switch (choice) {
                    case 1:
                        foodService.displayAllFoodItems();
                        break;
                    case 2:
                        searchFoodMenu();
                        break;
                    case 3:
                        cartService.addToCart(scanner);
                        break;
                    case 4:
                        cartService.removeFromCart(scanner);
                        break;
                    case 5:
                        cartService.viewCart();
                        break;
                    case 6:
                        placeOrder(customer);
                        break;
                    case 7:
                        makePayment(customer);
                        break;
                    case 8:
                        orderService.displayOrderHistory(customer);
                        break;
                    case 9:
                        cancelOrder(customer);
                        break;
                    case 10:
                        customer.displayUserDetails();
                        break;
                    case 11:
                        loggedIn = false;
                        System.out.println("✓ Logged out successfully.");
                        break;
                }
            } catch (InvalidInputException e) {
                System.err.println("✗ " + e.getMessage());
            }
        }
    }

    private static void searchFoodMenu() {
        System.out.println("\n========================================");
        System.out.println("          SEARCH FOOD");
        System.out.println("========================================");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Max Price");
        System.out.println("3. Filter by Category");
        System.out.println("4. Sort by Price");
        System.out.println("========================================");

        int choice = InputValidator.readInt(scanner, "Enter your choice: ");
        try {
            InputValidator.validateMenuChoice(choice, 1, 4);
            switch (choice) {
                case 1:
                    String name = InputValidator.readString(scanner, "Enter food name to search: ");
                    foodService.displaySearchResults(foodService.searchFood(name));
                    break;
                case 2:
                    double maxPrice = InputValidator.readDouble(scanner, "Enter maximum price: ");
                    if (Double.isNaN(maxPrice)) {
                        throw new InvalidInputException("Please enter a valid price.");
                    }
                    foodService.displaySearchResults(foodService.searchFood(maxPrice));
                    break;
                case 3:
                    String category = InputValidator.readString(scanner, "Enter category: ");
                    foodService.displaySearchResults(foodService.filterByCategory(category));
                    break;
                case 4:
                    foodService.displaySearchResults(foodService.sortByPrice());
                    break;
            }
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void placeOrder(Customer customer) {
        try {
            if (cartService.isCartEmpty()) {
                throw new EmptyCartException("Cannot place order: your cart is empty.");
            }
            Order order = orderService.placeOrder(customer, cartService.getCart());
            System.out.println("✓ Order placed successfully! Order ID: " + order.getOrderId());
            order.displayOrder();
            cartService.clearCart();
        } catch (EmptyCartException | InsufficientStockException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void makePayment(Customer customer) {
        try {
            // Find the most recent unpaid order for this customer
            List<Order> history = orderService.getOrderHistory(customer);
            Order unpaidOrder = null;
            for (int i = history.size() - 1; i >= 0; i--) {
                if (history.get(i).getPaymentStatus().equals("PENDING")) {
                    unpaidOrder = history.get(i);
                    break;
                }
            }
            if (unpaidOrder == null) {
                System.err.println("✗ No pending payments found. Please place an order first.");
                return;
            }
            orderService.processPayment(scanner, unpaidOrder);
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void cancelOrder(Customer customer) {
        try {
            int orderId = InputValidator.readInt(scanner, "Enter Order ID to cancel: ");
            if (orderId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid order ID.");
            }
            // Verify the order belongs to this customer
            Order order = orderService.findOrderById(orderId);
            if (order == null || order.getCustomer().getUserId() != customer.getUserId()) {
                System.err.println("✗ Order not found in your history.");
                return;
            }
            boolean cancelled = orderService.cancelOrder(orderId);
            if (cancelled) {
                System.out.println("✓ Order " + orderId + " has been cancelled.");
            } else {
                System.err.println("✗ Order cannot be cancelled (already delivered or cancelled).");
            }
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    // --- Admin Flow ---

    private static void adminLogin() {
        Admin admin = userService.loginAdmin(scanner);
        if (admin != null) {
            adminMenu(admin);
        }
    }

    private static void adminMenu(Admin admin) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n========================================");
            System.out.println("            ADMIN MENU");
            System.out.println("========================================");
            System.out.println("1.  Add Food Item");
            System.out.println("2.  View Food Items");
            System.out.println("3.  Update Food Item");
            System.out.println("4.  Delete Food Item");
            System.out.println("5.  Search Food Item");
            System.out.println("6.  View All Orders");
            System.out.println("7.  Update Order Status");
            System.out.println("8.  View Customers");
            System.out.println("9.  Generate Sales Summary");
            System.out.println("10. Logout");
            System.out.println("========================================");

            int choice = InputValidator.readInt(scanner, "Enter your choice: ");
            try {
                InputValidator.validateMenuChoice(choice, 1, 10);
                switch (choice) {
                    case 1:
                        addFoodItem();
                        break;
                    case 2:
                        foodService.displayAllFoodItems();
                        break;
                    case 3:
                        updateFoodItem();
                        break;
                    case 4:
                        deleteFoodItem();
                        break;
                    case 5:
                        adminSearchFood();
                        break;
                    case 6:
                        orderService.displayAllOrders();
                        break;
                    case 7:
                        updateOrderStatus();
                        break;
                    case 8:
                        viewCustomers();
                        break;
                    case 9:
                        orderService.generateSalesSummary();
                        break;
                    case 10:
                        loggedIn = false;
                        System.out.println("✓ Admin logged out successfully.");
                        break;
                }
            } catch (InvalidInputException e) {
                System.err.println("✗ " + e.getMessage());
            }
        }
    }

    private static void addFoodItem() {
        try {
            System.out.println("\n========================================");
            System.out.println("          ADD FOOD ITEM");
            System.out.println("========================================");
            String name = InputValidator.readString(scanner, "Enter food name: ");
            String category = InputValidator.readString(scanner, "Enter category: ");
            double price = InputValidator.readDouble(scanner, "Enter price: ");
            if (Double.isNaN(price)) {
                throw new InvalidInputException("Please enter a valid price.");
            }
            int qty = InputValidator.readInt(scanner, "Enter available quantity: ");
            if (qty == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid quantity.");
            }
            String description = InputValidator.readString(scanner, "Enter description: ");

            // Uses the overloaded addFoodItem method (METHOD OVERLOADING)
            FoodItem item = foodService.addFoodItem(name, category, price, qty, description);
            System.out.println("✓ Food item added successfully! ID: " + item.getFoodId());
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void updateFoodItem() {
        try {
            int foodId = InputValidator.readInt(scanner, "Enter Food ID to update: ");
            if (foodId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid food ID.");
            }
            FoodItem existing = foodService.findFoodById(foodId);
            if (existing == null) {
                throw new FoodNotFoundException("Food item with ID " + foodId + " not found.");
            }
            System.out.println("Current details: " + existing.getFoodName() + " | " +
                    existing.getCategory() + " | ₹" + existing.getPrice() +
                    " | Qty: " + existing.getAvailableQuantity());

            String name = InputValidator.readString(scanner, "Enter new name: ");
            String category = InputValidator.readString(scanner, "Enter new category: ");
            double price = InputValidator.readDouble(scanner, "Enter new price: ");
            if (Double.isNaN(price)) {
                throw new InvalidInputException("Please enter a valid price.");
            }
            int qty = InputValidator.readInt(scanner, "Enter new quantity: ");
            if (qty == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid quantity.");
            }
            String description = InputValidator.readString(scanner, "Enter new description: ");

            foodService.updateFoodItem(foodId, name, category, price, qty, description);
            System.out.println("✓ Food item updated successfully!");
        } catch (InvalidInputException | FoodNotFoundException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void deleteFoodItem() {
        try {
            int foodId = InputValidator.readInt(scanner, "Enter Food ID to delete: ");
            if (foodId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid food ID.");
            }
            foodService.deleteFoodItem(foodId);
            System.out.println("✓ Food item deleted successfully!");
        } catch (InvalidInputException | FoodNotFoundException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void adminSearchFood() {
        System.out.println("\n========================================");
        System.out.println("          SEARCH FOOD ITEM");
        System.out.println("========================================");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Max Price");
        System.out.println("3. Filter by Category");
        System.out.println("========================================");

        int choice = InputValidator.readInt(scanner, "Enter your choice: ");
        try {
            InputValidator.validateMenuChoice(choice, 1, 3);
            switch (choice) {
                case 1:
                    String name = InputValidator.readString(scanner, "Enter food name: ");
                    foodService.displaySearchResults(foodService.searchFood(name));
                    break;
                case 2:
                    double maxPrice = InputValidator.readDouble(scanner, "Enter max price: ");
                    if (Double.isNaN(maxPrice)) {
                        throw new InvalidInputException("Please enter a valid price.");
                    }
                    foodService.displaySearchResults(foodService.searchFood(maxPrice));
                    break;
                case 3:
                    String category = InputValidator.readString(scanner, "Enter category: ");
                    foodService.displaySearchResults(foodService.filterByCategory(category));
                    break;
            }
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void updateOrderStatus() {
        try {
            int orderId = InputValidator.readInt(scanner, "Enter Order ID: ");
            if (orderId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid order ID.");
            }
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                System.err.println("✗ Order not found.");
                return;
            }
            System.out.println("Current status: " + order.getOrderStatus());
            System.out.println("Available statuses:");
            System.out.println("1. CONFIRMED");
            System.out.println("2. PREPARING");
            System.out.println("3. READY");
            System.out.println("4. DELIVERED");
            System.out.println("5. CANCELLED");
            int statusChoice = InputValidator.readInt(scanner, "Select new status (1-5): ");
            if (statusChoice == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid choice.");
            }
            InputValidator.validateMenuChoice(statusChoice, 1, 5);
            String newStatus;
            switch (statusChoice) {
                case 1: newStatus = Order.CONFIRMED; break;
                case 2: newStatus = Order.PREPARING; break;
                case 3: newStatus = Order.READY; break;
                case 4: newStatus = Order.DELIVERED; break;
                case 5: newStatus = Order.CANCELLED; break;
                default: throw new InvalidInputException("Invalid status choice.");
            }
            if (statusChoice == 5) {
                orderService.cancelOrder(orderId);
                System.out.println("✓ Order " + orderId + " has been cancelled.");
            } else {
                boolean updated = orderService.updateOrderStatus(orderId, newStatus);
                if (updated) {
                    System.out.println("✓ Order status updated to " + newStatus + ".");
                } else {
                    System.err.println("✗ Could not update order status.");
                }
            }
        } catch (InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void viewCustomers() {
        List<Customer> customers = userService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No registered customers.");
            return;
        }
        System.out.println("========================================");
        System.out.println("          REGISTERED CUSTOMERS");
        System.out.println("========================================");
        System.out.printf("%-10s %-20s %-25s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("----------------------------------------");
        for (Customer c : customers) {
            System.out.printf("%-10d %-20s %-25s %-15s%n",
                    c.getUserId(), c.getName(), c.getEmail(), c.getPhone());
        }
        System.out.println("========================================");
    }
}
