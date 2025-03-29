package com.cafe94.domain;

import com.cafe94.enums.UserRole;
import java.util.List;

/**
 * Represents a Waiter user in the Cafe94 system.
 * This is a concrete class inheriting properties from {@link Staff}.
 *
 * Responsibilities:
 * Waiters handle customer interactions for dine-in service,
 * including taking orders, managing table assignments and status,
 * potentially processing payments, and approving bookings or deliveries.
 * These actions are typically performed through Service classes
 * (OrderService, TableService, BookingService, PaymentService) which verify
 * the user's WAITER role and permissions.
 */
public class Waiter extends Staff {

    /**
     * Constructs a new Waiter object.
     *
     * @param userID            Unique User ID (0 for new entities)
     * or existing ID.
     * @param firstName         Waiter's first name (validated by superclass).
     * @param lastName          Waiter's last name (validated by superclass).
     * @param email             Waiter's email address
     * (validated by superclass).
     * @param hashedPassword    Waiter's hashed password
     * (validated by superclass).
     * @param schedule          Waiter's work schedule
     * (nullable, stored immutably by superclass).
     * @param totalHoursWorked  Initial total hours worked
     * (non-negative, validated by superclass).
     */
    public Waiter(int userID, String firstName, String lastName, String email,
                  String hashedPassword, List<ScheduleEntry> schedule,
                  double totalHoursWorked) {
        // Explicitly call the updated Staff constructor,
        // setting the role to UserRole.WAITER
        super(userID, firstName, lastName, email, hashedPassword,
        UserRole.WAITER, schedule, totalHoursWorked);
        // No Waiter-specific fields to initialize in this version
    }

    // No Waiter-specific methods added in this version.
    // equals, hashCode, and toString are inherited from Staff.
}