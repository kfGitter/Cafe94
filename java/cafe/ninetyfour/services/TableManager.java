package cafe.ninetyfour.services;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.models.Reservation;
import cafe.ninetyfour.models.Table;
import cafe.ninetyfour.models.*;

/**
 * TableManager class is responsible for managing tables in the cafe,
 * including finding available tables, assigning tables to reservations,
 * and retrieving table information.
 */
public class TableManager {
    private static final Logger logger = Logger.getLogger(TableManager.class.getName());

    private final List<Table> tables;

    /**
     * Constructor for TableManager.
     * Initializes the list of tables.
     */
    public TableManager() {
        this.tables = initializeTables();
    }

    // Instance method
    public List<Table> findAvailableTables(LocalDateTime startTime, int duration, int guests) {
        List<Table> available = new ArrayList<>();
        for (Table table : tables) {
            if (table.getCapacity() >= guests &&
                    table.checkAvailability(startTime, duration)) {
                available.add(table);
            }
        }
        return available;
    }

    /**
     * Initializes the tables based on the specifications.
     * 4 tables of 2, 4 tables of 4, 2 tables of 8, and 1 table of 10.
     *
     * @return List of initialized tables
     */
    private List<Table> initializeTables() {
        List<Table> tables = new ArrayList<>();
        // Add tables as per spec (4x2, 4x4, 2x8, 1x10)
        for (int i = 1; i <= 4; i++) tables.add(new Table(i, 2));
        for (int i = 5; i <= 8; i++) tables.add(new Table(i, 4));
        for (int i = 9; i <= 10; i++) tables.add(new Table(i, 8));
        tables.add(new Table(11, 10));
        return tables;
    }

    // Assign a table to a reservation
//    public boolean assignTable(int tableId, Reservation reservation) {
//        Table table = getTableById(tableId);
//        return table != null && table.assignToReservation(reservation);
//    }

    // Add validation to
    /**
     * Assigns a table to a reservation.
     *
     * @param tableId      the ID of the table to assign
     * @param reservation   the reservation to assign to the table
     * @return true if the assignment was successful, false otherwise
     * @throws ServiceException if an error occurs during assignment
     */
    public boolean assignTable(int tableId, Reservation reservation) throws ServiceException {
        try {
            Objects.requireNonNull(reservation, "Reservation cannot be null");
            Table table = getTableById(tableId);
            if (table == null) {
                throw new ServiceException("Table not found");
            }
            if (!table.checkAvailability(reservation.getStartTime(), reservation.getDuration())) {
                throw new ServiceException("Table not available for requested time");
            }
            return table.assignToReservation(reservation);
        } catch (Exception e) {
            logger.severe("Error loading table: " + e.getMessage());
            throw new ServiceException("Failed to assign table", e);
        }
    }

    // Add method to get tables by capacity range
    /**
     * Retrieves tables within a specified capacity range.
     *
     * @param minCapacity the minimum capacity
     * @param maxCapacity the maximum capacity
     * @return List of tables within the specified capacity range
     */
    public List<Table> getTablesByCapacity(int minCapacity, int maxCapacity) {
        return tables.stream()
                .filter(t -> t.getCapacity() >= minCapacity && t.getCapacity() <= maxCapacity)
                .toList();
    }

    // Helper method
    /**
     * Retrieves a table by its ID.
     *
     * @param tableId the ID of the table to retrieve
     * @return the Table object if found, null otherwise
     */
    public Table getTableById(int tableId) {
        return tables.stream()
                .filter(t -> t.getTableId() == tableId)
                .findFirst()
                .orElse(null);
    }

    // Get all tables (for reports/UI)
    /**
     * Retrieves all tables in the system.
     *
     * @return a list of all Table objects
     */
    public List<Table> getAllTables() {
        return new ArrayList<>(tables);
    }
}