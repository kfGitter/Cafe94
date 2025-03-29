// File: src/main/java/com/cafe94/domain/DeliveryOrder.java
package com.cafe94.domain;

import com.cafe94.enums.OrderStatus;
import com.cafe94.util.ValidationUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
// No need to import Serializable, as it's inherited from Order

/**
 * Represents an order placed for delivery.
 * Extends the base Order class,
 * // adding delivery-specific details like address and driver.
 */
public class DeliveryOrder extends Order {

    // Added serialVersionUID because this class adds
    // state to a Serializable superclass
    private static final long serialVersionUID = 1L;

    /** Delivery address. Mandatory and immutable for this order type. */
    // Made deliveryAddress final
    private final String deliveryAddress;

    /** Estimated time of delivery. Optional. */
    private LocalTime estimatedDeliveryTime; // Or LocalDateTime

    /** ID of the driver assigned to this delivery. 0 indicates unassigned. */
    private int assignedDriverID = 0; // Default to unassigned

    /**
     * Constructs a new DeliveryOrder.
     *
     * @param orderID         Unique ID (0 for new entities) or existing ID.
     * @param items           List of items (non-null, non-empty).
     * @param customerID      ID of the customer placing the order
     * (must be positive).
     * @param deliveryAddress Delivery address (non-null, non-blank).
     * @param initialStatus   The starting status
     * (e.g., PENDING_CONFIRMATION or CONFIRMED).
     * @throws NullPointerException if deliveryAddress is null.
     * @throws IllegalArgumentException if deliveryAddress is blank.
     */
    public DeliveryOrder(int orderID, List<Item> items, int customerID,
    String deliveryAddress, OrderStatus initialStatus) {
        super(orderID, items, customerID, initialStatus);
        
        // Validate and set final deliveryAddress directly
        // Assumes ValidationUtils.requireNonBlank exists
        this.deliveryAddress =
        ValidationUtils.requireNonBlank(deliveryAddress, "Delivery address");
        
        // estimatedDeliveryTime is not set initially
        // assignedDriverID defaults to 0 (unassigned)
    }

    // --- Getters ---

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public int getAssignedDriverID() {
        return assignedDriverID;
    }

    // --- Setters ---

    // Removed setDeliveryAddress setter as field is now final

    /**
     * Sets the estimated delivery time and updates the last updated timestamp.
     * @param estimatedDeliveryTime The estimated time (can be null).
     */
    public void setEstimatedDeliveryTime(LocalTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        // Update lastUpdatedTimestamp as this is a modification to
        // the order details
        this.lastUpdatedTimestamp = LocalDateTime.now();
    }

    /**
     * Assigns a driver to this delivery order.
     * Service layer should validate driverID corresponds
     * to an actual DRIVER user.
     * @param driverID The ID of the assigned driver (must be > 0).
     * @throws IllegalArgumentException if driverID is not positive.
     */
    public void assignDriver(int driverID) {
        // TODO: Validate driverID exists and is a DRIVER (Service Layer)
        if (driverID <= 0) {
            throw new IllegalArgumentException(
                "Assigned Driver ID must be positive. Provided: " + driverID);
        }
        this.assignedDriverID = driverID;
        // NOTE: Service layer should handle any status change
        // logic upon assignment.
        // Update timestamp if assigning a driver is considered a
        // status-affecting update
        this.lastUpdatedTimestamp = LocalDateTime.now();
    }

    // --- Standard Methods ---

    @Override
    public String toString() {
         // Use string replacement for cleaner integration with super.toString()
        return super.toString().replaceFirst("]$", "")
               + ", Address='" + deliveryAddress + '\'' 
               + ", DriverID=" +
               (assignedDriverID > 0 ? assignedDriverID : "Unassigned")
               + (estimatedDeliveryTime != null ? ", EstTime=" +
               estimatedDeliveryTime : "") + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
         // Check specific class type, not just instanceof Order
        if (o == null || getClass() != o.getClass()) return false;
        // Check base Order equality first
        if (!super.equals(o)) return false;
        DeliveryOrder that = (DeliveryOrder) o;
        // Compare DeliveryOrder specific fields
        return assignedDriverID == that.assignedDriverID &&
               Objects.equals(deliveryAddress, that.deliveryAddress) &&
               Objects.equals(estimatedDeliveryTime,
               that.estimatedDeliveryTime);
    }

    @Override
    public int hashCode() {
         // Combine hashcode from base Order with DeliveryOrder specific fields
        return Objects.hash(super.hashCode(), deliveryAddress,
        estimatedDeliveryTime, assignedDriverID);
    }
}