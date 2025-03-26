import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final int tableId;
    private static int nextId = 1;
    private TableStatus status;
    private int capacity;
    private boolean isAvailable;
    private List<Reservation> reservations; // Track reservations

    public Table(int capacity) {
        this.tableId = nextId++;
        this.status = TableStatus.AVAILABLE;
        this.isAvailable = true;
        this.capacity = capacity;
        this.reservations = new ArrayList<>(); // Initialize list
    }

    // Getters
    public int getCapacity() {
        return capacity;
    }

    public int getTableId() {
        return tableId;
    }

    public TableStatus getStatus() {
        return status;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public void setAvailability(boolean available) {
        this.isAvailable = available;
        this.status = available ? TableStatus.AVAILABLE : TableStatus.UNAVAILABLE;
    }

    //  Added method: Updates table status dynamically
    public void updateStatus(TableStatus newStatus) {
        this.status = newStatus;
        this.isAvailable = (newStatus == TableStatus.AVAILABLE);
    }

    //  Added method: Releases a table after a reservation ends
    public void releaseTable() {
        this.isAvailable = true;
        this.status = TableStatus.AVAILABLE;
        reservations.clear(); // Remove past reservations
        System.out.println("Table " + tableId + " is now available.");
    }

    //  Added method: Assign a table to a reservation
    public boolean assignToReservation(Reservation reservation) {
        if (checkAvailability(reservation.getStartTime(), reservation.getDuration())) {
            reservations.add(reservation);
            updateStatus(TableStatus.RESERVED);
            return true;
        }
        return false;
    }

    //  Improved checkAvailability (Uses stored reservations)
    public boolean checkAvailability(LocalDateTime time, int duration) {
        for (Reservation reservation : reservations) {
            LocalDateTime startReserved = reservation.getStartTime();
            LocalDateTime endReserved = startReserved.plusMinutes(reservation.getDuration());

            if (!time.isAfter(endReserved) && !time.isBefore(startReserved)) {
                return false; // Table is occupied during this time
            }
        }
        return isAvailable;
    }

    @Override
    public String toString() {
        return "Table ID: " + tableId + "\nStatus: " + status + "\nCapacity: " + capacity;
    }
}
