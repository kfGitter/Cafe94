package com.cafe94.domain;

import com.cafe94.enums.BookingStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a customer booking for a table at Cafe94.
 */
// Added implements Serializable
public class Booking implements Serializable {

    // Added serialVersionUID
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the booking. */
    private int bookingID;

    /** ID of the customer making the booking. */
    private final int customerID; // Or User object reference

    /** ID of the table assigned to the booking
     * (might be assigned upon confirmation). */
    private int tableNumber = 0; // 0 indicates not yet assigned

    /** Date of the booking. */
    private LocalDate bookingDate;

    /** Time of the booking. */
    private LocalTime bookingTime;

    /** Number of guests for the booking. */
    private int numberOfGuests;

    /** Current status of the booking. */
    private BookingStatus status;

    /** Timestamp when the booking request was created. */
    private final LocalDateTime creationTimestamp;

     /** Optional notes related to the booking
      * (e.g., dietary requirements, special occasion). */
    private String notes;

    /**
     * Constructs a new Booking request.
     *
     * @param bookingID      Unique ID (0 for new entities) or existing ID.
     * @param customerID     ID of the customer.
     * @param bookingDate    Date of booking (non-null).
     * @param bookingTime    Time of booking (non-null).
     * @param numberOfGuests Number of guests (must be > 0).
     * @param initialStatus  Initial status (usually PENDING_APPROVAL).
     * @param notes          Optional notes (can be null).
     * @throws NullPointerException if date, time, or status is null.
     * @throws IllegalArgumentException if numberOfGuests is not positive.
     */
    public Booking(int bookingID, int customerID, LocalDate bookingDate,
    LocalTime bookingTime, int numberOfGuests, BookingStatus initialStatus,
    String notes) {
        // TODO: Validate customerID exists? (Service Layer)
        Objects.requireNonNull(bookingDate,
        "Booking date cannot be null.");
        Objects.requireNonNull(bookingTime,
        "Booking time cannot be null.");
        Objects.requireNonNull(initialStatus,
        "Initial booking status cannot be null.");
        if (numberOfGuests <= 0) {
            throw new IllegalArgumentException(
                "Number of guests must be positive.");
        }
        // TODO: Consider validating date/time is in the future?(Service Layer)

        this.bookingID = bookingID;
        this.customerID = customerID;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.numberOfGuests = numberOfGuests;
        this.status = initialStatus;
        this.creationTimestamp = LocalDateTime.now();
        this.notes = notes;
    }

    // --- Getters ---

    public int getBookingID() {
        return bookingID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public LocalDateTime getBookingDateTime() {
        // Convenience method
        if (bookingDate == null || bookingTime == null) return null;
        return LocalDateTime.of(bookingDate, bookingTime);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getNotes() {
        return notes;
    }

    // --- Setters ---

     /** * Sets the booking ID. Should typically only be called once
      * by the persistence layer after saving a new booking.
      * @param bookingID The assigned persistent ID.
      */
    // Changed visibility to protected
    protected void setBookingID(int bookingID) {
         // Add logic similar to User.setUserID if
         // needed to prevent changing non-zero IDs
         if (this.bookingID != 0 && this.bookingID != bookingID) {
             System.err.println("Warning: Attempting to change an"
             + " already assigned booking ID.");
             // Or throw new IllegalStateException("Cannot
             // change an already assigned persistent booking ID.");
         }
        this.bookingID = bookingID;
    }

    /**
     * Assigns a table to the booking, usually upon confirmation.
     * @param tableNumber The number of the assigned table (must be > 0).
     */
    public void assignTable(int tableNumber) {
        if (tableNumber <= 0) {
            throw new IllegalArgumentException(
                "Assigned table number must be positive.");
        }
        // TODO: Validate table exists and has sufficient capacity?
        // (Service Layer)
        this.tableNumber = tableNumber;
        // TODO: Consider updating associated Table status to RESERVED
        // (Service Layer)
    }

    /** * Updates the booking status. Service layer should handle side effects.
     * @param newStatus The new status (non-null). 
     */
    public void setStatus(BookingStatus newStatus) {
        Objects.requireNonNull(newStatus, "Booking status cannot be null.");
        this.status = newStatus;
         // TODO: Consider if status change should trigger other actions
         // (Service Layer: notifications, table status update).
    }

    /** * Updates booking details. Service layer must handle consequences
     * // (re-approval, etc.).
     * @param newDate New date for the booking (non-null).
     * @param newTime New time for the booking (non-null).
     * @param newGuestCount New number of guests (must be > 0).
     */
    public void updateDetails(LocalDate newDate, LocalTime newTime,
    int newGuestCount) {
        Objects.requireNonNull(newDate, "Booking date cannot be null.");
         Objects.requireNonNull(newTime, "Booking time cannot be null.");
         if (newGuestCount <= 0) {
            throw new IllegalArgumentException(
                "Number of guests must be positive.");
         }
         // TODO: Add validation for date/time in the future? (Service Layer)
         this.bookingDate = newDate;
         this.bookingTime = newTime;
         this.numberOfGuests = newGuestCount;
         // Service Layer must handle consequences:
         // - Reset status to PENDING_APPROVAL?
         // - Re-check table availability?
         // - Clear assigned tableNumber?
    }

     /** Sets or updates the booking notes. */
    public void setNotes(String notes) {
        // Consider trimming or validating notes if needed
        this.notes = notes;
    }

    // --- Standard Methods ---

    @Override
    public String toString() {
        // Format slightly for clarity
        return "Booking[ID=" + bookingID + ", CustID=" + customerID +
               ", DateTime=" + (getBookingDateTime() !=
               null ? getBookingDateTime().toString() : "N/A") +
               ", Guests=" + numberOfGuests + ", Status=" + status +
               (tableNumber > 0 ? ", Table=" + tableNumber : "") +
               (notes != null && !notes.isEmpty() ? ", Notes=Yes" : "") + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use getClass() for stricter type checking
        // if extending Booking is possible
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        // Rely on bookingID if assigned and non-zero for both
        if (bookingID != 0 && booking.bookingID != 0) {
            return bookingID == booking.bookingID;
        }
        // Fallback for unsaved bookings
        // (less reliable, using key booking details)
        // Use getBookingDateTime() which handles nulls
        return customerID == booking.customerID &&
               numberOfGuests == booking.numberOfGuests &&
               Objects.equals(getBookingDateTime(),
               booking.getBookingDateTime()) && status == booking.status;
    }

    @Override
    public int hashCode() {
         // Rely on bookingID if assigned and non-zero
        if (bookingID != 0) {
            return Objects.hash(bookingID);
        }
        // Fallback hashcode consistent with equals fallback
        return Objects.hash(customerID, getBookingDateTime(),
        numberOfGuests, status);
    }
}