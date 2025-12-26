/**
 * Interface for items that can be collected.
 */
public interface Collectible {
    /**
     * Collect the item and add it to the player's inventory.
     */
    void collect(Player p);
}

