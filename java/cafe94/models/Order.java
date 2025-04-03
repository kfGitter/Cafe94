package cafe94.models;

import java.time.LocalDateTime;
import java.util.List;

import cafe94.enums.OrderStatus;
import cafe94.services.CustomerService;

public abstract class Order {
    protected  int orderId;
    protected final int customerId;
    protected final List<Item> items;
    protected OrderStatus status;
    protected final LocalDateTime orderTime;
    protected final double totalPrice;
    protected boolean isApproved;

    public Order(int customerId, List<Item> items) {
        this.orderId = -1; // Default value, will be set later
        this.customerId = customerId;
        this.items = List.copyOf(items); // Defensive copy
        this.status = OrderStatus.PENDING;
        this.orderTime = LocalDateTime.now();
        this.totalPrice = calculateTotal();
        this.isApproved = false;
    }


    public double calculateTotal() {
        return items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }

    public void updateStatus(OrderStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null");
        this.status = status;
    }

    public void approveOrder() {
        this.isApproved = true;
        updateStatus(OrderStatus.IN_PROGRESS);
    }

    // Getters
    public Customer getCustomer(CustomerService service) {
        return service.findCustomerById(customerId);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Setters
    public void setOrderId(int orderId) {
        if (orderId <= 0) throw new IllegalArgumentException("Invalid order ID");
        this.orderId = orderId;
    }

    // Abstract method for order-specific processing
    public abstract void processOrder();

    @Override
    public String toString() {
        return String.format("Order #%d (Customer: %d) - %s - $%.2f",
                orderId, customerId, status, totalPrice);
    }

}