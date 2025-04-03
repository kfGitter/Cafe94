package cafe94.services;

import java.util.*;
import cafe94.models.Customer;

public class CustomerService {
    // Primary collection (username -> Customer)
    private final Map<String, Customer> customersByUsername;

    // Secondary index (id -> Customer)
    private final Map<Integer, Customer> customersById;
    private int nextCustomerId = 1;

    public CustomerService() {
        this.customersByUsername = new HashMap<>();
        this.customersById = new HashMap<>();
    }

    // Registration handles both mappings
    public Customer registerCustomer(String username, String password,
                                     String firstName, String lastName,
                                     String address) {
        if (customersByUsername.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Customer newCustomer = new Customer(
                username, password,
                firstName, lastName, address
        );

        customersByUsername.put(username, newCustomer);
        customersById.put(newCustomer.getCustomerId(), newCustomer);
        return newCustomer;
    }

// Lookup methods
//    public Customer findCustomerByUsername(String username) {
//        return customersByUsername.get(username);
//    }

    public Customer findCustomerById(int customerId) {
        return customersById.get(customerId);
    }

    // Existing methods (now use customersByUsername)
    public boolean customerExists(String username) {
        return customersByUsername.containsKey(username);
    }
}