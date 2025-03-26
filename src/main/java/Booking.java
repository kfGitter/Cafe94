//AI GENERATED TO SUPPORT TABLE RESERVATION


import java.time.LocalDateTime;

public class Booking extends Reservation {
    private int numberOfGuests;

    public Booking(int customerId, LocalDateTime startTime, int duration, int numberOfGuests) {
        super(customerId, startTime, duration);
        this.numberOfGuests = numberOfGuests;
    }

    //  Approve Booking (Assigns tables if available)
    @Override
    public boolean approve() {
        if (tables.isEmpty()) {
            System.out.println(" Booking cannot be approved. No tables assigned.");
            return false;
        }
        this.status = ReservationStatus.APPROVED;
        notifyCustomer(" Your booking (ID: " + reservationId + ") is confirmed!");
        return true;
    }

    //  Cancel Booking (Releases tables)
    @Override
    public void cancel() {
        for (Table table : tables) {
            table.releaseTable();
        }
        this.status = ReservationStatus.CANCELED;
        notifyCustomer(" Your booking (ID: " + reservationId + ") has been canceled.");
    }

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

