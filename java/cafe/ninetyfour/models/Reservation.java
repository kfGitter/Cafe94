package cafe.ninetyfour.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cafe.ninetyfour.enums.ReservationStatus;

/**
 * Represents a reservation in the cafe system.
 * This is an abstract class that serves as
 * a base for different types of reservations.
 * Provides common functionality for booking and event reservations.
 */
public abstract class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static int nextReservationId = 1;
    protected final int reservationId;
    protected int customerId;
    protected LocalDateTime startTime;
    // Duration in minutes
    protected int duration;
    protected List<Table> tables;
    protected ReservationStatus status;
    protected LocalDateTime creationTime;


    /**
     * Constructs a new Reservation with basic details.
     * Not instantiate directly, use subclasses like Booking
     *
     * @param customerId the ID of the customer making the reservation
     * @param startTime  the starting time of the reservation
     * @param duration   the duration in minutes
     */
    public Reservation(int customerId, LocalDateTime startTime, int duration) {
        this.creationTime = LocalDateTime.now();
        this.reservationId = nextReservationId++;
        this.customerId = customerId;
        this.startTime = startTime;
        this.duration = duration;
        this.tables = new ArrayList<>();
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Abstract method to approve the reservation.
     *
     * @return true if approval was successful
     */
    public abstract boolean approve();

    /**
     * Abstract method to cancel the reservation.
     */
    public abstract void cancel();


    //  Common Methods

    /**
     * Assigns a table to this reservation.
     *
     * @param table the table to assign
     * @return true if table was successfully assigned
     */
    public boolean addTable(Table table) {
        if (table.assignToReservation(this)) {
            tables.add(table);
            return true;
        }
        return false;
    }


    /**
     * Sends a notification message to the customer.
     *
     * @param message the notification message
     */
    public void notifyCustomer(String message) {
        System.out.println("Notification to customer " + customerId + ": " + message);
    }

    public static void setNextReservationId(int id) {
        nextReservationId = id;
    }


    public int getCustomerId() {
        return customerId;
    }


    public int getReservationId() {
        return reservationId;
    }

    /**
     * Gets the reservation start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }


    /**
     * Gets the reservation duration in minutes.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }


    /**
     * Gets the list of assigned tables.
     *
     * @return list of tables
     */
    public List<Table> getTables() {
        return tables;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Calculates the end time of the reservation.
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }
}

