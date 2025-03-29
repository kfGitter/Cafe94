package com.cafe94.domain;

import com.cafe94.enums.UserRole;
// Assuming ScheduleEntry is in the same package or imported correctly
// import com.cafe94.domain.ScheduleEntry;
import java.util.List;

/**
 * Represents a Manager user in the Cafe94 system.
 * This is a concrete class inheriting properties from {@link Staff}
 * (and transitively {@link User}).
 *
 * Responsibilities:
 * Managers typically have broad permissions, overseeing operations, staff, reports,
 * and system configuration. Specific actions (like hiring staff, generating financial reports)
 * are usually implemented within dedicated Service classes (e.g., StaffService, ReportService),
 * which verify the user performing the action has the MANAGER role and necessary permissions.
 * This class mainly serves to represent the Manager entity itself.
 */
public class Manager extends Staff {

    /**
     * Constructs a new Manager object.
     *
     * @param userID            Unique User ID (0 for new entities) or existing ID.
     * @param firstName         Manager's first name (validated by superclass).
     * @param lastName          Manager's last name (validated by superclass).
     * @param email             Manager's email address (validated by superclass).
     * @param hashedPassword    Manager's hashed password (validated by superclass).
     * @param schedule          Manager's work schedule (nullable, stored immutably by superclass).
     * @param totalHoursWorked  Initial total hours worked (non-negative, validated by superclass).
     */
    public Manager(int userID, String firstName, String lastName, String email, 
                   String hashedPassword, List<ScheduleEntry> schedule, 
                   double totalHoursWorked) {
        // Explicitly call the updated Staff constructor, setting the role to UserRole.MANAGER
        super(userID, firstName, lastName, email, hashedPassword, UserRole.MANAGER, schedule, totalHoursWorked);
        // No Manager-specific fields to initialize in this version
    }

    // --- Manager Specific Business Logic ---
    // Comments explaining why logic resides in services remain valid.

    // Note: No additional fields or complex methods are added here by default.
    // toString(), equals(), hashCode() are inherited from Staff/User and should suffice.
}