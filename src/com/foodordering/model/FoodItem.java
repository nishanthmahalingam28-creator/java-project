package com.foodordering.model;

/**
 * Represents a food item in the menu.
 * Demonstrates ENCAPSULATION (private fields, getters/setters).
 */
public class FoodItem {

    private int foodId;
    private String foodName;
    private String category;
    private double price;
    private int availableQuantity;
    private String description;
    private boolean available;

    public FoodItem(int foodId, String foodName, String category, double price,
                    int availableQuantity, String description) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.category = category;
        this.setPrice(price);
        this.setAvailableQuantity(availableQuantity);
        this.description = description;
        this.available = availableQuantity > 0;
    }

    // --- Getters and setters with validation ---

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.availableQuantity = availableQuantity;
        this.available = availableQuantity > 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    /**
     * Displays the food item in a formatted line.
     */
    public void displayFoodItem() {
        String status = available ? "Available" : "Out of Stock";
        System.out.printf("%-6d %-20s %-15s ₹%-8.2f %-8d %s%n",
                foodId, foodName, category, price, availableQuantity, status);
    }

    @Override
    public String toString() {
        return foodId + "," + foodName + "," + category + "," + price + "," +
                availableQuantity + "," + description + "," + available;
    }
}
