package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.*;
import cafe.ninetyfour.enums.OrderStatus;


/**
 * Represents an eat-in order where customers dine at the cafe.
 * Extends the base Order class with table number information.
 */
public class EatInOrder extends Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tableNumber;

    /**
     * Constructs a new EatInOrder with the specified details.
     *
     * @param customerId the ID of the customer placing the order
     * @param items the list of items being ordered
     * @param tableNumber the table number where the customer is seated
     */
    public EatInOrder(int customerId, List<Item> items, int tableNumber) {
        super(customerId, items);
        this.tableNumber = tableNumber;
    }


    /**
     * Processes the eat-in order by updating its status to IN_PROGRESS.
     */
    @Override
    public void processOrder() {
        System.out.println("Processing Eat-In Order for Table " + tableNumber);
        this.updateStatus(OrderStatus.IN_PROGRESS);
    }

    /**
     * Returns a string representation of the eat-in order.
     * @return a formatted string with order details including table number
     */
    @Override
    public String toString() {
        return super.toString() + ", Table: " + tableNumber;
    }
}
