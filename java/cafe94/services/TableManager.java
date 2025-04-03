package cafe94.services;

import java.time.LocalDateTime;
import java.util.*;

import cafe94.models.*;

public class TableManager {
    private final List<Table> tables;

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
    public boolean assignTable(int tableId, Reservation reservation) {
        Table table = getTableById(tableId);
        return table != null && table.assignToReservation(reservation);
    }

    // Helper method
    public Table getTableById(int tableId) {
        return tables.stream()
                .filter(t -> t.getTableId() == tableId)
                .findFirst()
                .orElse(null);
    }

    // Get all tables (for reports/UI)
    public List<Table> getAllTables() {
        return new ArrayList<>(tables);
    }
}