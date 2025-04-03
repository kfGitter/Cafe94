package cafe94.models;

import cafe94.enums.OrderStatus;

import java.util.List;
import java.util.Objects;


public class DeliveryOrder extends Order {
    private final String deliveryAddress;
    private final int duration;
    private int driverId;

    public DeliveryOrder(int customerId, List<Item> items,
                         String deliveryAddress, int duration) {
        super(customerId, items);
        this.deliveryAddress = Objects.requireNonNull(deliveryAddress);
        this.duration = duration;
        this.driverId = -1; // Indicates unassigned
    }

    @Override
    public void processOrder() {
        if (driverId <= 0) {
            throw new IllegalStateException("No driver assigned");
        }
        System.out.printf("Processing delivery to %s (Driver: %d)%n",
                deliveryAddress, driverId);
        this.updateStatus(OrderStatus.IN_PROGRESS);
    }

    public void assignDriver(int driverId) {
        if (driverId <= 0) {
            throw new IllegalArgumentException("Invalid IDs");
        }
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return String.format("%s, Address: %s, Duration: %d mins, Driver: %d",
                super.toString(), deliveryAddress, duration, driverId);
    }
}