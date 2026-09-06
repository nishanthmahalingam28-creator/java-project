package com.foodordering.service;

import com.foodordering.model.FoodItem;
import com.foodordering.util.FileManager;
import com.foodordering.exception.FoodNotFoundException;
import com.foodordering.exception.InvalidInputException;
import com.foodordering.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Service class for managing food items.
 * Demonstrates COLLECTIONS (ArrayList, HashMap), GENERICS, METHOD OVERLOADING,
 * and STREAM API.
 */
public class FoodService {

    private List<FoodItem> foodItems;
    private Map<Integer, FoodItem> foodMap;
    private int nextFoodId;

    public FoodService() {
        this.foodItems = new ArrayList<>();
        this.foodMap = new HashMap<>();
        this.nextFoodId = 1;
    }

    /**
     * Loads food items from file, or creates sample data if no file exists.
     */
    public void loadFoodItems() {
        List<FoodItem> loaded = FileManager.loadFoodItems();
        if (loaded != null && !loaded.isEmpty()) {
            foodItems = loaded;
            foodMap.clear();
            for (FoodItem f : foodItems) {
                foodMap.put(f.getFoodId(), f);
                if (f.getFoodId() >= nextFoodId) {
                    nextFoodId = f.getFoodId() + 1;
                }
            }
        } else {
            createSampleFoodItems();
        }
    }

    /**
     * Creates 12 sample food items.
     */
    private void createSampleFoodItems() {
        addFoodItem(new FoodItem(nextFoodId++, "Chicken Biryani", "Main Course", 150.0, 20, "Aromatic basmati rice with spiced chicken"));
        addFoodItem(new FoodItem(nextFoodId++, "Veg Biryani", "Main Course", 120.0, 15, "Fragrant rice with mixed vegetables"));
        addFoodItem(new FoodItem(nextFoodId++, "Fried Rice", "Main Course", 100.0, 25, "Wok-tossed rice with vegetables"));
        addFoodItem(new FoodItem(nextFoodId++, "Noodles", "Main Course", 90.0, 30, "Hakka noodles with vegetables"));
        addFoodItem(new FoodItem(nextFoodId++, "Burger", "Snacks", 80.0, 40, "Crispy veggie burger with cheese"));
        addFoodItem(new FoodItem(nextFoodId++, "Pizza", "Snacks", 200.0, 15, "Margherita pizza with fresh basil"));
        addFoodItem(new FoodItem(nextFoodId++, "Sandwich", "Snacks", 60.0, 35, "Grilled vegetable sandwich"));
        addFoodItem(new FoodItem(nextFoodId++, "French Fries", "Snacks", 50.0, 50, "Crispy golden fries with dip"));
        addFoodItem(new FoodItem(nextFoodId++, "Fresh Juice", "Beverages", 80.0, 30, "Freshly squeezed orange juice"));
        addFoodItem(new FoodItem(nextFoodId++, "Tea", "Beverages", 20.0, 100, "Hot masala chai"));
        addFoodItem(new FoodItem(nextFoodId++, "Coffee", "Beverages", 30.0, 80, "Freshly brewed filter coffee"));
        addFoodItem(new FoodItem(nextFoodId++, "Ice Cream", "Desserts", 70.0, 25, "Vanilla ice cream with chocolate sauce"));
        saveFoodItems();
    }

    /**
     * Saves food items to file.
     */
    public void saveFoodItems() {
        FileManager.saveFoodItems(foodItems);
    }

    // --- METHOD OVERLOADING: addFoodItem ---

    /**
     * Adds a food item to the menu.
     * This overloaded version accepts a FoodItem object.
     * Demonstrates METHOD OVERLOADING.
     */
    public void addFoodItem(FoodItem foodItem) {
        foodItems.add(foodItem);
        foodMap.put(foodItem.getFoodId(), foodItem);
    }

    /**
     * Adds a food item to the menu.
     * This overloaded version accepts individual parameters.
     * Demonstrates METHOD OVERLOADING.
     */
    public FoodItem addFoodItem(String name, String category, double price,
                                int quantity, String description) throws InvalidInputException {
        InputValidator.validateName(name);
        InputValidator.validatePrice(price);
        InputValidator.validateQuantity(quantity);
        FoodItem item = new FoodItem(nextFoodId++, name.trim(), category.trim(),
                price, quantity, description.trim());
        addFoodItem(item);
        saveFoodItems();
        return item;
    }

    /**
     * Updates a food item's details.
     */
    public FoodItem updateFoodItem(int foodId, String name, String category,
                                   double price, int quantity, String description)
            throws FoodNotFoundException, InvalidInputException {
        FoodItem item = findFoodById(foodId);
        if (item == null) {
            throw new FoodNotFoundException("Food item with ID " + foodId + " not found.");
        }
        InputValidator.validateName(name);
        InputValidator.validatePrice(price);
        InputValidator.validateQuantity(quantity);
        item.setFoodName(name.trim());
        item.setCategory(category.trim());
        item.setPrice(price);
        item.setAvailableQuantity(quantity);
        item.setDescription(description.trim());
        saveFoodItems();
        return item;
    }

    /**
     * Deletes a food item by ID.
     */
    public boolean deleteFoodItem(int foodId) throws FoodNotFoundException {
        FoodItem item = findFoodById(foodId);
        if (item == null) {
            throw new FoodNotFoundException("Food item with ID " + foodId + " not found.");
        }
        foodItems.remove(item);
        foodMap.remove(foodId);
        saveFoodItems();
        return true;
    }

    /**
     * Finds a food item by ID.
     */
    public FoodItem findFoodById(int foodId) {
        return foodMap.get(foodId);
    }

    // --- METHOD OVERLOADING: searchFood ---

    /**
     * Searches food items by name (case-insensitive).
     * Demonstrates METHOD OVERLOADING and STREAM API (filter).
     */
    public List<FoodItem> searchFood(String name) {
        return foodItems.stream()
                .filter(f -> f.getFoodName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Searches food items below a given maximum price.
     * Demonstrates METHOD OVERLOADING and STREAM API (filter).
     */
    public List<FoodItem> searchFood(double maxPrice) {
        return foodItems.stream()
                .filter(f -> f.getPrice() <= maxPrice)
                .sorted((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Filters food items by category using Stream API.
     */
    public List<FoodItem> filterByCategory(String category) {
        return foodItems.stream()
                .filter(f -> f.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Sorts food items by price (ascending) using Stream API.
     */
    public List<FoodItem> sortByPrice() {
        return foodItems.stream()
                .sorted((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Returns all food items.
     */
    public List<FoodItem> getAllFoodItems() {
        return foodItems;
    }

    /**
     * Displays all food items in a formatted table.
     */
    public void displayAllFoodItems() {
        if (foodItems.isEmpty()) {
            System.out.println("No food items available.");
            return;
        }
        System.out.println("========================================");
        System.out.println("             FOOD MENU");
        System.out.println("========================================");
        System.out.printf("%-6s %-20s %-15s %-10s %-8s %s%n",
                "ID", "Name", "Category", "Price", "Qty", "Status");
        System.out.println("----------------------------------------");
        for (FoodItem item : foodItems) {
            item.displayFoodItem();
        }
        System.out.println("========================================");
    }

    /**
     * Displays search results.
     */
    public void displaySearchResults(List<FoodItem> results) {
        if (results.isEmpty()) {
            System.out.println("✗ No matching food items found.");
            return;
        }
        System.out.println("========================================");
        System.out.println("          SEARCH RESULTS");
        System.out.println("========================================");
        System.out.printf("%-6s %-20s %-15s %-10s %-8s %s%n",
                "ID", "Name", "Category", "Price", "Qty", "Status");
        System.out.println("----------------------------------------");
        for (FoodItem item : results) {
            item.displayFoodItem();
        }
        System.out.println("========================================");
    }
}
