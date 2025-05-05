package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.Objects;
import cafe.ninetyfour.enums.UserRole;
import cafe.ninetyfour.exceptions.ServiceException;

/**
 * This class represents a user in the cafe management system.
 * It is the base class for all user types in the system
 * (customers, staff, etc.).
 * Implements Serializable to support object persistence.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static int nextUserId = 1;
    protected final int userId;
    protected String username = "";
    protected String email;
    protected String address;
    protected String firstName;
    protected String lastName;

    //password simply stored just for coursework, not to do in production
    private String password;
    protected final UserRole role;
    protected boolean isActive;

    // -------------------------------------------------------------------
    /**
     * The defined Constructor of the User Class.
     * Constructs a new User with the specified username, password, and role.
     *
     * @param username the unique identifier for the user (minimum 5 characters)
     * @param password the user's password (must meet complexity requirements)
     * @param role the user's role in the system (cannot be null)
     * @throws IllegalArgumentException if username or password validation fails
     * @throws NullPointerException if role is null
     */
    public User(String username, String password, UserRole role) {
        this.userId = nextUserId++;
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.isActive = true;
    }



    // -------------------------------------------------------------------
    // Validation methods

    /**
     * Validates that a password meets system requirements.
     *
     * @param password the password to validate
     * @return the validated password
     * @throws IllegalArgumentException if password is null
     * or doesn't meet requirements
     */
    private static String validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (password.length() < 7) {
            throw new IllegalArgumentException
                    ("Password must be at least 7 characters");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }

        if (!hasLetter) {
            throw new IllegalArgumentException
                    ("Password must contain at least one letter");
        }
        if (!hasDigit) {
            throw new IllegalArgumentException
                    ("Password must contain at least one number");
        }
        if (!hasSpecial) {
            throw new IllegalArgumentException
                    ("Password must contain at least one special character");
        }

        return password;
    }


    /**
     * Validates that a username meets system requirements.
     *
     * @param username the username to validate
     * @return the validated username
     * @throws IllegalArgumentException if username is null
     * or doesn't meet requirements
     */
    private static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 5) {
            throw new IllegalArgumentException
                    ("Username must be at least 5 characters");
        }
        return username;
    }


    /**
     * Validates an email address format (using Regex).
     *
     * @param email the email address to validate
     * @return the validated email address
     * @throws IllegalArgumentException if email format is invalid
     */
    private static String validateEmail(String email) {
        if (email == null || !email.matches
                ("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email;
    }

    /**
     * Validates all registration input fields at once.
     *
     * @param username the proposed username
     * @param password the proposed password
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param email the user's email address
     * @throws ServiceException if any validation fails
     */
    public static void validateRegistrationInput(String username,
                                                 String password,
                                                 String firstName,
                                                 String lastName,
                                                 String email)
            throws ServiceException {
        try {
            validateUsername(username);
            validatePassword(password);
            validateEmail(email);
            Objects.requireNonNull
                    (firstName, "First name cannot be null");
            Objects.requireNonNull
                    (lastName, "Last name cannot be null");


            // Additional validations if needed
            if (firstName.trim().isEmpty()) {
                throw new ServiceException("First name cannot be empty");
            }
            if (lastName.trim().isEmpty()) {
                throw new ServiceException("Last name cannot be empty");
            }
        } catch (IllegalArgumentException e) {
            // Convert to ServiceException for consistent error handling
            throw new ServiceException(e.getMessage());
        }
    }


    //-------------------------------------------------------------------
    // Account status methods
    public boolean isActive() {
        return isActive;
    }

    public void activateAccount() {
        this.isActive = true;
    }

    public void deactivateAccount() {
        this.isActive = false;
    }

    // -------------------------------------------------------------------
    // Password Methods

    /**
     * Verifies if the input password matches the user's stored password.
     *
     * @param input the password to verify
     * @return true if passwords match, false otherwise
     */
    public boolean verifyPassword(String input) {
        return this.password.equals(input); // In production, compare hashes
    }


    /**
     * Changes the user's password after verifying the current password.
     *
     * @param currentPassword the current password
     * @param newPassword the new password
     * @throws SecurityException if the current password is incorrect
     */
    public void changePassword(String currentPassword, String newPassword) {
        if (!verifyPassword(currentPassword)) {
            throw new SecurityException("Current password is incorrect");
        }
        this.password = validatePassword(newPassword);
    }

    // -------------------------------------------------------------------
    // Getters

    /**
     * Gets the user's unique identifier.
     * @return the user ID
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's first name.
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the user's last name.
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's email address.
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's address.
     * @return the user's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the user's role in the system.
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Gets the user's unique identifier.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the user's password.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    // -------------------------------------------------------------------
    //Setters

    /**
     * Sets the user's username after validation.
     *
     * @param username the new username
     * @throws IllegalArgumentException if the username is invalid
     */

    public void setUsername(String username) {
        this.username = validateUsername(username);
    }


    /**
     * Sets the user's first name after validation.
     *
     * @param firstName the new first name
     * @throws IllegalArgumentException if the first name is invalid
     */
    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull
                (firstName, "First name cannot be null");
    }

    /**
     * Sets the user's last name after validation.
     *
     * @param lastName the new last name
     * @throws IllegalArgumentException if the last name is invalid
     */
    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull
                (lastName, "Last name cannot be null");;
    }

    /**
     * Sets the user's email address after validation.
     *
     * @param email the new email address
     * @throws IllegalArgumentException if the email format is invalid
     */
    public void setEmail(String email) {
        this.email = validateEmail(email);
    }


    /**
     * Sets the user's address after validation.
     *
     * @param address the new address
     * @throws IllegalArgumentException if the address is invalid
     */
    public void setAddress(String address) {
        this.address = Objects.requireNonNull
                (address, "Address cannot be null");;
    }

    public static void setNextUserId(int id) {
        nextUserId = id;
    }

    // -------------------------------------------------------------------
    /**
     * Returns a string representation of the User object.
     * @return a string containing the user's details
     */
    @Override
    public String toString() {
        return String.format("User[username=%s, role=%s, active=%s]",
                username, role, isActive);
    }

    // -------------------------------------------------------------------

    /**
     * Compares users based on username.
     * @param o the object to compare with
     * @return true if usernames match, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    /**
     * Generates a hash code based on username.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


}