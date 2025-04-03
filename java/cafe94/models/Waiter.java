package cafe94.models;

import java.util.*;
import cafe94.services.OrderService;
import cafe94.services.BookingService;
import cafe94.enums.UserRole;
import cafe94.enums.OrderStatus;

public class Waiter extends Staff {
    private  OrderService orderService;
    private  BookingService bookingService;

    public Waiter(String username, String password, int hoursToWork) {
        super(username, password, UserRole.WAITER, hoursToWork);
    }

    // Dependency injection via setters
    public void setOrderService(OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService);
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = Objects.requireNonNull(bookingService);
    }

    public boolean approveBooking(int bookingId) {
        return bookingService.approveBooking(bookingId);
    }

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

