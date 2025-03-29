// File: src/main/java/com/cafe94/domain/Staff.java
package com.cafe94.domain;

import com.cafe94.enums.UserRole;
import com.cafe94.util.ValidationUtils; 
import java.io.Serializable;
import java.util.ArrayList; // Needed for mutable list copy
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for staff members. Allows schedule updates.
 */
public abstract class Staff extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    // Allow schedule to be mutable internally,
    // but getter returns unmodifiable view
    private List<ScheduleEntry> schedule;
    private double totalHoursWorked;

    /**
     * Constructs a new Staff entity.
     */
    protected Staff(int userID, String firstName, String lastName,
    String email, String hashedPassword, UserRole role,
    List<ScheduleEntry> schedule, double totalHoursWorked) {
        super(userID, firstName, lastName, role, email, hashedPassword);
        // Store a mutable copy internally, handle null input
        this.schedule =
        new ArrayList<>(ValidationUtils.nullSafeList(schedule));
        this.totalHoursWorked = ValidationUtils.requireNonNegative(
            totalHoursWorked, "Total hours worked"
        );
    }

    // --- Schedule Management ---
    
    /** @return An immutable view of the staff member's scheduled shifts. */
    public List<ScheduleEntry> getSchedule() {
        // Return unmodifiable view
        return Collections.unmodifiableList(schedule);
    }

    /** * Updates the staff member's work schedule.
     * Stores a mutable copy internally.
     * @param newSchedule Nullable list of new schedule entries.
     */
    // Re-added setSchedule to allow updates via UserServiceImpl
    public void setSchedule(List<ScheduleEntry> newSchedule) {
        // Store a mutable copy, handling null input
        this.schedule =
        new ArrayList<>(ValidationUtils.nullSafeList(newSchedule));
    }

    // --- Hours Tracking ---
    // (Methods remain the same as previous version)
    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }
    public void setTotalHoursWorked(double hours) {
        /* ... validation ... */
        this.totalHoursWorked =
        ValidationUtils.requireNonNegative(hours, "Total hours worked");
    }
    public void addWorkedHours(double hoursToAdd) {
        /* ... validation ... */
        ValidationUtils.requireNonNegative(hoursToAdd, "Hours to add");
        this.totalHoursWorked += hoursToAdd;
    }

    // --- Object Overrides ---
    @Override
    public String toString() {
        // ... (toString implementation remains the same as previous version)
         return super.toString().replaceFirst("]$", "")
                + ", ScheduleEntries=" + (schedule !=
                null ? schedule.size() : 0) + ", TotalHours="
                + String.format("%.2f", totalHoursWorked) + ']';
    }

    @Override
    public boolean equals(Object o) {
        // ... (equals implementation remains the same as previous version)
        if (!super.equals(o)) return false;
        Staff staff = (Staff) o;
        return Double.compare(staff.totalHoursWorked, totalHoursWorked) == 0
               && Objects.equals(schedule, staff.schedule);
    }

    @Override
    public int hashCode() {
        // ... (hashCode implementation remains the same as previous version)
        return Objects.hash(super.hashCode(), schedule, totalHoursWorked);
    }
}
