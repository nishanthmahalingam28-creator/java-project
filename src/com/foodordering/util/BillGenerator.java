package com.foodordering.util;

import com.foodordering.model.Order;
import com.foodordering.model.OrderItem;

import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating formatted bills.
 * Demonstrates separation of concerns (bill generation is separate from order logic).
 */
public class BillGenerator {

    private static final double TAX_RATE = 0.05; // 5% tax

    /**
     * Generates a formatted bill string for the given order.
     */
    public static String generateBill(Order order) {
        StringBuilder sb = new StringBuilder();
        String separator = "========================================\n";

        sb.append(separator);
        sb.append("          FOOD ORDER BILL\n");
        sb.append("===============\n\n");
        sb.append(String.format("Order ID       : %d%n", order.getOrderId()));
        sb.append(String.format("Customer Name  : %s%n", order.getCustomer().getName()));
        sb.append(String.format("Date           : %s%n", order.getFormattedDate()));
        sb.append("\n---\n\n");
        sb.append(String.format("%-20s %-8s %s%n", "Item", "Qty", "Price"));
        sb.append("\n");

        double subtotal = 0;
        for (OrderItem oi : order.getOrderItems()) {
            sb.append(String.format("%-20s %-8d ₹%-8.2f%n",
                    oi.getFoodName(), oi.getQuantity(), oi.getPrice()));
            subtotal += oi.getLineTotal();
        }
        double tax = subtotal * TAX_RATE;
        double grandTotal = subtotal + tax;

        sb.append("------------------------------\n\n");
        sb.append(String.format("Subtotal                  : ₹%.2f%n", subtotal));
        sb.append(String.format("Tax (5%%)                  : ₹%.2f%n", tax));
        sb.append(String.format("Grand Total               : ₹%.2f%n", grandTotal));
        sb.append("\n");
        sb.append(String.format("Payment Method            : %s%n",
                order.getPaymentMethod() != null ? order.getPaymentMethod() : "N/A"));
        sb.append(String.format("Payment Status            : %s%n", order.getPaymentStatus()));
        sb.append(String.format("Order Status              : %s%n", order.getOrderStatus()));
        sb.append("\n");
        sb.append(separator);
        sb.append("       THANK YOU FOR ORDERING\n");
        sb.append("======================\n");

        return sb.toString();
    }

    /**
     * Generates the bill and prints it to console.
     */
    public static void printBill(Order order) {
        String bill = generateBill(order);
        System.out.println(bill);
    }

    /**
     * Returns the tax rate used for bill generation.
     */
    public static double getTaxRate() {
        return TAX_RATE;
    }
}
