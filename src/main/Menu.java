// File: src/main/java/com/cafe94/domain/Menu.java
package com.cafe94.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents the restaurant's menu, holding a collection of items.
 * This class primarily acts as a container; management logic is in
 * MenuService.
 * Assumes Item class is Serializable if this class is used with
 * serialization.
 */
// Added implements Serializable
public class Menu implements Serializable {

    // Added serialVersionUID
    private static final long serialVersionUID = 1L;

    // Using a Map for potentially faster lookups by ID if needed
    // Made final to ensure the map reference doesn't change
    private final Map<Integer, Item> items;

    /**
     * Default constructor initializes an empty menu using a thread-safe map.
     */
    public Menu() {
        this.items = new ConcurrentHashMap<>(); // Thread-safe map
    }

    /**
     * Constructor to initialize the menu with a list of items.
     * @param initialItems The initial list of items (can be null).
     */
    public Menu(List<Item> initialItems) {
        this(); // Call default constructor first
        if (initialItems != null) {
            for (Item item : initialItems) {
                // Ensure item and its ID are valid before adding
                if (item != null && item.getItemID() > 0) {
                    this.items.put(item.getItemID(), item);
                }
            }
        }
    }

    /**
     * Adds or updates an item in the menu map.
     * Ensures item is not null and has a positive ID.
     * @param item The item to add or update (non-null, with positive ID).
     */
    public void addOrUpdateItem(Item item) {
        // Require item to be non-null and have a positive ID
        if (item != null && item.getItemID() > 0) { 
            this.items.put(item.getItemID(), item);
        } else {
            // Optionally log a warning or throw an exception for invalid items
            System.err.println(
                "Attempted to add invalid item (null or ID <= 0) to menu: "
                + item);
        }
    }

    /**
     * Removes an item from the menu map by its ID.
     * @param itemId The ID of the item to remove.
     * @return The removed item, or null if not found.
     */
    public Item removeItem(int itemId) {
        // ID check for removal consistency
        if (itemId <= 0) {
            return null;
        }
        return this.items.remove(itemId);
    }

    /**
     * Gets an item by its ID.
     * @param itemId The ID of the item (must be > 0).
     * @return The Item, or null if not found or ID is invalid.
     */
    public Item getItemById(int itemId) {
         if (itemId <= 0) {
            return null;
        }
        return this.items.get(itemId);
    }

    /**
     * Gets an unmodifiable list of all items currently on the menu.
     * @return An unmodifiable list of all items.
     */
    public List<Item> getAllItems() {
        // Return unmodifiable list to prevent external modification
        return Collections.unmodifiableList(
            new ArrayList<>(this.items.values()));
    }

    /**
     * Gets an unmodifiable list of all items marked as daily specials.
     * @return An unmodifiable list of daily special items.
     */
    public List<Item> getDailySpecials() {
        return this.items.values().stream()
                // Ensure items are not null
                .filter(Objects::nonNull)
                // Assumes Item.isDailySpecial() exists
                .filter(Item::isDailySpecial)
                // Use toUnmodifiableList (Java 10+)
                // .collect(Collectors.toUnmodifiableList());
                // Or for Java 8:
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                Collections::unmodifiableList));
    }

    /**
     * Gets an unmodifiable list of all unique category names
     * present in the menu.
     * @return An unmodifiable, sorted list of unique category names.
     */
    public List<String> getAllCategories() {
        return this.items.values().stream()
                .filter(Objects::nonNull)
                // Assumes Item.getCategory() exists
                .map(Item::getCategory)
                // Ensure category name is not null
                .filter(Objects::nonNull)
                // Ensure category name is not blank
                .filter(category -> !category.isBlank())
                .distinct()
                // Sort case-insensitively
                .sorted(String.CASE_INSENSITIVE_ORDER)
                // Use toUnmodifiableList (Java 10+)
                // .collect(Collectors.toUnmodifiableList());
                // Or for Java 8:
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                Collections::unmodifiableList));
    }

     /**
     * Gets an unmodifiable list of all items belonging to
     * a specific category (case-insensitive).
     * @param category The category name (case-insensitive).
     * @return An unmodifiable list of items in that category.
     */
    public List<Item> getItemsByCategory(String category) {
        if (category == null || category.isBlank()) {
            // Return immutable empty list for invalid input
            return Collections.emptyList();
        }
        return this.items.values().stream()
                .filter(Objects::nonNull)
                // Use equalsIgnoreCase for case-insensitive matching
                .filter(item -> category.equalsIgnoreCase(item.getCategory()))
                 // Use toUnmodifiableList (Java 10+)
                // .collect(Collectors.toUnmodifiableList());
                // Or for Java 8:
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                Collections::unmodifiableList));
    }
    
    // equals() and hashCode() are typically not needed for a container
    // like Menu,
    // unless you need to compare two different Menu instances
    // for exact content equality.
    // The default object identity is usually sufficient.
}