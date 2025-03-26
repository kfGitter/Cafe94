import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {

    public Report generateReport(ReportType reportType, List<Order> orders, List<Staff> staffMembers) {
        switch (reportType) {
            case BUSIEST_PERIOD:
                System.out.println(" Busiest Period: " + getBusiestPeriod(orders));
                break;
            case POPULAR_ITEM:
                System.out.println(" Most Popular Item: " + getPopularItem(orders));
                break;
            case HIGHEST_WORKING_HOURS:
                System.out.println(" Highest Worked Hours: " + getHighestWorkingHours(staffMembers) + " hours");
                break;
            case MOST_ACTIVE_CUSTOMER:
                System.out.println(" Most Active Customer: " + getMostActiveCustomer(orders));
                break;
            default:
                System.out.println(" Invalid report type.");
        }
        return this;
    }

//--------------------------------------------------------------------------------------

    public LocalDateTime getBusiestPeriod(List<Order> orders) {
        Map<LocalDateTime, Integer> orderCounts = new HashMap<>();

        for (Order order : orders) {
            LocalDateTime orderTime = order.getOrderTime();
            orderCounts.put(orderTime, orderCounts.getOrDefault(orderTime, 0) + 1);
        }

        LocalDateTime busiestTime = null;
        int maxOrders = 0;

        for (Map.Entry<LocalDateTime, Integer> entry : orderCounts.entrySet()) {
            if (entry.getValue() > maxOrders) {
                busiestTime = entry.getKey();
                maxOrders = entry.getValue();
            }
        }
        return busiestTime;
    }


    public Item getPopularItem(List<Order> orders) {
        Map<Item, Integer> itemCounts = new HashMap<>();

        for (Order order : orders) {
            for (Item item : order.getItems()) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            }
        }

        Item mostPopularItem = null;
        int maxCount = 0;

        for (Map.Entry<Item, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostPopularItem = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return mostPopularItem;
    }


    public int getHighestWorkingHours(List<Staff> staffMembers) {
        int maxHours = 0;

        for (Staff staff : staffMembers) {
            if (staff.getTotalHoursWorked() > maxHours) {
                maxHours = staff.getTotalHoursWorked();
            }
        }
        return maxHours;
    }


    public Customer getMostActiveCustomer(List<Order> orders) {
        Map<Customer, Integer> customerOrders = new HashMap<>();

        for (Order order : orders) {
            Customer customer = order.getCustomer();
            customerOrders.put(customer, customerOrders.getOrDefault(customer, 0) + 1);
        }

        Customer mostActiveCustomer = null;
        int maxOrders = 0;

        for (Map.Entry<Customer, Integer> entry : customerOrders.entrySet()) {
            if (entry.getValue() > maxOrders) {
                mostActiveCustomer = entry.getKey();
                maxOrders = entry.getValue();
            }
        }
        return mostActiveCustomer;
    }
}
