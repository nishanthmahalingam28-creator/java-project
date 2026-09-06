package com.foodordering.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order.
 * Demonstrates COLLECTIONS (ArrayList), GENERICS, and ENCAPSULATION.
 */
public class Order {

    private int orderId;
    private Customer customer;
    private List<OrderItem> orderItems;
    private LocalDate orderDate;
    private double totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private String paymentMethod;
    private String paymentId;

    /** Order status constants */
    public static final String PLACED = "PLACED";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String PREPARING = "PREPARING";
    public static final String READY = "READY";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = new ArrayList<>();
        this.orderDate = LocalDate.now();
        this.paymentStatus = "PENDING";
        this.orderStatus = PLACED;
    }

    // --- Getters and setters ---

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getFormattedDate() {
        return orderDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    /**
     * Displays the order summary.
     */
    public void displayOrder() {
        System.out.println("========================================");
        System.out.println("           ORDER SUMMARY");
        System.out.println("========================================");
        System.out.println("Order ID       : " + orderId);
        System.out.println("Customer       : " + customer.getName());
        System.out.println("Date           : " + getFormattedDate());
        System.out.println("Status         : " + orderStatus);
        System.out.println("Payment Status : " + paymentStatus);
        if (paymentMethod != null) {
            System.out.println("Payment Method : " + paymentMethod);
        }
        System.out.println("----------------------------------------");
        System.out.printf("%-20s %-8s %-10s%n", "Item", "Qty", "Price");
        System.out.println("----------------------------------------");
        for (OrderItem oi : orderItems) {
            System.out.printf("%-20s %-8d ₹%-10.2f%n",
                    oi.getFoodName(), oi.getQuantity(), oi.getPrice());
        }
        System.out.println("----------------------------------------");
        System.out.printf("Total Amount: ₹%.2f%n", totalAmount);
        System.out.println("========================================");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append("|");
        sb.append(customer.getUserId()).append("|");
        sb.append(orderDate).append("|");
        sb.append(totalAmount).append("|");
        sb.append(paymentStatus).append("|");
        sb.append(orderStatus).append("|");
        sb.append(paymentMethod != null ? paymentMethod : "N/A").append("|");
        sb.append(paymentId != null ? paymentId : "N/A").append("|");
        for (int i = 0; i < orderItems.size(); i++) {
            if (i > 0) sb.append(";");
            sb.append(orderItems.get(i).toString());
        }
        return sb.toString();
    }
}
