package cafe94.models;
import cafe94.enums.ItemCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Menu {
    private List<Item> items;
    private Item dailySpecial;
    private final Map<Integer, Item> itemsById = new HashMap<>();
    private final Map<String, Item> itemsByName = new HashMap<>();

    // constructor
    public Menu() {
        items = new ArrayList<Item>();
        dailySpecial = null;
    }

//    Daily Special
    public Item getDailySpecial() {
        return dailySpecial;
    }

    public void setDailySpecial(Item newDailySpecial) {
        if (dailySpecial != null) {
            dailySpecial.setAsDailySpecial(false);
        }
        newDailySpecial.setAsDailySpecial(true);
        this.dailySpecial = newDailySpecial;
        System.out.println("Daily special set to: " + newDailySpecial.getName());
    }

    public void notifyCustomersOfDailySpecial() {
        if (dailySpecial != null) {
            System.out.println("Today's Daily Special: " + dailySpecial.getName());
        }
    }

//    Items management
    public ArrayList<Item> getItems() {
        return (ArrayList<Item>) items;
    }

    public boolean addItem(Item item) {
        if (!itemsById.containsKey(item.getItemID())) {
            items.add(item);
            itemsById.put(item.getItemID(), item);
            itemsByName.put(item.getName().toLowerCase(), item);
            System.out.println("Item '" + item.getName() + "' added to menu.");
            return true;
        }
        System.out.println("Item with ID " + item.getItemID() + " already exists in menu.");
        return false;
    }

    public boolean removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            System.out.println("Item '" + item.getName() + "' removed from menu.");
            return true;
        }
        System.out.println("Item not found in menu.");
        return false;
    }

    public Item getItemByID(int itemID) {
        return itemsById.get(itemID);
    }

    public Item getItemByName(String name) {
        return itemsByName.get(name.toLowerCase());
    }

    public List<Item> getItemsByCategory(ItemCategory category) {
        return items.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());
    }



//    other methods (for customer)

    public void browseMenu() {
        System.out.println("\n----------------------------------------------");
        System.out.println("Welcome to the Menu!");
        notifyCustomersOfDailySpecial();

        for (ItemCategory category : ItemCategory.values()) {
            System.out.println("\n" + category + ":");
            getItemsByCategory(category).forEach(item ->
                    System.out.println(item.getName() + " - £" + item.getPrice())
            );
        }
        System.out.println("\nBon Appétit!");
    }}


