package cafe94.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cafe94.enums.TableStatus;


public class Table {
    private final int tableId;
    private final int capacity;
    private TableStatus status;
    private List<Reservation> reservations;

    public Table(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = TableStatus.AVAILABLE;
        this.reservations = new ArrayList<>();
    }

    // Getters (no setters for final fields)
    public int getTableId() { return tableId; }
    public int getCapacity() { return capacity; }
    public TableStatus getStatus() { return status; }
    public List<Reservation> getReservations() { return reservations; }

    // Business Logic Methods
    public boolean assignToReservation(Reservation reservation) {
        if (checkAvailability(reservation.getStartTime(), reservation.getDuration())) {
            reservations.add(reservation);
            status = TableStatus.RESERVED;
            return true;
        }
        return false;
    }


    public void releaseTable() {
        this.status = TableStatus.AVAILABLE;
        this.reservations.removeIf(r -> r.getEndTime().isBefore(LocalDateTime.now()));
        System.out.println("Table " + tableId + " released");
    }

    public boolean checkAvailability(LocalDateTime requestedStart, int requestedDuration) {
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

//    public boolean checkAvailability(LocalDateTime time, int duration) {
//        for (Reservation r : reservations) {
//            LocalDateTime end = r.getStartTime().plusMinutes(r.getDuration());
//            if (!time.isAfter(end) && !time.plusMinutes(duration).isBefore(r.getStartTime())) {
//                return false;
//            }
//        }
//        return status == TableStatus.AVAILABLE;
//    }

    @Override
    public String toString() {
        return String.format("Table %d (%d seats) - %s", tableId, capacity, status);
    }
}