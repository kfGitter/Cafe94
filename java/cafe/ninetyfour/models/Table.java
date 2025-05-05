package cafe.ninetyfour.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cafe.ninetyfour.enums.TableStatus;

/**
 * Represents a table in the cafe system.
 * Each table has a unique ID, capacity, status, and a list of reservations.
 * * It keeps track of reservations and manages its own assignment and release.
 */
public class Table implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int tableId;
    private final int capacity;
    private TableStatus status;
    private List<Reservation> reservations;

    /**
     * Constructor for creating a Table object.
     *
     * @param tableId  the unique ID of the table
     * @param capacity the seating capacity of the table
     */
    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = TableStatus.AVAILABLE;
        this.reservations = new ArrayList<>();
    }

    // Getters (no setters for final fields)
    public int getTableId() {
        return tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    // Business Logic Methods

    /**
     * Assigns a reservation to this table if available.
     *
     * @param reservation the reservation to assign
     * @return true if the reservation was successfully assigned, false otherwise
     */
    public boolean assignToReservation(Reservation reservation) {
        if (checkAvailability(reservation.getStartTime(), reservation.getDuration())) {
            reservations.add(reservation);
            status = TableStatus.RESERVED;
            return true;
        }
        return false;
    }


    /**
     * Releases the table, making it available for new reservations.
     */
    public void releaseTable() {
        this.status = TableStatus.AVAILABLE;
        this.reservations.removeIf(r ->
                r.getEndTime().isBefore(LocalDateTime.now()));
        System.out.println("Table " + tableId + " released");
    }

    /**
     * Checks if the table is available at the requested time.
     *
     * @param requestedStart    the requested start time
     * @param requestedDuration the requested duration in minutes
     * @return true if available, false otherwise
     */
    public boolean checkAvailability(LocalDateTime requestedStart,
                                     int requestedDuration) {
        for (Reservation reservation : reservations) {
            LocalDateTime reservedEnd = reservation.getEndTime();
            LocalDateTime requestedEnd = requestedStart.plusMinutes(requestedDuration);

            // Check for overlap
            if (requestedStart.isBefore(reservedEnd) &&
                    requestedEnd.isAfter(reservation.getStartTime())) {
                return false;
            }
        }
        return status == TableStatus.AVAILABLE;
    }


    /**
     * Returns a formatted string representation of the table.
     *
     * @return string with table details
     */
    @Override
    public String toString() {
        return String.format("Table %d (%d seats) - %s", tableId, capacity, status);
    }
}