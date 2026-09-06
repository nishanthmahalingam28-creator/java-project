package com.foodordering.model;

/**
 * Customer subclass of User.
 * Demonstrates INHERITANCE and METHOD OVERRIDING.
 */
public class Customer extends User {

    private String address;

    public Customer(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Overrides the abstract method in User.
     * Demonstrates METHOD OVERRIDING with @Override annotation.
     */
    @Override
    public void displayUserDetails() {
        System.out.println("========================================");
        System.out.println("          CUSTOMER PROFILE");
        System.out.println("========================================");
        System.out.println("Customer ID : " + getUserId());
        System.out.println("Name        : " + getName());
        System.out.println("Email       : " + getEmail());
        System.out.println("Phone       : " + getPhone());
        if (address != null && !address.isEmpty()) {
            System.out.println("Address     : " + address);
        }
        System.out.println("========================================");
    }
}
