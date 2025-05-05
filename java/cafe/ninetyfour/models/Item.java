package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.ItemCategory;

import java.io.Serializable;

/**
 * Represents an item in the Menu, also used for Orders.
 * Each item has a unique ID, name, category,
 * price, and a flag for daily specials.
 */
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private final int itemID;
    private static int nextItemID = 1;
    private ItemCategory category;
    private double price;
    private boolean isDailySpecial;

    /**
     * Constructor for creating an Item object.
     *
     * @param name     the name of the item
     * @param category the category of the item
     * @param price    the price of the item
     */
    // When auto incrementing itemID,
    // no need to include it as a parameter
    public Item(String name, ItemCategory category, double price) {
        this.name = name;
        this.itemID = nextItemID++;
        this.price = price;
        this.category = category;
        this.isDailySpecial = false;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getItemID() {
        return itemID;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isDailySpecial() {
        return isDailySpecial;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        if (price > 0) {
            this.price = price;
        } else {
            System.out.println("Price must be positive.");
        }
    }

// We don't set itemID, it is auto incremented

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    // Used by Chef to set item as daily special
    /**
     * Sets the item as a daily special.
     *
     * @param isDailySpecial true if the item is a daily special, false otherwise
     */
    public void setAsDailySpecial(boolean isDailySpecial) {
        this.isDailySpecial = isDailySpecial;
    }

    /**
     * Returns a string representation of the item.
     *
     * @return a string containing the item ID, name, category, and price
     */
    public String toString() {
        return String.format("Item ID: %d | Name: %s | Category: %s | Price: £%.2f",
                itemID, name, category, price);
    }

}
