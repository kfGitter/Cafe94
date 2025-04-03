package cafe94.models;

import cafe94.enums.UserRole;

public abstract class Staff extends User {
    protected int totalWorkedHours;
    protected final int hoursToWork;  // Made final since it shouldn't change after creation

    public Staff(String username, String password, UserRole role, int hoursToWork) {
        super(username, password, role);
        this.hoursToWork = hoursToWork;
        this.totalWorkedHours = 0;  // Initialize in constructor only
    }


    public void logWorkedHours(int hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours cannot be negative");
        this.totalWorkedHours += hours;
    }

    // Getters
    public int getTotalWorkedHours() { return totalWorkedHours; }
    public int getHoursToWork() { return hoursToWork; }
}
