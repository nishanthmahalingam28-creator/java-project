package com.foodordering.model;

/**
 * Admin subclass of User.
 * Demonstrates INHERITANCE and METHOD OVERRIDING.
 */
public class Admin extends User {

    private String role;

    public Admin(int userId, String name, String email, String phone, String password) {
        super(userId, name, email, phone, password);
        this.role = "Administrator";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Overrides the abstract method in User.
     * Demonstrates METHOD OVERRIDING with @Override annotation.
     */
    @Override
    public void displayUserDetails() {
        System.out.println("========================================");
        System.out.println("           ADMIN PROFILE");
        System.out.println("========================================");
        System.out.println("Admin ID    : " + getUserId());
        System.out.println("Name        : " + getName());
        System.out.println("Email       : " + getEmail());
        System.out.println("Phone       : " + getPhone());
        System.out.println("Role        : " + role);
        System.out.println("========================================");
    }
}
