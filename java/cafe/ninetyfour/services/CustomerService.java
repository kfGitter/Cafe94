package cafe.ninetyfour.services;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.models.Customer;
import cafe.ninetyfour.models.Staff;
import cafe.ninetyfour.models.User;

/**
 * CustomerService class is responsible for managing customer data,
 * including registration, retrieval, and persistence.
 */
public class CustomerService {
    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private static final String CUSTOMERS_FILE = "data/customers.dat";

    // Primary collection (username -> Customer)
    private final Map<String, Customer> customersByUsername;

    // Secondary index (id -> Customer)
    private final Map<Integer, Customer> customersById;


    private int nextCustomerId = 1;

    /**
     * Constructor for CustomerService.
     * Initializes the customer collections and loads existing customers from file.
     */
    public CustomerService() {
        this.customersByUsername = new HashMap<>();
        this.customersById = new HashMap<>();
        loadCustomers();  // Load customers from file on startup
    }

    // Persistence Methods
    public synchronized void saveCustomers() throws ServiceException {
        try {
            // Ensure directory exists
            new File("data").mkdirs();

            // Create temp file
            File tempFile = new File(CUSTOMERS_FILE + ".tmp");

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(tempFile))) {
                oos.writeObject(new ArrayList<>(customersByUsername.values()));
            }

            // Atomic replace
            File dataFile = new File(CUSTOMERS_FILE);
            if (dataFile.exists() && !dataFile.delete()) {
                throw new ServiceException("Could not delete old customer file");
            }
            if (!tempFile.renameTo(dataFile)) {
                throw new ServiceException("Could not rename temp customer file");
            }
        } catch (IOException e) {
            throw new ServiceException("Failed to save customer data", e);
        }
    }

    /**
     * Loads customers from a file into the service.
     * If the file does not exist or is corrupt, initializes default customers.
     */
    @SuppressWarnings("unchecked")
    private void loadCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Customer> loaded = (List<Customer>) ois.readObject();

                // Reset ID counter
                this.nextCustomerId = loaded.stream()
                        .mapToInt(Customer::getUserId)
                        .max()
                        .orElse(0) + 1;

                // Clear and reload
                customersByUsername.clear();
                customersById.clear();

                loaded.forEach(c -> {
                    customersByUsername.put(c.getUsername().toLowerCase(), c);
                    customersById.put(c.getUserId(), c);
                });

                logger.info("Successfully loaded " + loaded.size() + " customers");
            } catch (Exception e) {
                logger.severe("Error loading customers: " + e.getMessage());
                // Consider creating empty file if corrupt
                initializeDefaultCustomers();
            }
        } else {
            logger.info("No customer data file found, starting with empty dataset");
            initializeDefaultCustomers();
        }
    }

    /**
     * Initializes default customers for the system.
     * This method is called when no customer data file is found or if loading fails.
     */
    private void initializeDefaultCustomers() {
        try {
            // Optionally create some default users
            Customer adminCustomer = new Customer(
                    "admin", "admin123",
                    "System", "Admin", "Online"
            );
            customersByUsername.put("admin", adminCustomer);
            customersById.put(adminCustomer.getUserId(), adminCustomer);
            saveCustomers();
        } catch (Exception e) {
            logger.severe("Failed to initialize default customers: " + e.getMessage());
        }
    }


    /**
     * Returns the next available customer ID.
     * This method is synchronized to ensure thread safety.
     *
     * @return the next customer ID
     */
    // Add this method to ensure proper synchronization
    public synchronized void saveAndReload() throws ServiceException {
        saveCustomers();
        // Clear current data and reload from file
        customersByUsername.clear();
        customersById.clear();
        loadCustomers();
    }

    // Business Methods


    /**
     * Registers a new customer in the system.
     * Validates input and checks for existing username.
     *
     * @param username  the username of the new customer
     * @param password  the password of the new customer
     * @param firstName the first name of the new customer
     * @param lastName  the last name of the new customer
     * @param address   the address of the new customer
     * @param email     the email of the new customer
     * @return the newly registered Customer object
     * @throws ServiceException if registration fails due to
     * invalid input or existing username
     */
    public Customer registerCustomer(String username, String password,
                                     String firstName, String lastName,
                                     String address, String email)
            throws ServiceException {
        try {
            // Validate and create customer
            User.validateRegistrationInput(username, password, firstName, lastName, email);

            if (customersByUsername.containsKey(username.toLowerCase())) {
                throw new ServiceException("Username already exists");
            }

            Customer newCustomer = new Customer(
                    username, password,
                    firstName, lastName, address
            );

            // Store and persist
            customersByUsername.put(username.toLowerCase(), newCustomer);
            customersById.put(newCustomer.getUserId(), newCustomer);
            saveCustomers(); // Ensure immediate persistence

            return newCustomer;
        } catch (Exception e) {
            logger.severe("Error registering customer: " + e.getMessage());
            throw new ServiceException("Failed to register customer", e);
        }
    }

    /**
     * Finds a customer by their username.
     *
     * @param username the username of the customer to find
     * @return the Customer object if found, null otherwise
     * @throws NullPointerException if the username is null
     */
    public Customer findCustomerByUsername(String username) {
        Objects.requireNonNull(username, "Username cannot be null");
        return customersByUsername.get(username);
    }

    /**
     * Finds a customer by their ID.
     *
     * @param customerId the ID of the customer to find
     * @return the Customer object if found, null otherwise
     * @throws IllegalArgumentException if the ID is invalid
     */
    public Customer findCustomerById(int customerId) {
        if (customerId <= 0) throw new IllegalArgumentException("Invalid ID");
        return customersById.get(customerId);
    }

    public boolean customerExists(String username) {
        return customersByUsername.containsKey(username);
    }


    /**
     * Retrieves all customers in the system.
     *
     * @return a list of all Customer objects
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customersById.values());
    }
}