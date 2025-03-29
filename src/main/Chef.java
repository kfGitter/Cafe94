package com.cafe94.domain;

import com.cafe94.enums.UserRole;
// Assuming ScheduleEntry is in the same package or imported correctly
// import com.cafe94.domain.ScheduleEntry;
import java.util.List;

/**
 * Represents a Chef user in the Cafe94 system.
 * This is a concrete class inheriting properties from {@link Staff}.
 *
 * Responsibilities:
 * Chefs are primarily concerned with food preparation, managing menu items,
 * and potentially inventory. Actions like marking orders as
 * "PREPARING" or "READY",
 * or adding/modifying menu items, are typically executed via Service classes
 * (e.g., OrderService, MenuService) that ensure the acting user is a Chef
 * with the correct permissions.
 */
public class Chef extends Staff {

    /**
     * Constructs a new Chef object.
     *
     * @param userID            Unique User ID (0 for new entities) or
     * existing ID.
     * @param firstName         Chef's first name (validated by superclass).
     * @param lastName          Chef's last name
     * (validated by superclass).
     * @param email             Chef's email address
     * (validated by superclass).
     * @param hashedPassword    Chef's hashed password
     * (validated by superclass).
     * @param schedule          Chef's work schedule (nullable, stored
     * immutably by superclass).
     * @param totalHoursWorked  Initial total hours worked (non-negative,
     * validated by superclass).
     */
    public Chef(int userID, String firstName, String lastName, String email,
                String hashedPassword, List<ScheduleEntry> schedule,
                double totalHoursWorked) {
        // Explicitly call the updated Staff constructor, setting
        // the role to UserRole.CHEF
        super(userID, firstName, lastName, email, hashedPassword,
        UserRole.CHEF, schedule, totalHoursWorked);
        // No Chef-specific fields to initialize in this version
    }

    // No Chef-specific methods added in this version.
    // equals, hashCode, and toString are inherited from Staff.
}