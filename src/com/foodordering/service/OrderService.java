package com.foodordering.service;

import com.foodordering.exception.EmptyCartException;
import com.foodordering.exception.FoodNotFoundException;
import com.foodordering.exception.InsufficientStockException;
import com.foodordering.exception.InvalidInputException;
import com.foodordering.interface_pkg.OrderOperations;
import com.foodordering.model.Cart;
import com.foodordering.model.CartItem;
import com.foodordering.model.Customer;
import com.foodordering.model.FoodItem;
import com.foodordering.model.Order;
import com.foodordering.model.OrderItem;
import com.foodordering.payment.CardPayment;
import com.foodordering.payment.CashPayment;
import com.foodordering.payment.PaymentMethod;
import com.foodordering.payment.UpiPayment;
import com.foodordering.util.BillGenerator;
import com.foodordering.util.FileManager;
import com.foodordering.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Service class for order operations.
 * Implements the OrderOperations interface — demonstrates INTERFACES,
 * POLYMORPHISM (PaymentMethod reference), COLLECTIONS, GENERICS, and STREAM API.
 */
public class OrderService implements OrderOperations {

    private List<Order> orders;
    private int nextOrderId;
    private FoodService foodService;

    public OrderService(FoodService foodService) {
        this.orders = new ArrayList<>();
        this.nextOrderId = 1001;
        this.foodService = foodService;
    }

    /**
     * Loads orders from file.
     */
    public void loadOrders(List<Customer> customers) {
        List<Order> loaded = FileManager.loadOrders(customers);
        if (loaded != null && !loaded.isEmpty()) {
            orders = loaded;
            for (Order o : orders) {
                if (o.getOrderId() >= nextOrderId) {
                    nextOrderId = o.getOrderId() + 1;
                }
            }
        }
    }

    /**
     * Saves orders to file.
     */
    public void saveOrders() {
        FileManager.saveOrders(orders);
    }

    /**
     * Places a new order for the given customer.
     * Implements the OrderOperations interface method.
     */
    @Override
    public Order placeOrder(Customer customer) throws EmptyCartException, InsufficientStockException {
        Cart cart = customer != null ? null : null; // placeholder, cart is passed by caller
        throw new UnsupportedOperationException("Use placeOrder(Customer, Cart) instead.");
    }

    /**
     * Places a new order using the given customer's cart.
     * This is the main order creation method.
     */
    public Order placeOrder(Customer customer, Cart cart)
            throws EmptyCartException, InsufficientStockException {

        if (cart == null || cart.isEmpty()) {
            throw new EmptyCartException("Cannot place order: your cart is empty.");
        }

        // Verify stock availability for all cart items
        for (CartItem ci : cart.getItems()) {
            FoodItem food = foodService.findFoodById(ci.getFoodItem().getFoodId());
            if (food == null) {
                throw new InsufficientStockException(
                        "Food item '" + ci.getFoodItem().getFoodName() + "' no longer exists.");
            }
            if (ci.getQuantity() > food.getAvailableQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for '" + food.getFoodName() +
                        "'. Requested: " + ci.getQuantity() +
                        ", Available: " + food.getAvailableQuantity());
            }
        }

        // Create the order
        Order order = new Order(nextOrderId, customer);
        nextOrderId++;

        // Add order items and reduce stock
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem(
                    ci.getFoodItem().getFoodId(),
                    ci.getFoodItem().getFoodName(),
                    ci.getQuantity(),
                    ci.getFoodItem().getPrice()
            );
            order.addOrderItem(oi);

            // Reduce available stock
            FoodItem food = foodService.findFoodById(ci.getFoodItem().getFoodId());
            food.setAvailableQuantity(food.getAvailableQuantity() - ci.getQuantity());
        }

        // Calculate total (subtotal + 5% tax)
        double subtotal = cart.calculateSubtotal();
        double total = subtotal + (subtotal * BillGenerator.getTaxRate());
        order.setTotalAmount(total);
        order.setOrderStatus(Order.PLACED);

        orders.add(order);
        foodService.saveFoodItems();
        saveOrders();

        return order;
    }

    /**
     * Processes payment for an order.
     * Demonstrates POLYMORPHISM: a single PaymentMethod reference is used
     * to call pay() on different implementations (UpiPayment, CardPayment, CashPayment).
     */
    public PaymentMethod processPayment(Scanner scanner, Order order) throws InvalidInputException {
        System.out.println("\n========================================");
        System.out.println("              PAYMENT");
        System.out.println("========================================");
        System.out.println("Order Total: ₹" + String.format("%.2f", order.getTotalAmount()));
        System.out.println("----------------------------------------");
        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. Cash");
        System.out.println("========================================");

        int choice = InputValidator.readInt(scanner, "Select payment method (1-3): ");
        if (choice == Integer.MIN_VALUE) {
            throw new InvalidInputException("Please enter a valid number.");
        }
        InputValidator.validateMenuChoice(choice, 1, 3);

        // POLYMORPHISM: same interface reference, different implementations
        PaymentMethod payment;
        switch (choice) {
            case 1:
                payment = new UpiPayment();
                break;
            case 2:
                payment = new CardPayment();
                break;
            case 3:
                payment = new CashPayment();
                break;
            default:
                throw new InvalidInputException("Invalid payment option.");
        }

        String paymentId = payment.pay(order.getTotalAmount());
        order.setPaymentMethod(payment.getPaymentType());
        order.setPaymentId(paymentId);
        order.setPaymentStatus("SUCCESS");
        saveOrders();

        System.out.println("✓ Payment Successful!");
        System.out.println("  Payment ID   : " + paymentId);
        System.out.println("  Method       : " + payment.getPaymentType());
        System.out.println("  Status       : SUCCESS");

        // Generate and save bill
        String bill = BillGenerator.generateBill(order);
        FileManager.saveBill(bill, order.getOrderId());
        BillGenerator.printBill(order);

        return payment;
    }

    /**
     * Cancels an order by ID.
     * Implements the OrderOperations interface method.
     */
    @Override
    public boolean cancelOrder(int orderId) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId && !o.getOrderStatus().equals(Order.CANCELLED)
                    && !o.getOrderStatus().equals(Order.DELIVERED)) {
                // Restore stock
                for (OrderItem oi : o.getOrderItems()) {
                    FoodItem food = foodService.findFoodById(oi.getFoodId());
                    if (food != null) {
                        food.setAvailableQuantity(food.getAvailableQuantity() + oi.getQuantity());
                    }
                }
                o.setOrderStatus(Order.CANCELLED);
                foodService.saveFoodItems();
                saveOrders();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the order history for a given customer using Stream API.
     * Implements the OrderOperations interface method.
     */
    @Override
    public List<Order> getOrderHistory(Customer customer) {
        return orders.stream()
                .filter(o -> o.getCustomer().getUserId() == customer.getUserId())
                .collect(Collectors.toList());
    }

    /**
     * Returns all orders.
     * Implements the OrderOperations interface method.
     */
    @Override
    public List<Order> getAllOrders() {
        return orders;
    }

    /**
     * Finds an order by ID.
     */
    public Order findOrderById(int orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the status of an order.
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        Order order = findOrderById(orderId);
        if (order == null) {
            return false;
        }
        if (order.getOrderStatus().equals(Order.CANCELLED)) {
            return false;
        }
        order.setOrderStatus(newStatus);
        saveOrders();
        return true;
    }

    /**
     * Displays all orders (admin view).
     */
    public void displayAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        System.out.println("========================================");
        System.out.println("            ALL ORDERS");
        System.out.println("========================================");
        System.out.printf("%-8s %-15s %-12s %-12s %-15s%n",
                "OrderID", "Customer", "Date", "Status", "Payment Status");
        System.out.println("----------------------------------------");
        for (Order o : orders) {
            System.out.printf("%-8d %-15s %-12s %-12s %-15s%n",
                    o.getOrderId(),
                    o.getCustomer().getName(),
                    o.getFormattedDate(),
                    o.getOrderStatus(),
                    o.getPaymentStatus());
        }
        System.out.println("========================================");
    }

    /**
     * Displays order history for a customer.
     */
    public void displayOrderHistory(Customer customer) {
        List<Order> history = getOrderHistory(customer);
        if (history.isEmpty()) {
            System.out.println("You have no previous orders.");
            return;
        }
        System.out.println("========================================");
        System.out.println("          ORDER HISTORY");
        System.out.println("========================================");
        System.out.printf("%-8s %-12s %-12s %-15s ₹%-10s%n",
                "OrderID", "Date", "Status", "Payment", "Total");
        System.out.println("----------------------------------------");
        for (Order o : history) {
            System.out.printf("%-8d %-12s %-12s %-15s ₹%-10.2f%n",
                    o.getOrderId(),
                    o.getFormattedDate(),
                    o.getOrderStatus(),
                    o.getPaymentStatus(),
                    o.getTotalAmount());
        }
        System.out.println("========================================");
    }

    // --- Sales Analytics using Stream API ---

    /**
     * Generates a sales summary for the admin.
     * Demonstrates STREAM API for analytics.
     */
    public void generateSalesSummary() {
        if (orders.isEmpty()) {
            System.out.println("No orders to analyze.");
            return;
        }

        long totalOrders = orders.size();
        long completedOrders = orders.stream()
                .filter(o -> o.getOrderStatus().equals(Order.DELIVERED))
                .count();
        long cancelledOrders = orders.stream()
                .filter(o -> o.getOrderStatus().equals(Order.CANCELLED))
                .count();

        double totalRevenue = orders.stream()
                .filter(o -> !o.getOrderStatus().equals(Order.CANCELLED))
                .mapToDouble(Order::getTotalAmount)
                .sum();

        // Most ordered food item using Stream API
        Map<String, Integer> itemCount = new HashMap<>();
        for (Order o : orders) {
            if (!o.getOrderStatus().equals(Order.CANCELLED)) {
                for (OrderItem oi : o.getOrderItems()) {
                    itemCount.merge(oi.getFoodName(), oi.getQuantity(), Integer::sum);
                }
            }
        }
        String mostOrdered = itemCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        long activeOrders = totalOrders - cancelledOrders;
        double avgOrderValue = activeOrders > 0 ? totalRevenue / activeOrders : 0;

        System.out.println("========================================");
        System.out.println("          SALES SUMMARY");
        System.out.println("========================================");
        System.out.println("Total Orders         : " + totalOrders);
        System.out.println("Completed Orders     : " + completedOrders);
        System.out.println("Cancelled Orders     : " + cancelledOrders);
        System.out.printf("Total Revenue        : ₹%.2f%n", totalRevenue);
        System.out.println("Most Ordered Item    : " + mostOrdered);
        System.out.printf("Average Order Value   : ₹%.2f%n", avgOrderValue);
        System.out.println("========================================");
    }
}
