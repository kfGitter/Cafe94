import java.util.ArrayList;

public class Menu {
    private ArrayList<Item> items;
    private Item dailySpecial;

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
            newDailySpecial.setAsDailySpecial(true);
            this.dailySpecial = newDailySpecial;
        }
    }

//    Items management
    public ArrayList<Item> getItems() {
        return items;
    }

    public Item getItemByID(int itemID) {
        for (Item item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public boolean addItem(Item item) {
        if (!items.contains(item)) {
            items.add(item);
            System.out.println("Item '" + item.getName() + "' added to menu.");
            return true;
        }
        System.out.println("Item already in menu.");
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


//    other methods (for customer)
    public void notifyCustomersOfDailySpecial() {
        if (dailySpecial != null) {
            System.out.println("Today's Daily Special: " + dailySpecial.getName());
        }
    }

    public void browseMenu() {

        System.out.println("\n----------------------------------------------");
        System.out.println("Welcome to the Menu!");
        notifyCustomersOfDailySpecial();

        System.out.println("\nMain Courses:");
        for (Item item : items) {
            if (item.getCategory() == ItemCategory.MAIN) {
                System.out.println(item.getName());
            }
        }

        System.out.println("\nSides:");
        for (Item item : items) {
            if (item.getCategory() == ItemCategory.SIDE) {
                System.out.println(item.getName());
            }
        }

        System.out.println("\nDrinks:");
        for (Item item : items) {
            if (item.getCategory() == ItemCategory.DRINK) {
                System.out.println(item.getName());
            }
        }

        System.out.println("\nDesserts:");
        for (Item item : items) {
            if (item.getCategory() == ItemCategory.DESSERT) {
                System.out.println(item.getName());
            }
        }

        System.out.println("\nBon Appétit!");
    }


    public static void main(String[] args) {
        Menu menu = new Menu();
        Item item1 = new Item("Spaghetti", ItemCategory.MAIN);
        Item item2 = new Item("Garlic Bread", ItemCategory.SIDE);
        Item item3 = new Item("Tiramisu", ItemCategory.DESSERT);
        Item item4 = new Item("Coke", ItemCategory.DRINK);
        Item item5 = new Item("Fettuccine Alfredo", ItemCategory.MAIN);
        Item item6 = new Item("Cheesecake", ItemCategory.DESSERT);
        Item item7 = new Item("Sprite", ItemCategory.DRINK);
        Item item8 = new Item("Caesar Salad", ItemCategory.SIDE);
        Item item9 = new Item("Lasagna", ItemCategory.MAIN);
        Item item10 = new Item("Ice Cream", ItemCategory.DESSERT);
        Item item11 = new Item("Pepsi", ItemCategory.DRINK);
        Item item12 = new Item("Breadsticks", ItemCategory.SIDE);
        Item item13 = new Item("Ravioli", ItemCategory.MAIN);

        menu.addItem(item1);
        menu.addItem(item2);
        menu.addItem(item3);
        menu.addItem(item4);
        menu.addItem(item5);
        menu.addItem(item6);
        menu.addItem(item7);
        menu.addItem(item8);
        menu.addItem(item9);
        menu.addItem(item10);
        menu.addItem(item11);
        menu.addItem(item12);
        menu.addItem(item13);

        menu.getItemByID(8);
        menu.getItemByName("Ice Cream");
        menu.removeItem(item12);

        menu.setDailySpecial(item3);
        menu.setDailySpecial(item7);
        menu.browseMenu();
    }
}
