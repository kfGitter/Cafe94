package com.cafe94.domain;

import com.cafe94.enums.UserRole;
import com.cafe94.util.ValidationUtils;
import java.util.Objects;

/**
 * Represents a customer user with contact information and
 * optional staff association.
 * Inherits core user properties and implements
 * customer-specific functionality.
 */
public class Customer extends User {


    // Made address and phoneNumber final for immutability after creation
    private final String address;
    private final String phoneNumber;
    
    private Integer associatedStaffUserId;

    /**
     * Constructs a validated Customer entity.
     * * @param userID Temporary identifier (0 for new entities) or
     * existing ID.
     * @param firstName Legal given name (validated by superclass).
     * @param lastName Legal family name (validated by superclass).
     * @param email Unique contact identifier (validated by superclass).
     * @param hashedPassword Secure credential hash
     *  (validated by superclass).
     * @param address Physical location information (non-blank).
     * @param phoneNumber Optional contact number (non-blank if provided,
     * otherwise null).
     */
    public Customer(int userID, String firstName, String lastName,
    String email, String hashedPassword, String address,
    String phoneNumber) {
        // Removed redundant validation from super call arguments
        super(userID, firstName, lastName, UserRole.CUSTOMER, email,
        hashedPassword);
        
        // Validate and set customer-specific fields
        this.address = ValidationUtils.requireNonBlank(address, "Address");
        // Validate phoneNumber: allow null
        // but require non-blank if a string is provided
        this.phoneNumber = (phoneNumber == null || phoneNumber.isBlank())
        ? null : ValidationUtils.requireNonBlank(phoneNumber, "Phone number");
        this.associatedStaffUserId = null; // Default to no association
    }

    // --- Getters ---
    
    /** @return The customer's address. */
    public String getAddress() {
        return address;
    }

    /** @return The customer's phone number,
     * or null if not provided/applicable. */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** @return The ID of the associated staff member, or null if none. */
    public Integer getAssociatedStaffUserId() {
        return associatedStaffUserId;
    }

    // --- Mutators --- (Only for fields intended to be mutable)

    /**
     * Sets or clears the associated staff user ID.
     * Allows associating a customer with a specific staff member
     * (e.g., for personal service).
     * * @param staffUserId The ID of the staff member to associate,
     * or null to remove association.
     * @throws IllegalArgumentException if staffUserId is provided
     * but is not positive.
     */
    // Changed visibility to public to allow service layer management
    public void setAssociatedStaffUserId(Integer staffUserId) { 
        if (staffUserId != null && staffUserId <= 0) {
            throw new IllegalArgumentException(
                "Associated StaffID must be positive if provided.");
        }
        this.associatedStaffUserId = staffUserId;
    }

    // --- Object Identity Management ---
    
    @Override
    public String toString() {
        // Included address/phone in the detailed string representation
        return super.toString().replaceFirst("]$", "") +
               ", Address='" + address + '\'' +
               ", Phone='" + (phoneNumber != null ? phoneNumber : "N/A") +
               '\'' + ", StaffLink=" + (associatedStaffUserId != null ?
               associatedStaffUserId : "None") +']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use getClass() check for stricter equality inherited from User
        if (o == null || getClass() != o.getClass()) return false;
        // Check base User equality first (which uses ID or email)
        if (!super.equals(o)) return false;
        
        Customer customer = (Customer) o;
        // If base equality holds, compare Customer-specific fields
        return Objects.equals(address, customer.address)
            && Objects.equals(phoneNumber, customer.phoneNumber)
            && Objects.equals(associatedStaffUserId,
            customer.associatedStaffUserId);
    }

    @Override
    public int hashCode() {
        // Combine hashcode from base User with Customer-specific fields
        return Objects.hash(super.hashCode(), address,
        phoneNumber, associatedStaffUserId);
    }
}