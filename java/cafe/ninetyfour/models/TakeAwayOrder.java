package cafe.ninetyfour.models;

import java.io.Serializable;
import java.util.*;
import cafe.ninetyfour.enums.OrderStatus;

/**
 * Represents a takeaway order where customers pick up their food.
 * Extends the base Order class with takeaway-specific processing.
 */
public class TakeAwayOrder extends Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new TakeAwayOrder with the specified details.
     *
     * @param customerId the ID of the customer placing the order
     * @param items the list of items being ordered
     */

    public TakeAwayOrder(int customerId, List<Item> items) {
        super(customerId, items);
    }

    /**
     * Processes the takeaway order by marking it as COMPLETED.
     */
    @Override
    public void processOrder() {
        System.out.println("Processing Takeaway Order. Get Ready for Pickup!");
        this.updateStatus(OrderStatus.COMPLETED);
    }
}
