public class Item {
    private String name;
    //    ID final once set
    private final int itemID;
    private static int nextItemID = 1;
    private ItemCategory category;
    private boolean isDailySpecial;

    // When auto incrementing itemID, no need to include it as a parameter
    public Item(String name, ItemCategory category) {
        this.name = name;
        this.itemID = nextItemID++;
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

    public boolean isDailySpecial() {
        return isDailySpecial;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

// We don't set itemID, it is auto incremented

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    // Used by Chef to set item as daily special
    public void setAsDailySpecial( boolean isDailySpecial) {
        this.isDailySpecial = true;
    }

    public String toString() {
        return "Item ID: " + itemID + "\nName: " + name + "\nCategory: " + category;
    }


}
