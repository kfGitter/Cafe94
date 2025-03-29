// File: src/main/java/com/cafe94/domain/Item.java
package com.cafe94.domain;

import com.cafe94.util.ValidationUtils;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single item available on the Cafe94 menu.
 * Includes details like name, description, price, category,
 * allergens, and special status.
 * This class is designed to be immutable after creation,
 * except for the itemID set by persistence.
 */
// Added implements Serializable
public class Item implements Serializable {

    // Added serialVersionUID
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the menu item. Settable once by persistence. */
    private int itemID;

    /** Name of the menu item (e.g., "Cheeseburger", "Cappuccino").
     * Mandatory, immutable. */
    private final String name; // Made final

    /** Description of the menu item. Optional, immutable. */
    private final String description; // Made final

    /** Price of the menu item. Must be non-negative, immutable. */
    private final double price; // Made final

    /** Category the item belongs to (e.g., "Starters", "Main Courses").
     * Mandatory, immutable. */
    private final String category; // Made final

    /** Flag indicating if this item is currently a daily special.
     * Immutable after construction. */
    private final boolean isDailySpecial; // Made final

    /** List of allergen identifiers associated with the item.
     * Immutable list. */
    private final List<String> allergens; // Made final

    /**
     * Constructs a new immutable menu Item.
     *
     * @param itemID        The unique ID (0 for new entities) or existing ID.
     * @param name          The item name (non-null, non-blank).
     * @param description   The item description (can be null).
     * @param price         The item price (must be >= 0).
     * @param category      The item category (non-null, non-blank).
     * @param isDailySpecial True if it's a daily special, false otherwise.
     * @param allergens     List of allergen identifiers (nullable, stored as immutable list).
     * @throws NullPointerException if name or category is null.
     * @throws IllegalArgumentException if name/category is blank, price is negative.
     */
    // Added allergens parameter
    public Item(int itemID, String name, String description, double price,
    String category, boolean isDailySpecial, List<String> allergens) {
        
        this.itemID = itemID; // Initial ID

        // Validate and set final fields directly using ValidationUtils
        this.name = ValidationUtils.requireNonBlank(name, "Item name");
        this.description = description; // Allow null description
        this.price = ValidationUtils.requireNonNegative(price, "Item price");
        this.category = ValidationUtils.requireNonBlank(category,
        "Item category");
        this.isDailySpecial = isDailySpecial;
        
        // Store immutable copy of allergens, handling null input
        // Assumes ValidationUtils.nullSafeList exists
        // Use List.copyOf for Java 10+ for a truly immutable copy
        //this.allergens = List.copyOf(ValidationUtils.nullSafeList(allergens));
        // For Java 8 compatibility:
        this.allergens =
        Collections.unmodifiableList(new ArrayList<>(ValidationUtils.nullSafeList(allergens)));
    }

    // --- Getters ---
    // (No changes needed for getters, they access the final fields)

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDailySpecial() {
        return isDailySpecial;
    }

    /** @return An unmodifiable list of allergens. */
    public List<String> getAllergens() {
        // Returns the immutable list created in the constructor
        return allergens;
    }

    // --- Setters (Removed most setters as fields are now final) ---

     /**
     * Sets the item ID. Should typically only be called once
     * by the persistence layer after saving a new item.
     * @param itemID The assigned persistent ID (must be > 0).
     * @throws IllegalArgumentException if itemID is not positive.
     * @throws IllegalStateException if trying to reset an already assigned non-zero ID.
     */
    // Changed visibility to protected and added validation
    protected void setItemID(int itemID) {
        if (itemID <= 0) {
             throw new IllegalArgumentException(
                "Item ID assigned by persistence must be positive.");
        }
        if (this.itemID != 0 && this.itemID != itemID) {
            throw new IllegalStateException(
                "Cannot change an already assigned persistent item ID.");
        }
        this.itemID = itemID;
    }
    
    // Removed private setters: setName, setDescription, setPrice,
    // setCategory, setDailySpecial, setAllergens

    // Removed duplicate private helper: requireNonBlank

    // --- Standard Methods ---

    @Override
    public String toString() {
        // Shortened format slightly
        return "Item[" +
               "ID=" + itemID +
               ", Name='" + name + '\'' +
               ", Cat='" + category + '\'' +
               ", Price=" + String.format("%.2f", price) +
               ", Allergens=" + allergens.size() +
               ", Special=" + isDailySpecial +
               (description !=
               null && !description.isEmpty() ? ", Desc=Yes" : "") +']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use getClass() check for stricter type matching
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        // If itemID is assigned and non-zero for both, use it for identity
        if (itemID != 0 && item.itemID != 0) {
            return itemID == item.itemID;
        }
        // Fallback for transient items (compare key immutable fields)
        // Use name and category as potential business keys for
        // transient equality
        return Double.compare(item.price, price) == 0 && 
               Objects.equals(name, item.name) && 
               Objects.equals(category, item.category);
               // Removed other fields from transient check as ID, name,
               // category, price are primary identifiers
               // isDailySpecial == item.isDailySpecial &&
               // Objects.equals(description, item.description) &&
               // Objects.equals(allergens, item.allergens);
    }

    @Override
    public int hashCode() {
        // Use itemID if assigned and non-zero
        if (itemID != 0) {
            return Objects.hash(itemID);
        }
        // Fallback hashcode consistent with equals fallback
        return Objects.hash(name, category, price);
    }
}