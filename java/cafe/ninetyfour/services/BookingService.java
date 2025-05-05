package cafe.ninetyfour.services;

import java.util.*;
import java.io.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.models.Booking;
import cafe.ninetyfour.models.Reservation;
import cafe.ninetyfour.models.Table;
import cafe.ninetyfour.models.User;

/**
 * Service for creating, saving, approving, and managing table bookings.
 */
public class BookingService {
    private static final Logger logger = Logger.getLogger
            (BookingService.class.getName());

    private static final String BOOKINGS_FILE = "data/bookings.dat";
    private final Map<Integer, Booking> bookingsById = new HashMap<>();
    private List<Booking> bookings;
    private TableManager tableManager;  // Using TableManager instead of TableService
    private int nextBookingId = 1;


    /**
     * Constructor for BookingService.
     * Initializes the bookings list and loads existing bookings from file.
     *
     * @param tableManager the TableManager instance to manage tables
     */
    public BookingService(TableManager tableManager) {
        this.bookings = new ArrayList<>();
        this.tableManager = tableManager;
        loadBookings();
    }

    // Persistence Methods

    /**
     * Saves the current bookings to a file.
     *
     * @throws ServiceException if an error occurs during saving
     */
    public synchronized void saveBookings() throws ServiceException {
        try {
            new File("data").mkdirs();
            File tempFile = new File(BOOKINGS_FILE + ".tmp");

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(tempFile))) {
                oos.writeObject(new ArrayList<>(bookings));
            }

            File dataFile = new File(BOOKINGS_FILE);
            if (dataFile.exists() && !dataFile.delete()) {
                throw new ServiceException("Could not delete old bookings file");
            }
            if (!tempFile.renameTo(dataFile)) {
                throw new ServiceException("Could not rename temporary bookings file");
            }
        } catch (IOException e) {
            throw new ServiceException("Failed to save bookings", e);
        }
    }

    /**
     * Loads bookings from a file.
     * If the file exists, it reads the bookings and sets the next booking ID.
     */
    @SuppressWarnings("unchecked")
    private synchronized void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                List<Booking> loaded = (List<Booking>) ois.readObject();

                // Find max ID and set nextId
                int maxId= loaded.stream()
                        .mapToInt(Booking::getReservationId)
                        .max()
                        .orElse(0) + 1;
                //id
                this.nextBookingId = maxId + 1;
                User.setNextUserId(maxId + 1);

                // Replace current bookings
                this.bookings = new ArrayList<>(loaded);

            } catch (Exception e) {
                System.err.println("Error loading bookings: " + e.getMessage());
            }
        }
    }

    /**
     * Reloads bookings from the file and clears the current list.
     *
     * @throws ServiceException if an error occurs during reloading
     */
    public synchronized void saveAndReload() throws ServiceException {
        saveBookings();
        bookings.clear();
        loadBookings();
    }


    /**
     * Creates a new booking with default duration
     */
    public Booking createBooking(int customerId, LocalDateTime startTime,
                                 int numberOfGuests) throws ServiceException {
        try {
            if (numberOfGuests <= 0) {
                throw new ServiceException("Number of guests must be positive");
            }
            if (startTime.isBefore(LocalDateTime.now())) {
                throw new ServiceException("Booking time cannot be in the past");
            }

            Booking booking = new Booking(customerId, startTime, numberOfGuests);
            bookings.add(booking);
            saveBookings();
            return booking;
        } catch (Exception e) {
            logger.severe("Error loading booking: " + e.getMessage());
            throw new ServiceException("Failed to create booking", e);
        }
    }


    /**
     * Creates a new booking with custom duration
     */
    public Booking createBooking(int customerId, LocalDateTime startTime,
                                 int duration, int numberOfGuests) throws ServiceException {
        try {
            if (numberOfGuests <= 0) {
                throw new ServiceException("Number of guests must be positive");
            }
            if (startTime.isBefore(LocalDateTime.now())) {
                throw new ServiceException("Booking time cannot be in the past");
            }

            Booking booking = new Booking(customerId, startTime, duration, numberOfGuests);
            bookings.add(booking);
            saveBookings();
            return booking;
        } catch (Exception e) {
            logger.severe("Error loading booking: " + e.getMessage());
            throw new ServiceException("Failed to create booking", e);
        }
    }

    /**
     * Creates a new booking with custom duration and tables
     */
    // Add method to find bookings by date range
    public List<Booking> getBookingsBetween(LocalDateTime start, LocalDateTime end) {
        return bookings.stream()
                .filter(b -> !b.getStartTime().isBefore(start) && !b.getStartTime().isAfter(end))
                .toList();
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