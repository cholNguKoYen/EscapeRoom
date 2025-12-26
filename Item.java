import java.util.ArrayList;

/**
 * Item class represents items in the game.
 * Extends GameComponent, implements Collectible and Comparable<Item>.
 */
public class Item extends GameComponent implements Collectible, Comparable<Item> {
    public enum ItemType {
        KEY, TOOL, CLUE
    }
    
    private int value;
    private ItemType itemType;
    
    public Item(String name, int value, ItemType itemType) {
        super(name);
        this.value = value;
        this.itemType = itemType;
    }
    
    public int getValue() {
        return value;
    }
    
    public ItemType getItemType() {
        return itemType;
    }
    
    @Override
    public void inspect() {
        System.out.println("Item: " + name + " (Type: " + itemType + ", Value: " + value + ")");
    }
    
    @Override
    public void collect(Player p) {
        p.addToInventory(this);
        System.out.println("Collected: " + name);
    }
    
    /**
     * Compare by value; if values are equal then compare by name alphabetically.
     */
    @Override
    public int compareTo(Item other) {
        if (this.value != other.value) {
            return Integer.compare(this.value, other.value);
        }
        return this.name.compareTo(other.name);
    }
    
    @Override
    public String toString() {
        return name + " (" + itemType + ", value: " + value + ")";
    }
}

