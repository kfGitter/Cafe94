package cafe94.models;

import java.util.*;

import cafe94.enums.ItemCategory;
import cafe94.enums.OrderStatus;
import cafe94.enums.UserRole;
import cafe94.services.OrderService;

public class Chef extends Staff {
    private OrderService orderService;
    private  Menu menu;

    public Chef(String username, String password, int hoursToWork) {
        super(username, password, UserRole.CHEF, hoursToWork);
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService);
    }

    public void setMenu(Menu menu) {
        this.menu = Objects.requireNonNull(menu);
    }

    public boolean markOrderComplete(int orderId) {
        System.out.println("Chef marking order " + orderId + " as complete.");
        return orderService.updateStatus(orderId, OrderStatus.COMPLETED);
    }

    public void setDailySpecial(Item item) {
        System.out.println("Chef setting daily special: " + item.getName());
        menu.setDailySpecial(item);
    }

    public void createDailySpecial(String name, ItemCategory category, double price) {
        Item special = new Item(name, category, price);
        menu.addItem(special);
        menu.setDailySpecial(special);
    }

}