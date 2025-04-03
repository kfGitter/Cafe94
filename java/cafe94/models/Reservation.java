package cafe94.models;

import java.time.LocalDateTime;
import java.util.*;
import cafe94.*;
import cafe94.enums.ReservationStatus;

public abstract class Reservation {
    protected static int nextId = 1;
    protected final int reservationId;
    protected int customerId;
    protected LocalDateTime startTime;
    protected int duration; // Duration in minutes
    protected List<Table> tables;
    protected ReservationStatus status;
    protected LocalDateTime creationTime;

    public Reservation(int customerId, LocalDateTime startTime, int duration) {
        this.creationTime = LocalDateTime.now();
        this.reservationId = nextId++;
        this.customerId = customerId;
        this.startTime = startTime;
        this.duration = duration;
        this.tables = new ArrayList<>();
        this.status = ReservationStatus.PENDING;
    }

    //  Abstract methods (Implemented in Booking & Event)
    public abstract boolean approve();
    public abstract void cancel();

    //  Common Methods

    public boolean addTable(Table table) {
        if (table.assignToReservation(this)) {
            tables.add(table);
            return true;
        }
        return false;
    }

    public void notifyCustomer(String message) {
        System.out.println("Notification to customer " + customerId + ": " + message);
    }

    public int getCustomerId() {
        return customerId;
    }
    public int getReservationId() {
        return reservationId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public List<Table> getTables() {
        return tables;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }}

