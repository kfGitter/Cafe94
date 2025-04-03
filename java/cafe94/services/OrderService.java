package cafe94.services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cafe94.enums.OrderStatus;
import cafe94.models.*;

public class OrderService {
    private final List<Order> allOrders;
    private final Map<Integer, Order> ordersById;
    private int nextOrderId = 1;

    public OrderService() {
        this.allOrders = new CopyOnWriteArrayList<>();
        this.ordersById = new ConcurrentHashMap<>();  // Thread-safe map
    }

    // Place an order with validation
    public synchronized Order placeOrder(Order order) {
        Objects.requireNonNull(order, "Order cannot be null");
        order.setOrderId(nextOrderId++);
        allOrders.add(order);
        ordersById.put(order.getOrderId(), order);
        return order;
    }

    // Optimized status tracking
    public OrderStatus trackStatus(int orderId) {
        Order order = ordersById.get(orderId);
        return order != null ? order.getStatus() : null;
    }

    // Get all orders (immutable view)
    public List<Order> getAllOrders() {
        return Collections.unmodifiableList(allOrders);
    }

    // Get orders by customer (using stream)
    public List<Order> getOrdersByCustomer(int customerId) {
        return allOrders.stream()
                .filter(order -> order.getCustomerId() == customerId)
                .toList();
    }

    public List<Order> getOrderById(int orderId) {
        return allOrders.stream()
                .filter(order -> order.getOrderId() == orderId)
                .toList();
    }

    // Optimized status update using map
    public boolean updateStatus(int orderId, OrderStatus newStatus) {
        Order order = ordersById.get(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
            return true;
        }
        return false;
    }

    // Display outstanding orders
    public void displayOutstandingOrders() {
        System.out.println("Outstanding Orders:");
        allOrders.stream()
                .filter(order -> order.getStatus() != OrderStatus.COMPLETED)
                .forEach(System.out::println);
    }

    // Process an order
    public boolean processOrder(int orderId) {
        Order order = ordersById.get(orderId);
        if (order != null) {
            order.processOrder();
            return true;
        }
        return false;
    }
}