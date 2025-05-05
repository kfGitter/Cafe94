package cafe.ninetyfour.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import cafe.ninetyfour.services.OrderService;
import cafe.ninetyfour.services.StaffService;
import cafe.ninetyfour.services.CustomerService;
import cafe.ninetyfour.enums.ReportType;

/**
 * ReportGenerator class is responsible for generating various reports
 * based on the data from OrderService, StaffService, and CustomerService.
 */
public class ReportGenerator implements Serializable {
    private static final long serialVersionUID = 1L;
    private final OrderService orderService;
    private final StaffService staffService;
    private final CustomerService customerService;

    /**
     * Constructs a new ReportGenerator with required services.
     *
     * @param orderService service for order data access
     * @param staffService service for staff data access
     * @param customerService service for customer data access
     * @throws NullPointerException if any service is null
     */
    public ReportGenerator(OrderService orderService,
                           StaffService staffService,
                           CustomerService customerService) {
        this.orderService = Objects.requireNonNull(orderService);
        this.staffService = Objects.requireNonNull(staffService);
        this.customerService = Objects.requireNonNull(customerService);
    }

    /**
     * Generates a report based on the specified report type.
     *
     * @param reportType the type of report to generate
     */
    public void generateReport(ReportType reportType) {
        switch (reportType) {
            case BUSIEST_PERIOD:
                System.out.println("Busiest Hour: " + findBusiestHour());
                break;
            case POPULAR_ITEM:
                System.out.println("Most Popular: " + findPopularItem());
                break;
            case HIGHEST_WORKING_HOURS:
                System.out.println("Top Worker Hours: " + findTopWorkerHours());
                break;
            case MOST_ACTIVE_CUSTOMER:
                System.out.println("Most Active: " + findMostActiveCustomer());
                break;
            default:
                System.out.println("Invalid report type");
        }
    }

    /**
     * Finds the cafe's busiest hour based on order volume.
     * @return the LocalTime representing the busiest hour
     */
    private LocalTime findBusiestHour() {
        return orderService.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderTime().toLocalTime().withMinute(0),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }



    /**
     * Finds the most popular menu item based on order frequency.
     * @return the most ordered Item
     */
    private Item findPopularItem() {
        return orderService.getAllOrders().stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    /**
     * Finds the staff member with the highest total worked hours.
     * @return the total worked hours of the top worker
     */

    private int findTopWorkerHours() {
        return staffService.getAllStaff().stream()
                .mapToInt(Staff::getTotalWorkedHours)
                .max()
                .orElse(0);
    }


    /**
     * Finds the most active customer based on order count.
     * @return the Customer with the most orders
     */

    private Customer findMostActiveCustomer() {
        Map<Integer, Long> customerOrderCounts = orderService.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomerId,
                        Collectors.counting()
                ));

        return customerOrderCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> customerService.findCustomerById(entry.getKey()))
                .orElse(null);
    }
}
