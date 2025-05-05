package cafe.ninetyfour.services;


import java.util.*;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.models.Customer;
import cafe.ninetyfour.models.Staff;
import cafe.ninetyfour.models.User;


/**
 * AuthService class is responsible for handling user authentication
 * and session management in the cafe system.
 */
public class AuthService {
    private final CustomerService customerService;
    private final StaffService staffService;
    private final Map<String, User> activeSessions = new HashMap<>();

    /**
     * Constructs an AuthService with the specified customer and staff services.
     *
     * @param customerService the service for customer data access
     * @param staffService    the service for staff data access
     */
    public AuthService(CustomerService customerService, StaffService staffService) {
        this.customerService = customerService;
        this.staffService = staffService;
    }


    /**
     * Logs in a user by verifying their username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the logged-in user
     * @throws ServiceException if login fails due to invalid credentials or inactive account
     */
    public User login(String username, String password) throws ServiceException {
        if (username == null || username.isBlank()) {
            throw new ServiceException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new ServiceException("Password cannot be empty");
        }

        // Check staff first
        Staff staff = staffService.getStaffByUsername(username);
        if (staff != null) {
            if (!staff.verifyPassword(password)) {
                throw new ServiceException("Invalid password for staff account");
            }
            if (!staff.isActive()) {
                throw new ServiceException("Staff account is inactive");
            }
            activeSessions.put(username, staff);
            return staff;
        }

        // Check customers
        Customer customer = customerService.findCustomerByUsername(username);
        if (customer != null) {
            if (!customer.verifyPassword(password)) {
                throw new ServiceException("Invalid password for customer account");
            }
            if (!customer.isActive()) {
                throw new ServiceException("Customer account is inactive");
            }
            activeSessions.put(username, customer);
            return customer;
        }

        throw new ServiceException("No user found with that username");
    }


    /**
     * Logs out a user by removing their session.
     *
     * @param username the username of the user to log out
     * @throws ServiceException if the user is not logged in
     */
    public void logout(String username) throws ServiceException {
        if (activeSessions.remove(username) == null) {
            throw new ServiceException("User was not logged in");
        }
    }

    /**
     * Checks if a user is logged in.
     *
     * @param username the username of the user
     * @return true if the user is logged in, false otherwise
     */
    public boolean isLoggedIn(String username) {
        return activeSessions.containsKey(username);
    }

    /**
     * Gets the currently logged-in user.
     *
     * @param username the username of the user
     * @return the logged-in user, or null if not logged in
     */
    public User getCurrentUser(String username) {
        return activeSessions.get(username);
    }
}