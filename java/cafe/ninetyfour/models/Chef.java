package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.*;

import cafe.ninetyfour.enums.ItemCategory;
import cafe.ninetyfour.enums.OrderStatus;
import cafe.ninetyfour.enums.UserRole;
import cafe.ninetyfour.services.OrderService;

/**
 * Represents a chef in the cafe system with responsibilities for
 * order preparation and menu management.
 */
public class Chef extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    private OrderService orderService;
    private  Menu menu;


    /**
     * Constructs a new Chef with the specified details.
     *
     * @param username the chef's username
     * @param password the chef's password
     * @param hoursToWork the chef's contracted work hours
     */
    public Chef(String username, String password, int hoursToWork, String firstName, String lastName) {
        super(username, password, UserRole.CHEF, hoursToWork, firstName, lastName);
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
     * Sets the Menu dependency for menu management operations.
     *
     * @param menu the Menu implementation to use
     * @throws NullPointerException if menu is null
     */
    public void setMenu(Menu menu) {
        this.menu = Objects.requireNonNull(menu);
    }


    /**
     * Marks an order as completed in the system.
     * Mainly used by Chef
     * @param orderId the ID of the order to mark as complete
     * @return true if the status was successfully updated
     */
    public boolean markOrderComplete(int orderId) {
        System.out.println("Chef marking order " + orderId + " as complete.");
        return orderService.updateStatus(orderId, OrderStatus.COMPLETED);
    }

    /**
     * Sets an existing menu item as the daily special.
     *
     * @param item the item to set as daily special
     */
    public void setDailySpecial(Item item) {
        System.out.println("Chef setting daily special: " + item.getName());
        menu.setDailySpecial(item);
    }

    /**
     * Creates a new menu item and sets it as the daily special.
     *
     * @param name the name of the new special item
     * @param category the category of the new item
     * @param price the price of the new item
     */
    public void createDailySpecial(String name, ItemCategory category,
                                   double price) {
        Item special = new Item(name, category, price);
        menu.addItem(special);
        menu.setDailySpecial(special);
    }

}