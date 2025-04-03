package cafe94.models;

import java.util.*;
import cafe94.enums.UserRole;


public class Customer extends User {
    private final List<Order> orderHistory;

    public Customer(String username, String password, String firstName,
                    String lastName, String address) {
        super(username, password, UserRole.CUSTOMER);
        this.setFirstName(Objects.requireNonNull(firstName));
        this.setLastName(Objects.requireNonNull(lastName));
        this.setAddress(Objects.requireNonNull(address));
        this.orderHistory = new ArrayList<>();
    }

    public int getCustomerId() {
        return this.getUserId();
    }

    public List getOrderHistory() {
        return orderHistory;
    }

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

    public DeliveryOrder requestDeliveryOrder(List<Item> items, String deliveryAddress) {
        validateItems(items);
        if (deliveryAddress == null || deliveryAddress.isBlank()) {
            throw new IllegalArgumentException("Delivery address required");
        }

        DeliveryOrder newOrder = new DeliveryOrder(
                getUserId(),
                items,
                deliveryAddress,
                30
        );


        orderHistory.add(newOrder);
        return newOrder; // Note: Needs separate approval by waiter
    }

    private void validateItems(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain items");
        }
    }

    public void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No past orders found.");
            return;
        }

        System.out.println("\n=== Your Order History ===");
        orderHistory.forEach(order ->
                System.out.printf("Order #%d - %s - Total: £%.2f%n",
                        order.getOrderId(),
                        order.getStatus(),
                        order.calculateTotal())
        );
    }

    // Consider adding this for order lookup
    public Order getOrderById(int orderId) {
        return orderHistory.stream()
                .filter(o -> o.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }
}