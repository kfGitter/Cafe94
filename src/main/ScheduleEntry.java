package com.cafe94.domain;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a single entry in a staff member's work schedule,
 * defining a shift on a specific day with start and end times.
 * This class is immutable.
 */

 // Mark as final for immutability, implement Serializable
public final class ScheduleEntry implements Serializable {

    // Add serialVersionUID for Serializable
    private static final long serialVersionUID = 1L;

    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Constructs an immutable ScheduleEntry.
     *
     * @param dayOfWeek The day of the week for this shift (non-null).
     * @param startTime The start time of the shift (non-null).
     * @param endTime   The end time of the shift (non-null,
     * must be after startTime).
     * @throws NullPointerException if any parameter is null.
     * @throws IllegalArgumentException if endTime is not strictly
     * after startTime.
     */
    public ScheduleEntry(DayOfWeek dayOfWeek, LocalTime startTime,
    LocalTime endTime) {
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek,
        "Day of week cannot be null.");
        this.startTime = Objects.requireNonNull(startTime,
        "Start time cannot be null.");
        this.endTime = Objects.requireNonNull(endTime,
        "End time cannot be null.");

        // Validate that end time is strictly after start time
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time (" + endTime + ") " +
            "must be strictly after start time (" + startTime + ").");
        }
    }

    // --- Getters ---

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // --- Object Overrides ---

    @Override
    public String toString() {
        return "ScheduleEntry{" +
               "day=" + dayOfWeek +
               ", start=" + startTime +
               ", end=" + endTime +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use getClass() check for exact type matching as it's final
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEntry that = (ScheduleEntry) o;
        return dayOfWeek == that.dayOfWeek &&
               Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startTime, endTime);
    }
}