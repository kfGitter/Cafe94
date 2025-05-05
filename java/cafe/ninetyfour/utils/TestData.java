package cafe.ninetyfour.utils;


import cafe.ninetyfour.models.*;
import cafe.ninetyfour.services.CustomerService;
import cafe.ninetyfour.services.StaffService;
import cafe.ninetyfour.enums.UserRole;

public class TestData {
    public static void initializeTestUsers(CustomerService customerService, StaffService staffService) {
        try {
            // Create test customers
            Customer testCustomer1 = customerService.registerCustomer(
                    "test_customer1", "password123!!",
                    "Ethan", "Tari", "123 Test St", "customer1@test.com");

            Customer testCustomer2 = customerService.registerCustomer(
                    "test_customer2", "password@123",
                    "Suivan", "Ryu", "456 Test Ave", "customer2@test.com");

            // Create test staff members
            staffService.addStaff(new Manager(
                    "test_manager", "manager?123",
                    40, "Milan", "Khan"));

            staffService.addStaff(new Chef(
                    "test_chef", "chef@123",
                    35, "Gordon", "Ramsay"));

            staffService.addStaff(new Waiter(
                    "test_waiter", "!waiter123",
                    21, "Fetter", "Bark`"));

            staffService.addStaff(new DeliveryDriver(
                    "test_driver", "driver*123",
                    12, "John", "Doye"));

            System.out.println("Successfully initialized test users");
        } catch (Exception e) {
            System.err.println("Failed to initialize test users: " + e.getMessage());
            e.printStackTrace();
        }
    }
}