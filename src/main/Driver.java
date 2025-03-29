package com.cafe94.domain;

import com.cafe94.enums.UserRole;
// Assuming ScheduleEntry is in the same package or imported correctly
import com.cafe94.domain.ScheduleEntry;
import java.util.List;

/**
 * Represents a Driver user in the Cafe94 system,
 * responsible for delivering orders.
 * This is a concrete class inheriting properties from {@link Staff}.
 *
 * Responsibilities:
 * Drivers view assigned delivery orders and
 * update their status (e.g., "OUT_FOR_DELIVERY",
 * "DELIVERED"). These operations typically involve interacting with an OrderService
 * or a dedicated DeliveryService, verifying the user's DRIVER role and permissions.
 */
public class Driver extends Staff {

    /**
     * Constructs a new Driver object.
     *
     * @param userID            Unique User ID (0 for new entities) or
     * existing ID.
     * @param firstName         Driver's first name (validated by superclass).
     * @param lastName          Driver's last name (validated by superclass).
     * @param email             Driver's email address (validated by superclass).
     * @param hashedPassword    Driver's hashed password (validated by superclass).
     * @param schedule          Driver's work schedule (nullable, stored
     * immutably by superclass).
     * @param totalHoursWorked  Initial total hours worked (non-negative,
     * validated by superclass).
     */
    public Driver(int userID, String firstName, String lastName, String email,
                  String hashedPassword, List<ScheduleEntry> schedule,
                  double totalHoursWorked) {
        // Explicitly call the updated Staff constructor,
        //setting the role to UserRole.DRIVER
        super(userID, firstName, lastName, email,
        hashedPassword, UserRole.DRIVER, schedule, totalHoursWorked);
        // No Driver-specific fields to initialize in this version
    }

    // No Driver-specific methods added in this version.
    // equals, hashCode, and toString are inherited from Staff.
}
