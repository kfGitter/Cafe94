package cafe94.services;

import java.util.*;
import java.time.LocalDateTime;

import cafe94.models.Booking;
import cafe94.models.Table;

public class BookingService {
    private List<Booking> bookings;
    private TableManager tableManager;  // Using TableManager instead of TableService

    public BookingService(TableManager tableManager) {
        this.bookings = new ArrayList<>();
        this.tableManager = tableManager;
    }

    /**
     * Creates a new booking with standard duration (60 minutes)
     */
    public Booking createBooking(int customerId, LocalDateTime startTime, int numberOfGuests) {
        Booking booking = new Booking(customerId, startTime, numberOfGuests);
        bookings.add(booking);
        return booking;
    }

    /**
     * Creates a new booking with custom duration
     */
    public Booking createBooking(int customerId, LocalDateTime startTime, int duration, int numberOfGuests) {
        Booking booking = new Booking(customerId, startTime, duration, numberOfGuests);
        bookings.add(booking);
        return booking;
    }

    /**
     * Assigns a table to a booking after checking capaci
     */
    public boolean assignTableToBooking(int bookingId, int tableId) {
        Booking booking = findBookingById(bookingId);
        Table table = tableManager.getTableById(tableId);  // Using TableManager's method

        if (booking != null && table != null) {
            return booking.addTable(table);
        }
        return false;
    }

    /**
     * Approves a booking if it has tables assigned
     */
    public boolean approveBooking(int bookingId) {
        Booking booking = findBookingById(bookingId);
        return booking != null && booking.approve();
    }

    /**
     * Cancels a booking and releases all assigned tables
     */
    public void cancelBooking(int bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking != null) {
            booking.cancel();
        }
    }

    /**
     * Finds a booking by its ID
     */
    public Booking findBookingById(int bookingId) {
        return bookings.stream()
                .filter(b -> b.getReservationId() == bookingId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all bookings in the system
     */
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Gets all bookings for a specific customer
     */
    public List<Booking> getCustomerBookings(int customerId) {
        List<Booking> customerBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getCustomerId() == customerId) {
                customerBookings.add(booking);
            }
        }
        return customerBookings;
    }
}