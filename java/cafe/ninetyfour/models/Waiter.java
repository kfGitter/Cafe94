package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.*;

import cafe.ninetyfour.services.OrderService;
import cafe.ninetyfour.services.BookingService;
import cafe.ninetyfour.enums.UserRole;
import cafe.ninetyfour.enums.OrderStatus;

/**
 * Represents a waiter in the cafe system with responsibilities for
 * order and booking management.
 */
public class Waiter extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    private OrderService orderService;
    private BookingService bookingService;


    /**
     * Constructor for creating a Waiter object.
     *
     * @param username    the username of the waiter
     * @param password    the password of the waiter
     * @param hoursToWork the number of hours the waiter is scheduled to work
     * @param firstName   the first name of the waiter
     * @param lastName    the last name of the waiter
     */
    public Waiter(String username, String password, int hoursToWork,
                  String firstName, String lastName) {
        super(username, password, UserRole.WAITER, hoursToWork, firstName, lastName);
    }

    /**
     * Sets the OrderService dependency for order management operations.
     *
     * @param orderService the OrderService implementation to use
     * @throws NullPointerException if orderService is null
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService);
    }


    /**
     * Sets the BookingService dependency for booking management.
     *
     * @param bookingService the BookingService implementation to use
     * @throws NullPointerException if bookingService is null
     */
    public void setBookingService(BookingService bookingService) {
        this.bookingService = Objects.requireNonNull(bookingService);
    }

    /**
     * Approves a booking request.
     *
     * @param bookingId the ID of the booking to approve
     * @return true if the booking was successfully approved
     */
    public boolean approveBooking(int bookingId) {
        return bookingService.approveBooking(bookingId);
    }



    /**
     * Approves a delivery order and assigns a driver.
     *
     * @param orderId the ID of the delivery order to approve
     * @param driverId the ID of the driver to assign
     * @return true if the order was successfully approved and assigned
     */
    public boolean approveDeliveryOrder(int orderId, int driverId) {
        Order order = orderService.getOrderById(orderId).get(0);
        if (order instanceof DeliveryOrder deliveryOrder) {
            deliveryOrder.assignDriver(driverId);
            orderService.updateStatus(orderId, OrderStatus.APPROVED);
            System.out.println(" Delivery order " + orderId + " approved by waiter " + this.getUserId() + ". Assigned driver: " + driverId + ".");
            return true;
        }
        return false;
    }
}

