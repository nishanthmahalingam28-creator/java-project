package com.foodordering.service;

import com.foodordering.exception.FoodNotFoundException;
import com.foodordering.exception.InsufficientStockException;
import com.foodordering.exception.InvalidInputException;
import com.foodordering.exception.EmptyCartException;
import com.foodordering.model.Cart;
import com.foodordering.model.FoodItem;
import com.foodordering.util.InputValidator;

import java.util.Scanner;

/**
 * Service class for cart operations.
 * Demonstrates separation of cart logic from the cart model.
 */
public class CartService {

    private Cart cart;
    private FoodService foodService;

    public CartService(FoodService foodService) {
        this.cart = new Cart();
        this.foodService = foodService;
    }

    public Cart getCart() {
        return cart;
    }

    /**
     * Adds a food item to the cart by food ID.
     */
    public void addToCart(Scanner scanner) {
        try {
            int foodId = InputValidator.readInt(scanner, "Enter Food ID to add to cart: ");
            if (foodId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid number.");
            }
            FoodItem item = foodService.findFoodById(foodId);
            if (item == null) {
                throw new FoodNotFoundException("Food item with ID " + foodId + " not found.");
            }
            if (!item.isAvailable()) {
                System.err.println("✗ '" + item.getFoodName() + "' is currently out of stock.");
                return;
            }
            int qty = InputValidator.readInt(scanner, "Enter quantity: ");
            if (qty == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid quantity.");
            }
            InputValidator.validateQuantity(qty);
            cart.addItem(item, qty);
            System.out.println("✓ " + item.getFoodName() + " (x" + qty + ") added to cart.");
        } catch (FoodNotFoundException | InsufficientStockException | InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    /**
     * Removes a food item from the cart by food ID.
     */
    public void removeFromCart(Scanner scanner) {
        try {
            if (cart.isEmpty()) {
                throw new EmptyCartException("Your cart is empty. Nothing to remove.");
            }
            int foodId = InputValidator.readInt(scanner, "Enter Food ID to remove from cart: ");
            if (foodId == Integer.MIN_VALUE) {
                throw new InvalidInputException("Please enter a valid number.");
            }
            boolean removed = cart.removeItem(foodId);
            if (removed) {
                System.out.println("✓ Item removed from cart.");
            } else {
                System.err.println("✗ That item is not in your cart.");
            }
        } catch (EmptyCartException | InvalidInputException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    /**
     * Displays the cart contents.
     */
    public void viewCart() {
        try {
            cart.viewCart();
        } catch (EmptyCartException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    /**
     * Checks if the cart is empty.
     */
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Clears the cart.
     */
    public void clearCart() {
        cart.clear();
    }
}
