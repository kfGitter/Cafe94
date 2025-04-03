package cafe94.models;

import cafe94.enums.ReservationStatus;

import java.time.LocalDateTime;


public class Booking extends Reservation {
    private  int numberOfGuests;

    public Booking(int customerId, LocalDateTime startTime, int numberOfGuests) {
        super(customerId, startTime, 60);
        this.numberOfGuests = numberOfGuests;
    }

    // Constructor with custom duration
    public Booking(int customerId, LocalDateTime startTime, int duration, int numberOfGuests) {
        super(customerId, startTime, duration);
        this.numberOfGuests = numberOfGuests;
    }

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


//    public boolean approve(int waiterId) {
//        if (waiterId <= 0) {
//            throw new IllegalArgumentException("Waiter ID required");
//        }
//        // Implementation for approving a booking
//        if (this.tables.isEmpty()) {
//            System.out.println("Booking cannot be approved - no tables assigned");
//            return false;
//        }
//        this.status = ReservationStatus.APPROVED;
//        notifyCustomer("Your booking (ID: " + reservationId + ") is confirmed!");
//        return true;
//    }



    //  Cancel Booking (Releases tables)
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

//    public void cancel() {
//        // Release all tables
//        for (Table table : tables) {
//            table.releaseTable();
//        }
//        this.status = ReservationStatus.CANCELLED;
//        notifyCustomer("Your booking (ID: " + reservationId + ") has been cancelled");
//    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

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

