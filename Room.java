import java.util.ArrayList;

/**
 * Room class represents a room in the escape room.
 * It has a recursive structure: a room can contain subrooms.
 */
public class Room extends GameComponent {
    private ArrayList<GameComponent> contents; // items, puzzles, subrooms
    private ArrayList<Room> connectedRooms;
    private boolean isExit;
    private String requiredKey; // null if no key is required
    
    public Room(String name) {
        super(name);
        this.contents = new ArrayList<>();
        this.connectedRooms = new ArrayList<>();
        this.isExit = false;
        this.requiredKey = null;
    }
    
    public Room(String name, String requiredKey) {
        super(name);
        this.contents = new ArrayList<>();
        this.connectedRooms = new ArrayList<>();
        this.isExit = false;
        this.requiredKey = requiredKey;
    }
    
    public void setExit(boolean isExit) {
        this.isExit = isExit;
    }
    
    public boolean isExit() {
        return isExit;
    }
    
    public String getRequiredKey() {
        return requiredKey;
    }

    /**
     * Set or clear the required key for this room.
     */
    public void setRequiredKey(String requiredKey) {
        this.requiredKey = requiredKey;
    }
    
    public void addContent(GameComponent component) {
        contents.add(component);
    }
    
    public void addConnectedRoom(Room room) {
        connectedRooms.add(room);
    }
    
    public ArrayList<GameComponent> getContents() {
        return contents;
    }
    
    public ArrayList<Room> getConnectedRooms() {
        return connectedRooms;
    }
    
    @Override
    public void inspect() {
        System.out.println("=== Room: " + name + " ===");
        if (isExit) {
            System.out.println(">>> THIS IS THE EXIT! <<<");
        }
        if (requiredKey != null) {
            System.out.println("(Requires key: " + requiredKey + " to enter)");
        }
        System.out.println("\nRoom contents:");
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                System.out.println("  [Subroom] " + component.getName());
            } else {
                component.inspect();
            }
        }
        System.out.println("\nConnected rooms:");
        for (Room room : connectedRooms) {
            System.out.println("- " + room.getName());
        }
    }
    
    /**
     * Recursive method để explore (khám phá) tất cả các phòng
     * @param depth Độ sâu hiện tại trong cây recursive
     */
    public void exploreRecursive(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "└─ " + name + (isExit ? " [EXIT]" : ""));

        // Explore subrooms recursively (rooms contained within this room)
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                ((Room) component).exploreRecursive(depth + 1);
            }
        }

        // List connected rooms (same-level connections) but do not recurse into them
        if (!connectedRooms.isEmpty()) {
            System.out.println(indent + "   Connected:");
            for (Room room : connectedRooms) {
                System.out.println(indent + "     - " + room.getName());
            }
        }
    }
    
    /**
     * Recursive method để tìm item trong phòng và các phòng con
     * @param itemName Tên item cần tìm
     * @return true nếu tìm thấy, false nếu không
     */
    public boolean containsItemRecursive(String itemName) {
        // Tìm trong contents của phòng hiện tại
        for (GameComponent component : contents) {
            if (component instanceof Item && component.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
            // Nếu là Room con, tìm recursive
            if (component instanceof Room) {
                if (((Room) component).containsItemRecursive(itemName)) {
                    return true;
                }
            }
        }
        
        // Tìm trong connected rooms
        for (Room room : connectedRooms) {
            if (room.containsItemRecursive(itemName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Recursive method để tính độ sâu tối đa của cây phòng
     * @return Độ sâu tối đa
     */
    public int maxDepthRecursive() {
        int maxDepth = 0;
        
        // Tìm độ sâu tối đa trong subrooms
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                int depth = ((Room) component).maxDepthRecursive();
                maxDepth = Math.max(maxDepth, depth);
            }
        }
        
        // Tìm độ sâu tối đa trong connected rooms
        for (Room room : connectedRooms) {
            int depth = room.maxDepthRecursive();
            maxDepth = Math.max(maxDepth, depth);
        }
        
        return maxDepth + 1;
    }
    
    /**
     * Find item in the current room (searches in subrooms too).
     */
    public Item findItem(String itemName) {
        // First check current room contents
        for (GameComponent component : contents) {
            if (component instanceof Item && component.getName().equalsIgnoreCase(itemName)) {
                return (Item) component;
            }
        }
        // If not found, check subrooms recursively
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                Item found = ((Room) component).findItem(itemName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    /**
     * Remove an item from the current room or its subrooms.
     * @param item The item to remove
     * @return true if the item was found and removed, false otherwise
     */
    public boolean removeItem(Item item) {
        // First check current room contents
        if (contents.remove(item)) {
            return true;
        }
        // If not found, check subrooms recursively
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                if (((Room) component).removeItem(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if this room (including subrooms) contains any Item.
     */
    public boolean hasAnyItemRecursive() {
        for (GameComponent component : contents) {
            if (component instanceof Item) {
                return true;
            }
            if (component instanceof Room) {
                if (((Room) component).hasAnyItemRecursive()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Find puzzle in the current room (searches in subrooms too).
     */
    public Puzzle findPuzzle(String puzzleName) {
        // First check current room contents
        for (GameComponent component : contents) {
            if (component instanceof Puzzle && component.getName().equalsIgnoreCase(puzzleName)) {
                return (Puzzle) component;
            }
        }
        // If not found, check subrooms recursively
        for (GameComponent component : contents) {
            if (component instanceof Room) {
                Puzzle found = ((Room) component).findPuzzle(puzzleName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}

