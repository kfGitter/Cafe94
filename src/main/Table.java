// File: src/main/java/com/cafe94/domain/Table.java
package com.cafe94.domain;

// Assuming enum exists
import com.cafe94.enums.TableStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a dining table within the Cafe94 restaurant.
 * Includes table number, capacity, and current status.
 * Table number and capacity are immutable after creation.
 */
// Added implements Serializable
public class Table implements Serializable {

    // Added serialVersionUID
    private static final long serialVersionUID = 1L;

    /** Unique identifier or table number. Immutable. */
    // Made final
    private final int tableNumber;

    /** Maximum number of guests the table can accommodate. Immutable. */
    // Made final
    private final int capacity;

    /** Current status of the table (e.g., AVAILABLE, RESERVED,
     * OCCUPIED). Mutable. */
    private TableStatus status;

    /**
     * Constructs a new Table.
     *
     * @param tableNumber The unique table number (must be > 0).
     * @param capacity    The table capacity (must be > 0).
     * @param initialStatus The initial status (non-null, e.g., AVAILABLE).
     * @throws IllegalArgumentException if tableNumber or
     * capacity are not positive.
     * @throws NullPointerException if initialStatus is null.
     */
    public Table(int tableNumber, int capacity, TableStatus initialStatus) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException(
                "Table number must be positive. Provided: " + tableNumber);
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Table capacity must be positive. Provided: " + capacity);
        }
        // Use requireNonNull for consistency
        this.status = Objects.requireNonNull(initialStatus,
        "Initial table status cannot be null.");

        // Set final fields
        this.tableNumber = tableNumber;
        this.capacity = capacity;
    }

    // --- Getters ---

    public int getTableNumber() {
        return tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    // --- Setters ---

    /**
     * Updates the status of the table.
     * Service layer should handle side effects (logging, notifications).
     * @param newStatus The new status (non-null).
     * @throws NullPointerException if newStatus is null.
     */
    public void setStatus(TableStatus newStatus) {
        Objects.requireNonNull(newStatus, "New table status cannot be null.");
        // Consider adding check if status actually changed before assigning
        // if (this.status != newStatus) {
             this.status = newStatus;
             // TODO: Service layer may need to log this change or
             // trigger events.
        // }
    }

    // No setters for final fields tableNumber and capacity

    // --- Standard Methods ---

    @Override
    public String toString() {
        return "Table[Num=" + tableNumber +
               ", Cap=" + capacity +
               ", Status=" + status +
               ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
         // Use getClass() check for stricter type matching
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        // Equality is based solely on the unique table number
        return tableNumber == table.tableNumber;
    }

    @Override
    public int hashCode() {
        // Hash code based solely on the unique table number
        return Objects.hash(tableNumber);
    }
}