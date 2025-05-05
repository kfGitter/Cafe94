package cafe.ninetyfour.models;

import java.util.*;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.services.OrderService;
import cafe.ninetyfour.services.CustomerService;
import cafe.ninetyfour.services.StaffService;
import cafe.ninetyfour.enums.ItemCategory;
import cafe.ninetyfour.enums.ReportType;


public class Main {
    public static void main(String[] args) throws ServiceException {
        // Initialize services
        CustomerService customerService = new CustomerService();
        OrderService orderService = new OrderService();
        StaffService staffService = new StaffService();
        Menu menu = new Menu();
        ReportGenerator reportGenerator = new ReportGenerator(orderService, staffService, customerService);

        // ===== 1. SETUP PHASE =====
        // Add menu items
        Item pasta = new Item("Pasta", ItemCategory.MAIN, 12.99);
        Item pizza = new Item("Pizza", ItemCategory.MAIN, 14.99);
        Item salad = new Item("Salad", ItemCategory.SIDE, 5.99);
        menu.addItem(pasta);
        menu.addItem(pizza);
        menu.addItem(salad);

        // Register customers

        Customer customer1 = new Customer("john_doe", "password123", "John", "Doe", "123 Main St");
        Customer customer2 = new Customer("jane_doe", "password456", "Jane", "Doe", "456 Elm St");
        // Hire staff
        Manager manager = new Manager("boss2000", "admin123!!!", 40, "Amir", "Khan");
        manager.setStaffService(staffService);
        manager.setReportGenerator(reportGenerator);

        Chef chef = new Chef("master_chef", "cook123!!", 50, "Gordon", "Ramsay");
        chef.setOrderService(orderService);
        chef.setMenu(menu);

        Waiter waiter = new Waiter("server1", "serve123!!", 35, "Alice", "Smith");
        waiter.setOrderService(orderService);

        staffService.addStaff(manager);
        staffService.addStaff(chef);
        staffService.addStaff(waiter);

        // ===== 2. ORDER PROCESSING =====
        // Create orders
        Order eatInOrder = new EatInOrder(
                customer1.getUserId(),
                List.of(pasta, salad),
                4 // tableNumber
        );
        orderService.placeOrder(eatInOrder);

        DeliveryOrder deliveryOrder = new DeliveryOrder(
                customer2.getUserId(),
                List.of(pizza),
                "789 Pine Rd",
                45 // duration
        );
        orderService.placeOrder(deliveryOrder);

        // ===== 3. STAFF ACTIONS =====
        // Waiter approves delivery
        waiter.approveDeliveryOrder(deliveryOrder.getOrderId(), 101); // driverId
        /* Output:
        Delivery order approved with driver 101
        */

        // Chef marks order complete
        chef.markOrderComplete(eatInOrder.getOrderId());
        /* Output:
        Order 1 status updated to COMPLETED
        */

        // Chef creates daily special
        chef.createDailySpecial("Steak", ItemCategory.MAIN, 24.99);
        /* Output:
        Item 'Steak' added to menu.
        Today's Daily Special: Steak
        */

        // ===== 4. REPORTING =====
        manager.generateReport(ReportType.MOST_ACTIVE_CUSTOMER);
        /* Output:
        Most Active Customer: John Doe (ID: 1)
        */

        manager.generateReport(ReportType.POPULAR_ITEM);
        /* Output:
        Most Popular Item: Pizza
        */

        // ===== 5. SYSTEM STATE CHECKS =====
        // View all orders
        System.out.println("\nAll Orders:");
        orderService.getAllOrders().forEach(System.out::println);
        /* Output:
        Order #1 (Customer: 1) - COMPLETED - $18.98, Table: 4
        Order #2 (Customer: 2) - APPROVED - $14.99, Address: 789 Pine Rd, Driver: 101
        */

        // View menu
        menu.browseMenu();
        /* Output:
        Welcome to the Menu!
        Today's Daily Special: Steak

        src.cafe.ninetyfour.Main Courses:
        Pasta - $12.99
        Pizza - $14.99
        Steak - $24.99

        Sides:
        Salad - $5.99

        Bon Appétit!
        */

        // View staff hours
        chef.logWorkedHours(8);
        /* Output:
        master_chef worked for 8 hours. Total: 8
        */
    }
}