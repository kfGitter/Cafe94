package cafe94.models;

import cafe94.enums.ItemCategory;


public class Item {
    private String name;
    //    ID final once set
    private final int itemID;
    private static int nextItemID = 1;
    private ItemCategory category;
    private double price;
    private boolean isDailySpecial;

    // When auto incrementing itemID, no need to include it as a parameter
    public Item(String name, ItemCategory category, double price) {
        this.name = name;
        this.itemID = nextItemID++;
        this.price = price;
        this.category = category;
        this.isDailySpecial = false;
    }

    // getters
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

    // setters
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
    public void setAsDailySpecial(boolean isDailySpecial) {
        this.isDailySpecial = isDailySpecial; // Use parameter instead of hardcoding true
    }
    public String toString() {
        return String.format("Item ID: %d | Name: %s | Category: %s | Price: £%.2f",
                itemID, name, category, price);
    }

}
