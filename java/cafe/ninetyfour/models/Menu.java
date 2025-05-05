package cafe.ninetyfour.models;

import java.io.*;
import cafe.ninetyfour.enums.ItemCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the cafe's menu with items and daily specials.
 * Supports persistence by saving to and loading from a file.
 */
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Item> items;
    private Item dailySpecial;
    private final Map<Integer, Item> itemsById = new HashMap<>();
    private final Map<String, Item> itemsByName = new HashMap<>();

    /**
     * Constructs an empty Menu.
     */
    public Menu() {
        items = new ArrayList<Item>();
        dailySpecial = null;
    }


    /**
     * Saves the menu to a file for persistence.
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("data/menu.dat"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Error saving menu: " + e.getMessage());
        }
    }

    /**
     * Loads a menu from file or creates a new one if file doesn't exist.
     * @return the loaded Menu or a new Menu if loading fails
     */
    public static Menu loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("data/menu.dat"))) {
            return (Menu) ois.readObject();
        } catch (Exception e) {
            System.out.println("No saved menu found, creating new");
            return new Menu();
        }
    }

    // Daily Special Related Methods

    /**
     * Gets the current daily special item.
     * @return the daily special item, or null if none set
     */
    public Item getDailySpecial() {
        return dailySpecial;
    }


    /**
     * Sets a new daily special item.
     * @param newDailySpecial the item to set as daily special
     */
    public void setDailySpecial(Item newDailySpecial) {
        if (dailySpecial != null) {
            dailySpecial.setAsDailySpecial(false);
        }
        newDailySpecial.setAsDailySpecial(true);
        this.dailySpecial = newDailySpecial;
        System.out.println
                ("Daily special set to: " + newDailySpecial.getName());
    }

    /**
     * Notifies customers about the current daily special.
     */
    public void notifyCustomersOfDailySpecial() {
        if (dailySpecial != null) {
            System.out.println
                    ("Today's Daily Special: " + dailySpecial.getName());
        }
    }

//    Items management

    /**
     * Gets all items in the menu.
     * @return a list of all menu items
     */
    public ArrayList<Item> getItems() {
        return (ArrayList<Item>) items;
    }


    /**
     * Adds a new item to the menu.
     * @param item the item to add
     * @return true if the item was added, false if it already existed
     */
    public boolean addItem(Item item) {
        if (!itemsById.containsKey(item.getItemID())) {
            items.add(item);
            itemsById.put(item.getItemID(), item);
            itemsByName.put(item.getName().toLowerCase(), item);
            System.out.println
                    ("Item '" + item.getName() + "' added to menu.");
            return true;
        }
        System.out.println
                ("Item with ID " + item.getItemID() + " exists in menu.");
        return false;
    }

    /**
     * Removes an item from the menu.
     * @param item the item to remove
     * @return true if the item was removed, false if not found
     */
    public boolean removeItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
            System.out.println
                    ("Item '" + item.getName() + "' removed from menu.");
            return true;
        }
        System.out.println("Item not found in menu.");
        return false;
    }


    /**
     * Gets an item by its ID.
     * @param itemID the ID of the item to find
     * @return the Item object, or null if not found
     */
    public Item getItemByID(int itemID) {
        return itemsById.get(itemID);
    }

    /**
     * Gets an item by its name .
     * @param name the name of the item to find
     * @return the Item object, or null if not found
     */
    public Item getItemByName(String name) {
        return itemsByName.get
                (name.toLowerCase());
    }


    /**
     * Gets all items in a specific category.
     * @param category the category to filter by
     * @return a list of items in the specified category
     */
    public List<Item> getItemsByCategory(ItemCategory category) {
        return items.stream()
                .filter(item -> item.getCategory() == category)
                .collect(Collectors.toList());
    }



//    other methods (for customer)
    /**
     * Displays the full menu to the console, organized by category.
     */

    public void browseMenu() {
        System.out.println("\n-------------------------------------------");
        System.out.println("Welcome to the Menu!");
        notifyCustomersOfDailySpecial();

        for (ItemCategory category : ItemCategory.values()) {
            System.out.println("\n" + category + ":");
            getItemsByCategory(category).forEach(item ->
                    System.out.println
                            (item.getName() + " - £" + item.getPrice())
            );
        }
        System.out.println("\nBon Appétit!");
    }}


