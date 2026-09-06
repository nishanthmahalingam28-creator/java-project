package com.foodordering.model;

import com.foodordering.exception.EmptyCartException;
import com.foodordering.exception.InsufficientStockException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's shopping cart.
 * Demonstrates COLLECTIONS (ArrayList), GENERICS, and ENCAPSULATION.
 */
public class Cart {

    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Adds a food item to the cart. If the item already exists in the cart,
     * increases the quantity. Checks stock availability.
     */
    public void addItem(FoodItem foodItem, int quantity) throws InsufficientStockException {
        if (quantity <= 0) {
            throw new InsufficientStockException("Quantity must be greater than zero.");
        }
        int currentInCart = 0;
        for (CartItem ci : items) {
            if (ci.getFoodItem().getFoodId() == foodItem.getFoodId()) {
                currentInCart = ci.getQuantity();
                break;
            }
        }
        if (currentInCart + quantity > foodItem.getAvailableQuantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock for '" + foodItem.getFoodName() +
                    "'. Available: " + foodItem.getAvailableQuantity() +
                    ", already in cart: " + currentInCart);
        }
        // If already in cart, update quantity
        for (CartItem ci : items) {
            if (ci.getFoodItem().getFoodId() == foodItem.getFoodId()) {
                ci.setQuantity(ci.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(foodItem, quantity));
    }

    /**
     * Removes a food item from the cart by food ID.
     */
    public boolean removeItem(int foodId) {
        return items.removeIf(ci -> ci.getFoodItem().getFoodId() == foodId);
    }

    /**
     * Updates the quantity of a cart item.
     */
    public void updateQuantity(int foodId, int newQuantity) throws InsufficientStockException {
        if (newQuantity <= 0) {
            // If new quantity is zero or negative, remove the item
            removeItem(foodId);
            return;
        }
        for (CartItem ci : items) {
            if (ci.getFoodItem().getFoodId() == foodId) {
                if (newQuantity > ci.getFoodItem().getAvailableQuantity()) {
                    throw new InsufficientStockException(
                            "Insufficient stock. Available: " +
                            ci.getFoodItem().getAvailableQuantity());
                }
                ci.setQuantity(newQuantity);
                return;
            }
        }
    }

    /**
     * Displays all items in the cart.
     */
    public void viewCart() throws EmptyCartException {
        if (items.isEmpty()) {
            throw new EmptyCartException("Your cart is empty.");
        }
        System.out.println("========================================");
        System.out.println("              YOUR CART");
        System.out.println("========================================");
        System.out.printf("%-6s %-20s %-15s %-10s %-8s %-10s%n",
                "ID", "Name", "Category", "Price", "Qty", "Total");
        System.out.println("----------------------------------------");
        for (CartItem ci : items) {
            ci.displayCartItem();
        }
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal: ₹%.2f%n", calculateSubtotal());
        System.out.println("========================================");
    }

    /**
     * Calculates the subtotal (sum of all line totals).
     */
    public double calculateSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getLineTotal)
                .sum();
    }

    /**
     * Calculates the total including 5% tax.
     */
    public double calculateTotal() {
        return calculateSubtotal() * 1.05;
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
