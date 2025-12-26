import java.util.ArrayList;
import java.util.Stack;

/**
 * Player class represents the player.
 */
public class Player {
    private Stack<Room> moveHistory; // Stack for backtracking
    private ArrayList<Item> inventory;
    private Room currentRoom;
    
    public Player(Room startingRoom) {
        this.moveHistory = new Stack<>();
        this.inventory = new ArrayList<>();
        this.currentRoom = startingRoom;
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    
    /**
     * Move to a new room, pushing the previous room onto the stack.
     */
    public void moveTo(Room newRoom) {
        if (currentRoom != null) {
            moveHistory.push(currentRoom);
        }
        currentRoom = newRoom;
    }
    
    /**
     * Go back to the previous room by popping from the stack.
     */
    public boolean goBack() {
        if (!moveHistory.isEmpty()) {
            currentRoom = moveHistory.pop();
            return true;
        }
        return false;
    }
    
    /**
     * Pick up an item from the current room (including subrooms).
     */
    public boolean pickupItem(String itemName) {
        Item item = currentRoom.findItem(itemName);
        if (item != null) {
            item.collect(this);
            currentRoom.removeItem(item);
            return true;
        }
        return false;
    }
    
    /**
     * Add an item to the inventory.
     */
    public void addToInventory(Item item) {
        inventory.add(item);
    }
    
    /**
     * Check whether the player has a key with a specific name.
     */
    public boolean hasKey(String keyName) {
        for (Item item : inventory) {
            if (item.getItemType() == Item.ItemType.KEY && 
                item.getName().equalsIgnoreCase(keyName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a key (first matching) from the inventory by name.
     * @return true if removed
     */
    public boolean removeKey(String keyName) {
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.getItemType() == Item.ItemType.KEY && item.getName().equalsIgnoreCase(keyName)) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sort the inventory by value using insertion sort.
     */
    public void sortInventory() {
        // Insertion sort
        for (int i = 1; i < inventory.size(); i++) {
            Item key = inventory.get(i);
            int j = i - 1;
            while (j >= 0 && inventory.get(j).compareTo(key) > 0) {
                inventory.set(j + 1, inventory.get(j));
                j--;
            }
            inventory.set(j + 1, key);
        }
    }

    /**
     * Sort inventory alphabetically by name.
     */
    public void sortInventoryAlphabetical() {
        // Selection sort by name (case-insensitive) - implemented but not used by default
        for (int i = 0; i < inventory.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < inventory.size(); j++) {
                String nameJ = inventory.get(j).getName().toLowerCase();
                String nameMin = inventory.get(minIndex).getName().toLowerCase();
                if (nameJ.compareTo(nameMin) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Item tmp = inventory.get(i);
                inventory.set(i, inventory.get(minIndex));
                inventory.set(minIndex, tmp);
            }
        }
    }
    
    /**
     * Display the inventory.
     */
    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty!");
            return;
        }
        
        System.out.println("\n=== INVENTORY ===");
        // Inventory may be pre-sorted by caller (value or alphabetical)
        for (Item item : inventory) {
            System.out.println("- " + item);
        }
    }
}

