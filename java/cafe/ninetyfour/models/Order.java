package cafe.ninetyfour.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import cafe.ninetyfour.enums.OrderStatus;
import cafe.ninetyfour.services.CustomerService;

/**
 * Abstract base class representing an order in the cafe system.
 * Provides common functionality for all order types
 * (eat-in, takeaway, delivery).
 */
public abstract class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    protected  int orderId;
    protected final int customerId;
    protected final List<Item> items;
    protected OrderStatus status;
    protected final LocalDateTime orderTime;
    protected final double totalPrice;
    protected boolean isApproved;


    /**
     * Constructs a new Order with the given customerd and items.
     *
     * @param customerId the ID of the customer placing the order
     * @param items the list of items in the order
     *              (cannot be null or empty)
     */
    public Order(int customerId, List<Item> items) {
        this.orderId = -1; // Default value, will be set later
        this.customerId = customerId;
        this.items = List.copyOf(items); // Defensive copy
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = calculateTotal();
        this.isApproved = false;
    }


    /**
     * Calculates the total price of all items in the order.
     *
     * @return the sum of all item prices
     */
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }

    /**
     * Updates the status of the order.
     *
     * @param status the new status to set
     * @throws IllegalArgumentException if status is null
     */
    public void updateStatus(OrderStatus status) {
        if (status == null) throw new IllegalArgumentException
                ("Status cannot be null");
        this.status = status;
    }

    /**
     * Approves the order and sets its status to IN_PROGRESS.
     */
    public void approveOrder() {
        this.isApproved = true;
        updateStatus(OrderStatus.IN_PROGRESS);
    }

    // Getters
    /**
     * Retrieves the customer associated with this order.
     *
     * @param service the CustomerService to use for fetching customer details
     * @return the Customer object associated with this order
     */
    public Customer getCustomer(CustomerService service) {
        return service.findCustomerById(customerId);
    }

    /**
     * Retrieves the order time.
     *
     * @return the LocalDateTime when the order was placed
     */
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    /**
     * Retrieves the status of the order.
     *
     * @return the current status of the order
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Retrieves the order ID.
     *
     * @return the unique identifier of the order
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Retrieves the customer ID.
     *
     * @return the ID of the customer who placed the order
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Retrieves the list of items in the order.
     *
     * @return an unmodifiable list of items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Checks if the order is approved.
     *
     * @return true if the order is approved, false otherwise
     */

    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Retrieves the total price of the order.
     *
     * @return the total price of all items in the order
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retrieves the order time as a formatted string.
     *
     * @return the order time in "yyyy-MM-dd HH:mm:ss" format
     */
    // Setters
    public void setOrderId(int orderId) {
        if (orderId <= 0) throw new IllegalArgumentException
                ("Invalid order Id");
        this.orderId = orderId;
    }

    /**
     * Abstract method for order-specific processing logic.
     * Must be implemented by concrete order types.
     */
    public abstract void processOrder();


    /**
     * Returns a string representation of the order.
     * @return a formatted string with order details
     */
    @Override
    public String toString() {
        return String.format("Order #%d (Customer: %d) - %s - $%.2f",
                orderId, customerId, status, totalPrice);
    }

}