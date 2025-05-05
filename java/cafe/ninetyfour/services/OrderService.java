package cafe.ninetyfour.services;

import java.util.*;
import java.io.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.enums.OrderStatus;
import cafe.ninetyfour.models.Order;


/** OrderService class is responsible for managing orders in the cafe system.
 * It handles order placement, status tracking, and persistence.
 */

public class OrderService {
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());
    private static final String ORDERS_FILE = "orders.dat";
    private List<Order> allOrders;
    private final Map<Integer, Order> ordersById;
    private int nextOrderId = 1;


    /**
     * Constructor for OrderService.
     * Initializes the order list and loads existing orders from file.
     */
    public OrderService() {
        this.allOrders = new CopyOnWriteArrayList<>();
        this.ordersById = new ConcurrentHashMap<>();  // Thread-safe map
        loadOrders();  // Load orders from file on startup
    }

    // Persistence methods
    /**
     * Saves the current orders to a file.
     *
     * @throws ServiceException if an error occurs during saving
     */
    public synchronized void saveOrders() throws ServiceException {
        try {
            new File("data").mkdirs();
            File tempFile = new File(ORDERS_FILE + ".tmp");

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(tempFile))) {
                oos.writeObject(new ArrayList<>(allOrders));
            }

            File dataFile = new File(ORDERS_FILE);
            if (dataFile.exists() && !dataFile.delete()) {
                throw new ServiceException("Could not delete old orders file");
            }
            if (!tempFile.renameTo(dataFile)) {
                throw new ServiceException("Could not rename temp orders file");
            }
        } catch (IOException e) {
            throw new ServiceException("Failed to save orders", e);
        }
    }


    /**
     * Loads orders from a file into the service.
     */
    private void loadOrders() {
        File file = new File(ORDERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Order> loaded = (List<Order>) ois.readObject();

                // Reset ID counter
                this.nextOrderId = loaded.stream()
                        .mapToInt(Order::getOrderId)
                        .max()
                        .orElse(0) + 1;

                // Reload data
                allOrders.clear();
                ordersById.clear();
                loaded.forEach(order -> {
                    allOrders.add(order);
                    ordersById.put(order.getOrderId(), order);
                });
            } catch (Exception e) {
                System.err.println("Error loading orders: " + e.getMessage());
            }
        }
    }

    public synchronized void saveAndReload() throws ServiceException {
        saveOrders();
        allOrders.clear();
        ordersById.clear();
        loadOrders();
    }

    /**
     * Places a new order.
     *
     * @param order the order to be placed
     * @return the placed order with updated ID
     * @throws ServiceException if an error occurs during order placement
     */
    public synchronized Order placeOrder(Order order) throws ServiceException {
        try {
            Objects.requireNonNull(order, "Order cannot be null");
            if (order.getItems() == null || order.getItems().isEmpty()) {
                throw new ServiceException("Order must contain at least one item");
            }

            order.setOrderId(nextOrderId++);
            allOrders.add(order);
            ordersById.put(order.getOrderId(), order);
            saveOrders();
            logger.info("Placed new order ID: " + order.getOrderId());
            return order;
        } catch (Exception e) {
            logger.severe("Error loading order: " + e.getMessage());
            throw new ServiceException("Failed to place order", e);
        }
    }


    //method to find orders by status
    /**
     * Retrieves all orders with the specified status.
     *
     * @param status the status to filter orders by
     * @return a list of orders with the specified status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return allOrders.stream()
                .filter(order -> order.getStatus() == status)
                .toList();
    }

    // Optimized status tracking
    /**
     * Tracks the status of an order by its ID.
     *
     * @param orderId the ID of the order to track
     * @return the status of the order, or null if not found
     */
    public OrderStatus trackStatus(int orderId) {
        Order order = ordersById.get(orderId);
        return order != null ? order.getStatus() : null;
    }

    // Get all orders (immutable view)
    /**
     * Retrieves all orders in the system.
     *
     * @return an unmodifiable list of all orders
     */
    public List<Order> getAllOrders() {
        return Collections.unmodifiableList(allOrders);
    }

    /**
     * Retrieves all orders for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of orders for the specified customer
     */
    // Get orders by customer (using stream)
    public List<Order> getOrdersByCustomer(int customerId) {
        return allOrders.stream()
                .filter(order -> order.getCustomerId() == customerId)
                .toList();
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return a list of orders with the specified ID
     */
    public List<Order> getOrderById(int orderId) {
        return allOrders.stream()
                .filter(order -> order.getOrderId() == orderId)
                .toList();
    }

    // Optimized status update using map
    /**
     * Updates the status of an order.
     *
     * @param orderId   the ID of the order to update
     * @param newStatus the new status to set
     * @return true if the status was successfully updated, false otherwise
     */
    public boolean updateStatus(int orderId, OrderStatus newStatus) {
        Order order = ordersById.get(orderId);
        if (order != null) {
            order.updateStatus(newStatus);
            return true;
        }
        return false;
    }

    // Display outstanding orders
    /**
     * Displays all outstanding orders.
     */
    public void displayOutstandingOrders() {
        System.out.println("Outstanding Orders:");
        allOrders.stream()
                .filter(order -> order.getStatus() != OrderStatus.COMPLETED)
                .forEach(System.out::println);
    }

    // Process an order
    /**
     * Processes an order by its ID.
     *
     * @param orderId the ID of the order to process
     * @return true if the order was successfully processed, false otherwise
     */
    public boolean processOrder(int orderId) {
        Order order = ordersById.get(orderId);
        if (order != null) {
            order.processOrder();
            return true;
        }
        return false;
    }
}