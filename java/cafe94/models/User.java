package cafe94.models;

import java.io.Serializable;
import java.util.Objects;

import cafe94.enums.UserRole;

public class User implements Serializable {
    private static int nextUserId = 1;
    private final int userId;
    private String username = "";
    private String email;
    private String address;
    private String firstName;
    private String lastName;
    //password simply stored just for coursework, not to do in production
    private String password;
    private final UserRole role;
    private boolean isActive;

    // -------------------------------------------------------------------
    // Constructor
    public User(String username, String password, UserRole role) {
        this.userId = nextUserId++;
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.role = Objects.requireNonNull(role, "Role cannot be null");
//        this.isActive = true;
    }


    // -------------------------------------------------------------------

    public void selectProfile() {};


    // -------------------------------------------------------------------
    // Validation methods

    private String validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if (password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters");
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
            throw new IllegalArgumentException("Password must contain at least one letter");
        }
        if (!hasDigit) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        if (!hasSpecial) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }

        return password;
    }


    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters");
        }
        return username;
    }

    private String validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email;
    }



    // -------------------------------------------------------------------
    // Account status methods
//    public boolean isActive() {
//        return isActive;
//    }
//
//    public void activateAccount() {
//        this.isActive = true;
//    }
//
//    public void deactivateAccount() {
//        this.isActive = false;
//    }

    // -------------------------------------------------------------------
    // Password Methods

    public boolean verifyPassword(String input) {
        return this.password.equals(input); // In production, compare hashes
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (!verifyPassword(currentPassword)) {
            throw new SecurityException("Current password is incorrect");
        }
        this.password = validatePassword(newPassword);
    }

    // -------------------------------------------------------------------
    // Getters
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
    public UserRole getRole() {
        return role;
    }

    public int getUserId() {
        return userId;
    }


    // -------------------------------------------------------------------
    //Setters

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");;
    }

//    public void setUsername(String username) {
//        this.username = validateUsername(username);
//    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public void setAddress(String address) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");;
    }

    // -------------------------------------------------------------------
    @Override
    public String toString() {
        return String.format("User[username=%s, role=%s, active=%s]",
                username, role, isActive);
    }

    // -------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}