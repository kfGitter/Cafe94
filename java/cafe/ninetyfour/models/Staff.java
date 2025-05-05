package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.UserRole;

import java.io.Serializable;

/**
 * Abstract base class for all staff members in the cafe system.
 * Extends User with staff specific functionalites like work hour tracking.
 */
public abstract class Staff extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int totalWorkedHours;
    protected final int hoursToWork;  // Made final since it shouldn't change after creation


    /**
     * Constructs a new Staff member with the following details.
     *
     * @param username the staff member's username
     * @param password the staff member's password
     * @param role the staff member's role (cannot be null)
     * @param hoursToWork the contracted hours the staff member must work
     * @throws NullPointerException if role is null
     */
    public Staff(String username, String password, UserRole role, int hoursToWork, String firstName, String lastName) {
        super(username, password, role);
        setFirstName(firstName);
        setLastName(lastName);
        this.hoursToWork = hoursToWork;
        this.totalWorkedHours = 0;  // Initialize in constructor only
    }


    /**
     * Logs additional worked hours for the staff member.
     *
     * @param hours the number of hours to add (must be positive)
     * @throws IllegalArgumentException if hours is negative
     */
    public void logWorkedHours(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
        this.totalWorkedHours += hours;
    }

    /**
     * Gets the total hours worked by this staff member.
     * @return the total worked hours
     */
    public int getTotalWorkedHours() { return totalWorkedHours; }


    /**
     * Gets the contracted hours this staff member must work.
     * @return the required work hours
     */
    public int getHoursToWork() { return hoursToWork; }

}
