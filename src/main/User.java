// File: src/main/java/com/cafe94/domain/User.java
package com.cafe94.domain;

import com.cafe94.enums.UserRole;
import com.cafe94.util.ValidationUtils;
import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class representing any user interacting
 * with the Cafe94 system.
 * Allows modification of certain fields via protected setters.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int ZERO = 0;
    
    private int userID;
    
    // Removed final to allow updates via setters
    private String firstName;
    private String lastName;
    private String email;
    
    // Password should generally not be directly updatable via a simple setter
    private final String hashedPassword;
    private final UserRole role;

    /**
     * Constructs a new User entity with validated fields.
     */
    protected User(int userID, String firstName, String lastName,
    UserRole role, String email, String hashedPassword) {
        this.userID = userID;
        this.role = Objects.requireNonNull(role, "User role cannot be null.");
        // Use setters internally during construction to leverage validation
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.hashedPassword = ValidationUtils.requireNonBlank(hashedPassword,
        "Hashed password");
    }

    // --- Accessors ---
    public int getUserID() { return userID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public UserRole getRole() { return role; }
    public String getEmail() { return email; }
    public String getHashedPassword() { return hashedPassword; }

    // --- Mutators ---

    /** Sets the persistent User ID. Should typically only be called once
     * by persistence. */
    protected void setUserID(int userID) { 
        if (userID <= ZERO && this.userID != ZERO) {
            throw new IllegalStateException("Cannot reset " +
            "assigned ID to zero or negative.");
        }
         if (this.userID != ZERO && this.userID != userID && userID != 0) {
             throw new IllegalStateException(
                "Cannot change an already assigned persistent ID.");
         }
        this.userID = userID;
    }

    /** Updates the user's first name after validation. */
    protected void setFirstName(String firstName) {
        this.firstName = ValidationUtils.requireNonBlank(firstName,
        "First name");
    }

    /** Updates the user's last name after validation. */
    protected void setLastName(String lastName) {
        this.lastName = ValidationUtils.requireNonBlank(lastName, "Last name");
    }

    /** Updates the user's email after validation. */
    protected void setEmail(String email) {
        // Add email format validation here if desired using ValidationUtils
        this.email = ValidationUtils.requireNonBlank(email, "Email");
    }
    
    // Note: No setter for hashedPassword - password changes
    // should be specific operations.
    // Note: No setter for role - role changes via hire/fire/promote logic.

    // --- Object Overrides ---
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + "ID=" + userID +
               ", Name='" + firstName + " " + lastName + '\'' +
               ", Role=" + role + ", Email=" + email +']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User other = (User) o;
        if (userID != 0 && other.userID != 0) {
            return userID == other.userID;
        }
        return Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
         if (userID != 0) {
            return Objects.hash(userID);
        }
        return Objects.hash(email);
    }
}