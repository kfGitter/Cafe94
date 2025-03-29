// File: src/main/java/com/cafe94/domain/EatInOrder.java
package com.cafe94.domain;

import com.cafe94.enums.OrderStatus;
import java.util.List;
import java.util.Objects;
// No need to import Serializable, as it's inherited from Order

/**
 * Represents an order placed by a customer dining within the restaurant.
 * Extends the base Order class, adding a reference to the table.
 */
public class EatInOrder extends Order {

    // Added serialVersionUID because this class
    // adds state to a Serializable superclass
    private static final long serialVersionUID = 1L;

    /** Reference to the table where the order is placed. Mandatory. */
    // Store table number, or potentially Table object reference
    private int tableNumber;

    /**
     * Constructs a new EatInOrder.
     *
     * @param orderID       Unique ID (0 for new entities) or existing ID.
     * @param items         List of items (non-null, non-empty).
     * @param customerID    ID of the customer placing the order
     * (must be positive).
     * @param tableNumber   The table number associated with the order
     * (must be > 0).
     * @param initialStatus The starting status
     * (e.g., PENDING_CONFIRMATION or CONFIRMED).
     * @throws IllegalArgumentException if tableNumber is not positive.
     */
    public EatInOrder(int orderID, List<Item> items, int customerID,
    int tableNumber, OrderStatus initialStatus) {
        super(orderID, items, customerID, initialStatus); // Call base constructor

        if (tableNumber <= 0) {
            throw new IllegalArgumentException(
                "Eat-in order must have a valid positive" +
                " table number. Provided: " + tableNumber);
        }
        this.tableNumber = tableNumber;
        // TODO: Consider validating tableNumber exists in Service layer.
        // TODO: Consider updating the associated Table's status to OCCUPIED in Service layer.
    }

    // --- Getters ---

    public int getTableNumber() {
        return tableNumber;
    }

    // --- Setters ---

    /**
     * Sets/Updates the table number associated with this order.
     * Use with caution after creation. Service layer must handle implications (e.g., table status updates).
     * @param tableNumber The table number (must be > 0).
     * @throws IllegalArgumentException if tableNumber is not positive.
     */
    public void setTableNumber(int tableNumber) {
         if (tableNumber <= 0) {
            throw new IllegalArgumentException(
                "Table number must be positive. Provided: " + tableNumber);
        }
        this.tableNumber = tableNumber;
        // NOTE: Service layer must handle consequences
        // (updating status of old/new Table objects).
    }

    // --- Standard Methods ---

    @Override
    public String toString() {
        // Use string replacement for cleaner integration with super.toString()
        return super.toString().replaceFirst("]$", "")
               + ", Table=" + tableNumber + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Check specific class type, not just instanceof Order
        if (o == null || getClass() != o.getClass()) return false;
        // Check base Order equality first
        if (!super.equals(o)) return false;
        EatInOrder that = (EatInOrder) o;
        // Compare EatInOrder specific fields
        return tableNumber == that.tableNumber;
    }

    @Override
    public int hashCode() {
        // Combine hashcode from base Order with EatInOrder specific fields
        return Objects.hash(super.hashCode(), tableNumber);
    }
}