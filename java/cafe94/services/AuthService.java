package cafe94.services;


import java.util.*;
import cafe94.models.User;

public class AuthService {
    private final Map<String, User> registeredUsers = new HashMap<>();  // Changed to username-based
    private final Map<String, User> activeSessions = new HashMap<>();   // Tracks current logins

    // -------------------------------------------------------------------
    // Register a new user
    public void register(User user) {
        if (registeredUsers.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        registeredUsers.put(user.getUsername(), user);
    }

    // -------------------------------------------------------------------
    // Login with username/password
    public User login(String username, String password) {
        User user = registeredUsers.get(username);
        if (user != null && user.verifyPassword(password)) {
            activeSessions.put(username, user);
            return user;  // Return logged-in user
        }
        throw new SecurityException("Invalid credentials");
    }

    // Logout
    public void logout(String username) {
        if (activeSessions.remove(username) == null) {
            throw new IllegalStateException("User was not logged in");
        }
    }

    // Check if user is logged in
    public boolean isLoggedIn(String username) {
        return activeSessions.containsKey(username);
    }

    // Get logged-in user
//    public User getCurrentUser(String username) {
//        return activeSessions.get(username);
//    }
}