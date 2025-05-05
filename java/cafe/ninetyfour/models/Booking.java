package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Represents a table booking reservation in the cafe system.
 * Extends the base Reservation class with guest count functionality.
 */
public class Booking extends Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int numberOfGuests;

    /**
     * Constructs a new Booking with standard 60-minute duration.
     *
     * @param customerId the ID of the customer making the booking
     * @param startTime the booking start time
     * @param numberOfGuests the number of guests (must be positive)
     * @throws IllegalArgumentException if guest count is invalid
     */
    public Booking(int customerId, LocalDateTime startTime, int numberOfGuests) {
        super(customerId, startTime, 60);
        if (numberOfGuests <= 0) {
            throw new IllegalArgumentException("Number of guests must be positive");
        }
        this.numberOfGuests = numberOfGuests;
    }

    /**
     * Constructs a new Booking with custom duration.
     *
     * @param customerId the ID of the customer making the booking
     * @param startTime the booking start time
     * @param duration the booking duration in minutes
     * @param numberOfGuests the number of guests
     */    public Booking(int customerId, LocalDateTime startTime, int duration, int numberOfGuests) {
        super(customerId, startTime, duration);
        this.numberOfGuests = numberOfGuests;
    }


    /**
     * Assigns a table to this booking after checking capacity.
     *
     * @param table the table to assign
     * @return true if table was successfully assigned
     * @throws IllegalArgumentException if table capacity is insufficient
     */
    @Override
    public boolean addTable(Table table) {
        if (table.getCapacity() < this.numberOfGuests) {
            throw new IllegalArgumentException(
                    String.format("Table %d (%d seats) too small for %d guests",
                            table.getTableId(),
                            table.getCapacity(),
                            this.numberOfGuests));
        }
        return super.addTable(table);
    }



    /**
     * Approves the booking if tables are assigned.
     * @return true if approval was successful
     */
    @Override
    public boolean approve() {
        if (tables.isEmpty()) {
            System.out.println("Cannot approve - no tables assigned");
            return false;
        }
        this.status = ReservationStatus.APPROVED;
        notifyCustomer("Booking confirmed!");
        return true;
    }


    /**
     * Cancels the booking and releases all tables.
     */
    @Override
    public void cancel() {
        // Release all tables
        for (Table table : tables) {
            table.releaseTable();
        }
        this.tables.clear();  // Clear the assigned tables
        this.status = ReservationStatus.CANCELLED;
        notifyCustomer("Your booking (ID: " + reservationId + ") has been cancelled");
    }


    /**
     * Returns the number of guests for this booking.
     * @return number of guests
     */
    public int getNumberOfGuests() {
        return numberOfGuests;
    }


    /**
     * Returns a detailed string representation of the booking.
     * @return formatted booking details
     */
    @Override
    public String toString() {
        return "Booking ID: " + reservationId +
                "\nCustomer ID: " + customerId +
                "\nStart Time: " + startTime +
                "\nDuration: " + duration + " minutes" +
                "\nStatus: " + status +
                "\nNumber of Guests: " + numberOfGuests +
                "\nTables: " + tables;
    }
}


