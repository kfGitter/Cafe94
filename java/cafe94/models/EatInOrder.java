package cafe94.models;

import java.util.*;
import cafe94.enums.OrderStatus;


public class EatInOrder extends Order {
    private int tableNumber;

    public EatInOrder(int customerId, List<Item> items, int tableNumber) {
        super(customerId, items);
        this.tableNumber = tableNumber;
    }

    @Override
    public void processOrder() {
        System.out.println("Processing Eat-In Order for Table " + tableNumber);
        this.updateStatus(OrderStatus.IN_PROGRESS);
    }

    @Override
    public String toString() {
        return super.toString() + ", Table: " + tableNumber;
    }
}
