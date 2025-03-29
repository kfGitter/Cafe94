// File: src/main/java/com/cafe94/domain/Order.java
package com.cafe94.domain;

// Assuming OrderStatus enum exists in com.cafe94.enums
import com.cafe94.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class representing an order placed at Cafe94.
 * Contains common details like ID, items, customer reference,
 * status, and timestamps.
 * Encapsulates basic state transition logic via behavior methods.
 * Concrete subclasses (e.g., DeliveryOrder, EatInOrder)
 * will specify order type details.
 * Implements Serializable to allow persistence via serialization.
 */

 // Implement Serializable
public abstract class Order implements Serializable {

    // Add serialVersionUID
    private static final long serialVersionUID = 1L;
    
    /** Unique identifier for the order.
     * ypically assigned by persistence layer. */
    private int orderID;

    /** List of items included in the order.
     * Should not be empty. */
    private List<Item> items;

    /** ID of the customer who placed the order. */
    // Consider using Customer object reference if feasible
    private int customerID;

    /** Current status of the order (e.g., PENDING, PREPARING, DELIVERED). */
    private OrderStatus status;

    /** Timestamp when the order was placed. */
    private LocalDateTime orderTimestamp;

    /** Timestamp for the last update to the order status. */
    protected LocalDateTime lastUpdatedTimestamp;

    /** Calculated total price of the order based on items. */
    private double totalPrice;

    /**
     * Protected constructor for use by subclasses.
     *
     * @param orderID       Unique ID (0 for new entities) or existing ID.
     * @param items         List of items (non-null, non-empty).
     * Defensive copy is made.
     * @param customerID    ID of the customer placing the order
     * (must be positive).
     * @param initialStatus The starting status of the order
     * (e.g., PENDING_CONFIRMATION).
     * @throws NullPointerException if items list or initialStatus is null.
     * @throws IllegalArgumentException if items list is empty or
     * customerID is not positive.
     */
    protected Order(int orderID, List<Item> items, int customerID,
    OrderStatus initialStatus) {
        Objects.requireNonNull(items, "Order items list cannot be null.");
        Objects.requireNonNull(initialStatus,
        "Initial order status cannot be null.");
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                "Order must contain at least one item.");
        }
        if (customerID <= 0) { // Basic validation for customer ID
            throw new IllegalArgumentException(
                "Valid Customer ID is required for an order. Provided: "
                + customerID);
        }

        this.orderID = orderID;
        this.items = new ArrayList<>(items);
        this.customerID = customerID;
        this.status = initialStatus;
        // Set creation time
        this.orderTimestamp = LocalDateTime.now();
        // Initially same as creation
        this.lastUpdatedTimestamp = this.orderTimestamp;
        // Calculate price upon creation
        this.totalPrice = calculateTotalPrice();
    }

    /**
     * Calculates the total price based on the items in the order.
     * @return The sum of the prices of all items.
     */
    private double calculateTotalPrice() {
        if (this.items == null) return 0.0;
        // Assumes Item class has a getPrice() method returning double
        return this.items.stream()
        // Avoid potential NullPointerException if list contains null items
                .filter(Objects::nonNull)
                .mapToDouble(Item::getPrice)
                .sum();
    }

    // --- Getters ---

    public int getOrderID() {
        return orderID;
    }

    /** @return An unmodifiable view of the items in the order. */
    public List<Item> getItems() {
        // Ensure list is never null before returning unmodifiable view
        return items == null ? Collections.emptyList() :
        Collections.unmodifiableList(items);
    }

    public int getCustomerID() {
        return customerID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public LocalDateTime getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public double getTotalPrice() {
        // Return pre-calculated price.
        return totalPrice;
    }

    // --- Setters / Modifiers ---

    /**
     * Sets the order ID. Primarily intended for use by the persistence layer.
     * Package-private access restricts usage.
     * @param orderID The unique ID assigned by persistence.
     */
    // Keep package-private or change to protected if needed b
    //  persistence helpers
    void setOrderID(int orderID) { 
        if (this.orderID != 0 && this.orderID != orderID) {
             // Log or handle attempt to change existing ID if necessary
             System.err.println(
                "Warning: Attempting to change assigned order ID "
                + this.orderID + " to " + orderID);
        }
        this.orderID = orderID;
    }

    /**
     * Updates the status of the order internally.
     * Use behavior methods for transitions.
     * @param newStatus The new status (must not be null).
     * @throws NullPointerException if newStatus is null.
     */
    // Changed to protected, primarily for use by updateStatusInternal
    protected void setStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "New order status cannot be null.");
        // Internal update only updates status field,
        // timestamp update moved to updateStatusInternal
        this.status = newStatus;
    }

    /**
     * Replaces the items in the order. Recalculates total price.
     * Use with caution. Service layer should handle business logic
     * consequences.
     * @param newItems New list of items (must not be null or empty).
     * @throws NullPointerException if newItems is null.
     * @throws IllegalArgumentException if newItems is empty.
     */
    public void updateItems(List<Item> newItems) {
        Objects.requireNonNull(newItems,
        "Order items list cannot be null when updating.");
        if (newItems.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot update order to have no items.");
        }
        // NOTE: Service layer must handle consequences: status reset,
        // re-approval etc.
        // Example check (could be moved to service layer):
        // if (this.status != OrderStatus.PENDING_CONFIRMATION &&
        // this.status != OrderStatus.CONFIRMED) {
        //     throw new IllegalStateException("Cannot modify items
        // when order status is " + this.status);
        // }
        this.items = new ArrayList<>(newItems);
        this.totalPrice = calculateTotalPrice();
        this.lastUpdatedTimestamp = LocalDateTime.now();
        // Potentially reset status here ONLY IF
        // this domain object is solely responsible
        // But better handled in service layer
        // this.status = OrderStatus.PENDING_CONFIRMATION;
        // Use proper logging
        System.out.println("Items updated for order: " + orderID);
    }

    // --- Behavior Methods for State Transitions ---

    /**
     * Marks the order as confirmed
     * (e.g., payment received or accepted by staff).
     * Only possible if the order is PENDING_CONFIRMATION.
     * @throws IllegalStateException if order is not in
     * PENDING_CONFIRMATION state.
     */
    public void confirmOrder() {
        if (this.status == OrderStatus.PENDING_CONFIRMATION) {
            updateStatusInternal(OrderStatus.CONFIRMED);
        } else {
            throw new IllegalStateException(
                "Order cannot be confirmed from status: " + this.status);
        }
    }

    /**
     * Marks the order as being prepared by the kitchen.
     * Only possible if the order is CONFIRMED.
     * @throws IllegalStateException if order is not in CONFIRMED state.
     */
    public void startPreparation() {
        if (this.status == OrderStatus.CONFIRMED) {
            updateStatusInternal(OrderStatus.PREPARING);
        } else {
            throw new IllegalStateException(
                "Order preparation cannot start from status: " + this.status);
        }
    }

    /**
     * Marks the order as ready (prepared and waiting for
     * delivery/collection/serving).
     * Only possible if the order is PREPARING.
     * @throws IllegalStateException if order is not in PREPARING state.
     */
    public void markAsReady() {
        if (this.status == OrderStatus.PREPARING) {
            updateStatusInternal(OrderStatus.READY);
        } else {
            throw new IllegalStateException(
                "Order cannot be marked as ready from status: " + this.status);
        }
    }

    /**
     * Marks the order as completed (final state after delivery, collection,
     * or serving).
     * Allowed from various states like READY, SERVED, COLLECTED, DELIVERED.
     * @throws IllegalStateException if order is already completed/cancelled
     * or in an invalid state.
     */
    public void completeOrder() {
        // Define states from which completion is allowed
        List<OrderStatus> completableStates = List.of(
            OrderStatus.READY,
            OrderStatus.SERVED,
            OrderStatus.COLLECTED,
            OrderStatus.DELIVERED
        );

        if (completableStates.contains(this.status)) {
            updateStatusInternal(OrderStatus.COMPLETED);
        } else if (this.status == OrderStatus.COMPLETED) {
             // Idempotent: Already completed, do nothing. Use proper logging.
             System.out.println("Order " + orderID + " is already completed.");
        } else {
            throw new IllegalStateException(
                "Order cannot be completed from status: " + this.status);
        }
    }

    /**
     * Cancels the order. Checks if cancellation is
     * allowed based on current status.
     * @throws IllegalStateException if order cannot be cancelled from
     * its current state.
     */
    public void cancelOrder() {
        // Define which statuses allow cancellation
        // (adjust based on business rules)
        List<OrderStatus> cancellableStates = List.of(
            OrderStatus.PENDING_CONFIRMATION,
            OrderStatus.CONFIRMED
            // Add OrderStatus.PREPARING only if allowed
            // (potentially with conditions handled in Service layer)
        );

        if (cancellableStates.contains(this.status)) {
            updateStatusInternal(OrderStatus.CANCELLED);
        } else if (this.status == OrderStatus.CANCELLED) {
            // Idempotent: Already cancelled, do nothing. Use proper logging.
             System.out.println("Order " + orderID + " is already cancelled.");
        } else {
            // Service layer might handle cancellation from other states
            // (e.g., PREPARING with approval)
            throw new IllegalStateException(
                "Order cannot be cancelled from status: " + this.status);
        }
    }

    /**
     * Internal helper to update status and timestamp consistently.
     * @param newStatus The new status to set (must not be null).
     */
    protected void updateStatusInternal(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus,
        "New status cannot be null for internal update.");
        if (this.status != newStatus) {
            // Use proper logging framework instead of System.out
            System.out.printf("Order %d: Status changing from %s to %s%n",
            this.orderID, this.status, newStatus);
            this.status = newStatus;
            this.lastUpdatedTimestamp = LocalDateTime.now();
            // Consider publishing a domain event here if
            // using event-driven architecture
        }
    }

    // --- Standard Object Methods ---
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" +
               "ID=" + orderID +
               ", CustID=" + customerID + 
               ", Status=" + status +
               ", Items=" + (items != null ? items.size() : 0) +
               ", Total=" + String.format("%.2f", totalPrice) +
               ", Placed=" + (orderTimestamp !=
               null ? orderTimestamp.toString() : "N/A") + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use instanceof check for abstract class,
        // subclasses should override if needed
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        // Use orderID for equality if assigned (non-zero) for both
        if (orderID != 0 && order.orderID != 0) {
            return orderID == order.orderID;
        }
        // Fallback for unsaved entities (less reliable)
        // Comparing mutable lists and timestamps can be tricky
        // for transient equality.
        // Using customerID and timestamp might be sufficient as
        // a business key proxy.
        return customerID == order.customerID &&
               Objects.equals(orderTimestamp, order.orderTimestamp);
               // Avoid comparing item lists directly in equals
               // if items or Item class are mutable
               // && Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        // Use orderID if assigned (non-zero)
        if (orderID != 0) {
            return Objects.hash(orderID);
        }
        // Fallback hashcode for unsaved entities, consistent with
        // equals fallback
        return Objects.hash(customerID, orderTimestamp);
        // Avoid including mutable fields like items list in hashcode
    }
}