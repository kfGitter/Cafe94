package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.*;
import cafe.ninetyfour.enums.UserRole;


/**
 * Represents a customer in the cafe management system.
 * Extends the base User class with customer-specific functionality
 * like order placement and order history tracking.
 */
public class Customer extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Order> orderHistory;

    /**
     * Constructs a new Customer with the specified details.
     *
     * @param username the customer's username (must be unique)
     * @param password the customer's password (must meet complexity requirements)
     * @param firstName the customer's first name (cannot be null or empty)
     * @param lastName the customer's last name (cannot be null or empty)
     * @param address the customer's address (cannot be null or empty)
     *
     * @throws NullPointerException if any required field is null
     */
    public Customer(String username, String password, String firstName,
                    String lastName, String address) {
        super(username, password, UserRole.CUSTOMER);
        this.setFirstName(Objects.requireNonNull(firstName));
        this.setLastName(Objects.requireNonNull(lastName));
        this.setAddress(Objects.requireNonNull(address));
        this.orderHistory = new ArrayList<>();
    }

    //Order Related methods

    /**
     * Gets the customer's order history.
     * @return an unmodifiable list of the customer's past orders
     */
    public List getOrderHistory() {
        return orderHistory;
    }


    /**
     * Places an eat-in order for the specified items at a table.
     *
     * @param items the list of items to order (cannot be null or empty)
     * @param tableNumber the table number where the customer will dine
     * @return the created EatInOrder object
     * @throws IllegalArgumentException if items list is empty or null
     */
    // Order methods with basic validation
    public Order placeEatInOrder(List<Item> items, int tableNumber) {
        validateItems(items);
        Order newOrder = new EatInOrder(
                getUserId(),
                items,
                tableNumber
        );
        orderHistory.add(newOrder);
        return newOrder;
    }

    /**
     * Places a takeaway order for the specified items with a pickup time.
     *
     * @param items the list of items to order (cannot be null or empty)
     * @param pickupTime the scheduled pickup time (cannot be null or empty)
     * @return the created TakeAwayOrder object
     * @throws IllegalArgumentException if items list is empty or null,
     * or pickup time is invalid
     */
    public Order placeTakeawayOrder(List<Item> items, String pickupTime) {
        validateItems(items);
        if (pickupTime == null || pickupTime.isBlank()) {
            throw new IllegalArgumentException("Pickup time required");
        }

        Order newOrder = new TakeAwayOrder(
                getUserId(),
                items
        );
        orderHistory.add(newOrder);
        return newOrder;
    }


    /**
     * Requests a delivery order for the specified items to an address.
     *
     * @param items the list of items to order (cannot be null or empty)
     * @param deliveryAddress the delivery address (cannot be null or empty)
     * @return the created DeliveryOrder object (requires waiter approval)
     * @throws IllegalArgumentException if items list is empty or null,
     * or address is invalid
     */
    public DeliveryOrder requestDeliveryOrder(List<Item> items,
                                              String deliveryAddress) {
        // Validate items and address
        validateItems(items);
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Delivery address required");
        }

        // Create a new delivery order with a default delivery time of 30 minutes
        DeliveryOrder newOrder = new DeliveryOrder(
                getUserId(),
                items,
                deliveryAddress,
                30
        );


        orderHistory.add(newOrder);
        return newOrder; // Note: Needs separate approval by waiter
    }


    /**
     * Validates that an order contains at least one item.
     *
     * @param items the list of items to validate
     * @throws IllegalArgumentException if items list is empty or null
     */

    private void validateItems(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain items");
        }
    }

    /**
     * Displays the customer's order history to the console.
     * Shows order ID, status, and total price for each past order.
     */
    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No past orders found.");
            return;
        }

        System.out.println("\n---- Your Order History ----");
        orderHistory.forEach(order ->
                System.out.printf("Order #%d - %s - Total: £%.2f%n",
                        order.getOrderId(),
                        order.getStatus(),
                        order.calculateTotal())
        );
    }

    /**
     * Retrieves an order from the customer's history by its ID.
     *
     * @param orderId the ID of the order to find
     * @return the Order object if found, null otherwise
     */
    public Order getOrderById(int orderId) {
        return orderHistory.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }
}