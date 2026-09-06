package com.foodordering.interface_pkg;

import com.foodordering.model.Order;
import com.foodordering.model.Customer;

import java.util.List;

/**
 * Interface defining standard order-related operations.
 * Demonstrates the OOP concept of INTERFACES.
 */
public interface OrderOperations {

    /** Places a new order for the given customer. */
    Order placeOrder(Customer customer) throws Exception;

    /** Cancels an existing order by its order ID. */
    boolean cancelOrder(int orderId);

    /** Retrieves the order history for a given customer. */
    List<Order> getOrderHistory(Customer customer);

    /** Retrieves all orders (admin use). */
    List<Order> getAllOrders();
}
