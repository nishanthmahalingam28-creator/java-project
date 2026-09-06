package com.foodordering.model;

/**
 * Represents a single line item in a cart.
 * Demonstrates ENCAPSULATION and association with FoodItem.
 */
public class CartItem {

    private FoodItem foodItem;
    private int quantity;

    public CartItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the line total for this cart item.
     */
    public double getLineTotal() {
        return foodItem.getPrice() * quantity;
    }

    public void displayCartItem() {
        System.out.printf("%-6d %-20s %-15s ₹%-8.2f %-8d ₹%-8.2f%n",
                foodItem.getFoodId(),
                foodItem.getFoodName(),
                foodItem.getCategory(),
                foodItem.getPrice(),
                quantity,
                getLineTotal());
    }

    @Override
    public String toString() {
        return foodItem.getFoodId() + "," + foodItem.getFoodName() + "," +
                quantity + "," + getLineTotal();
    }
}
