package cafe94.models;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import cafe94.services.OrderService;
import cafe94.services.StaffService;
import cafe94.services.CustomerService;
import cafe94.enums.ReportType;

public class ReportGenerator {
    private final OrderService orderService;
    private final StaffService staffService;
    private final CustomerService customerService;

    public ReportGenerator(OrderService orderService,
                           StaffService staffService,
                           CustomerService customerService) {
        this.orderService = Objects.requireNonNull(orderService);
        this.staffService = Objects.requireNonNull(staffService);
        this.customerService = Objects.requireNonNull(customerService);
    }

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

    private int findTopWorkerHours() {
        return staffService.getAllStaff().stream()
                .mapToInt(Staff::getTotalWorkedHours)
                .max()
                .orElse(0);
    }

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
