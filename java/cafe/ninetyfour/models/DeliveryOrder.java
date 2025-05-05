package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.OrderStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a delivery order where food is delivered to a customer's address.
 * Extends the base Order class with delivery-specific information.
 */
public class DeliveryOrder extends Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String deliveryAddress;
    private final int duration;
    private int driverId;


    /**
     * Constructs a new DeliveryOrder with the specified details.
     *
     * @param customerId      the ID of the customer placing the order
     * @param items           the list of items being ordered
     * @param deliveryAddress the address for delivery (cannot be null)
     * @param duration        the estimated delivery duration in minutes
     * @throws NullPointerException if deliveryAddress is null
     */
    public DeliveryOrder(int customerId, List<Item> items,
                         String deliveryAddress, int duration) {
        super(customerId, items);
        this.deliveryAddress = Objects.requireNonNull(deliveryAddress);
        this.duration = duration;
        this.driverId = -1; // Indicates unassigned
    }

    /**
     * Processes the delivery order after verifying a driver is assigned.
     *
     * @throws IllegalStateException if no driver is assigned
     */
    @Override
    public void processOrder() {
        if (driverId <= 0) {
            throw new IllegalStateException("No driver assigned");
        }
        System.out.printf("Processing delivery to %s (Driver: %d)%n",
                deliveryAddress, driverId);
        this.updateStatus(OrderStatus.IN_PROGRESS);
    }

    /**
     * Assigns a driver to this delivery order.
     *
     * @param driverId the ID of the driver to assign
     * @throws IllegalArgumentException if driverId is invalid
     */
    public void assignDriver(int driverId) {
        if (driverId <= 0) {
            throw new IllegalArgumentException("Invalid Ids");
        }
        this.driverId = driverId;
    }

    /**
     * Returns a string representation of the delivery order.
     *
     * @return a formatted string with delivery details
     */
    @Override
    public String toString() {
        return String.format("%s, Address: %s, Duration: %d mins, Driver: %d",
                super.toString(), deliveryAddress, duration, driverId);
    }
}