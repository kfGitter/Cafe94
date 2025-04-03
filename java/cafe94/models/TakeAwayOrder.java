package cafe94.models;

import java.util.*;
import cafe94.enums.OrderStatus;

public class TakeAwayOrder extends Order {

    public TakeAwayOrder(int customerId, List<Item> items) {
        super(customerId, items);
    }

    @Override
    public void processOrder() {
        System.out.println("Processing Takeaway Order. Get Ready for Pickup!");
        this.updateStatus(OrderStatus.COMPLETED);
    }
}
