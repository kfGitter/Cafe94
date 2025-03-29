// File: src/main/java/com/cafe94/domain/TakeAwayOrder.java
package com.cafe94.domain;

// Assuming OrderStatus enum exists
import com.cafe94.enums.OrderStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents an order placed by a customer for collection (takeaway).
 * Extends the base Order class, adding a required pickup time.
 * Implements Serializable for persistence.
 */

 // Ensure Serializable
public class TakeAwayOrder extends Order implements Serializable {

    // Consistent ID
    private static final long serialVersionUID = 1L;

    /** The time the customer requested to pick up the order. Mandatory. */
    private LocalTime pickupTime;

    /**
     * Constructs a new TakeAwayOrder.
     *
     * @param orderID       Unique ID (0 for new entities) or existing ID.
     * @param items         List of items (non-null, non-empty).
     * @param customerID    ID of the customer placing the order
     * (must be positive).
     * @param initialStatus The starting status (e.g., PENDING_CONFIRMATION
     * or CONFIRMED).
     * @param pickupTime    The requested time for pickup (non-null).
     * @throws NullPointerException if pickupTime is null.
     * @throws IllegalArgumentException if items list is empty or
     * customerID invalid (checked by super).
     */
    public TakeAwayOrder(int orderID, List<Item> items, int customerID,
    OrderStatus initialStatus, LocalTime pickupTime) {
        // Call base constructor
        super(orderID, items, customerID, initialStatus);
        // Use setter for validation, which now also updates timestamp
        this.setPickupTime(pickupTime);
        // Ensure timestamp is set correctly even if setter
        // didn't change it initially
        this.lastUpdatedTimestamp = LocalDateTime.now();
    }

    // --- Getters ---

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    // --- Setters ---

    /**
     * Sets or updates the pickup time for the takeaway
     * order and updates the last updated timestamp.
     * @param pickupTime The requested pickup time (must not be null).
     * @throws NullPointerException if pickupTime is null.
     */
    public void setPickupTime(LocalTime pickupTime) {
        Objects.requireNonNull(pickupTime,
        "Pickup time cannot be null for a takeaway order.");
        // Optional validation (e.g., future time, within business hours)
        // belongs in Service layer
        
        // Check if value is actually changing before updating timestamp
        if (!Objects.equals(this.pickupTime, pickupTime)) {
            this.pickupTime = pickupTime;
            // Update lastUpdatedTimestamp as this is a
            // modification to the order details
            this.lastUpdatedTimestamp = LocalDateTime.now();
        }
    }

    // --- Standard Methods ---

    @Override
    public String toString() {
        // Use string replacement for cleaner integration with super.toString()
        return super.toString().replaceFirst("]$", "")
               + ", PickupTime=" + pickupTime + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
         // Check specific class type, not just instanceof Order
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Check base Order equality first
        TakeAwayOrder that = (TakeAwayOrder) o;
        // Compare TakeAwayOrder specific fields
        return Objects.equals(pickupTime, that.pickupTime);
    }

    @Override
    public int hashCode() {
        // Combine hash from Order superclass with TakeAwayOrder
        // specific fields
        return Objects.hash(super.hashCode(), pickupTime);
    }
}