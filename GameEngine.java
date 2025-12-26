import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * GameEngine controls the game loop and processes commands.
 */
public class GameEngine {
    private ArrayList<Room> map;
    private Queue<String> hintQueue;
    private Player player;
    private int turnCounter;
    private boolean gameRunning;
    private Scanner scanner;
    private ArrayList<String> requiredItems; // Items needed to win
    private boolean puzzle5Bsolved;

    public GameEngine() {
        this.map = new ArrayList<>();
        this.hintQueue = new LinkedList<>();
        this.turnCounter = 0;
        this.gameRunning = false;
        this.scanner = new Scanner(System.in);
        this.requiredItems = new ArrayList<>();
        this.puzzle5Bsolved = false;
    }

    /**
     * Compare room names with a tolerant match: exact ignore-case, or normalized
     * (remove spaces) match.
     */
    private boolean matchesRoomName(String roomName, String inputName) {
        if (roomName.equalsIgnoreCase(inputName))
            return true;
        String a = roomName.replaceAll("\\s+", "").toLowerCase();
        String b = inputName.replaceAll("\\s+", "").toLowerCase();
        return a.equals(b);
    }

    /**
     * Print the full map showing top-level rooms as siblings and their internal
     * subrooms.
     */
    private void printFullMap() {
        System.out.println("\n=== FULL MAP ===");
        if (map.isEmpty())
            return;

        Room entrance = findRoom("Entrance");
        if (entrance == null) {
            // fallback: print previous top-level roots
            Set<Room> subrooms = new HashSet<>();
            for (Room r : map) {
                for (GameComponent comp : r.getContents()) {
                    if (comp instanceof Room)
                        subrooms.add((Room) comp);
                }
            }
            for (Room r : map) {
                if (!subrooms.contains(r))
                    r.exploreRecursive(0);
            }
            return;
        }

        Set<Room> visited = new HashSet<>();
        printRoomDFS(entrance, 0, visited);
    }

    private void printRoomDFS(Room r, int depth, Set<Room> visited) {
        if (r == null || visited.contains(r))
            return;
        visited.add(r);

        String indent = "  ".repeat(depth);
        System.out.println(indent + "└─ " + r.getName() + (r.isExit() ? " [EXIT]" : ""));

        // Print non-room contents (items, puzzles)
        for (GameComponent comp : r.getContents()) {
            if (comp instanceof Room)
                continue;
            System.out.println(indent + "   - " + comp.getName());
        }

        // First recurse into subrooms (contained rooms)
        for (GameComponent comp : r.getContents()) {
            if (comp instanceof Room) {
                printRoomDFS((Room) comp, depth + 1, visited);
            }
        }

        // Then recurse into connected rooms (same-level neighbors)
        for (Room conn : r.getConnectedRooms()) {
            if (!visited.contains(conn)) {
                printRoomDFS(conn, depth + 1, visited);
            }
        }
    }

    // Initialize the game map and the player.

    public void initializeGame() {
        // Create main rooms (same level)
        Room entrance = new Room("Entrance");
        Room hallway = new Room("Hallway");
        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3", "key_room3");
        Room room4 = new Room("Room 4", "key_room4");
        Room room5 = new Room("Room 5", "key_room5");
        Room exit = new Room("Exit Room", "Exit Key");
        exit.setExit(true);

        // Subrooms
        Room r1a = new Room("1A");
        Room r1b = new Room("1B");
        Room r3a = new Room("3A");
        Room r2a = new Room("2A");
        Room r4a = new Room("4A");
        Room r4b = new Room("4B");
        Room r5a = new Room("5A");
        Room r5b = new Room("5B");

        // Items
        Item any1 = new Item("Old Book", 5, Item.ItemType.CLUE);
        Item any2 = new Item("Flashlight", 6, Item.ItemType.TOOL);
        Item any3 = new Item("Room3 Gem", 12, Item.ItemType.CLUE);
        Item any4 = new Item("Wrench", 4, Item.ItemType.TOOL);
        Item magnifier = new Item("Magnifying Glass", 3, Item.ItemType.TOOL);
        Item r3aItem = new Item("Small Coin", 2, Item.ItemType.CLUE);
        Item r4aItem = new Item("Silver Screw", 2, Item.ItemType.TOOL);
        Item r5aItem = new Item("Silver Coin", 3, Item.ItemType.CLUE);

        // Required end-game item
        requiredItems.add("Exit Key");

        // Puzzles
        RiddlePuzzle p1A = new RiddlePuzzle("1A Riddle", 2,
                "I have cities, but no houses. I have mountains, but no trees. What am I?",
                "Map");
        RiddlePuzzle p1B = new RiddlePuzzle("1B Riddle", 3,
                "What has keys but no locks, space but no room, and you can enter but not go inside?",
                "Keyboard");
        RiddlePuzzle p3 = new RiddlePuzzle("Room3 Riddle", 3,
                "I speak without a mouth and hear without ears. I have nobody, but I come alive with wind. What am I?",
                "Echo");
        // 4B puzzle that grants Exit Key
        CodePuzzle p4b = new CodePuzzle("4B Code Lock", 4, "7777");

        // 5B puzzles (three puzzles)
        RiddlePuzzle p5_1 = new RiddlePuzzle("Puzzle 1", 2,
                "I’m tall when I’m young, and I’m short when I’m old. What am I?", "Candle");
        RiddlePuzzle p5_2 = new RiddlePuzzle("Puzzle 2", 3, "What has hands but can not clap?", "Clock");
        RiddlePuzzle p5_3 = new RiddlePuzzle("Puzzle 3", 5, "What disappears as soon as you say its name?",
                "Silence");

        // Assemble room contents per specification
        entrance.addContent(any1);
        entrance.addConnectedRoom(hallway);

        hallway.addConnectedRoom(room1);
        hallway.addConnectedRoom(room2);

        // Room 1 and its subrooms
        room1.addContent(any4);
        r1a.addContent(p1A); // subroom 1A contains puzzle
        r1a.addConnectedRoom(r1b); // 1A -> 1B
        r1b.addContent(p1B); // subroom 1B contains puzzle
        room1.addContent(r1a);
        room1.addContent(r1b);
        room1.addConnectedRoom(room3); // Room1 connected to Room3

        // Room 3
        room3.addContent(any3);
        room3.addContent(p3);
        r3a.addContent(r3aItem);
        room3.addContent(r3a);

        // Room 2 and subrooms
        room2.addContent(any2);
        room2.addContent(magnifier);
        r2a.addContent(new Item("Note", 1, Item.ItemType.CLUE));
        room2.addContent(r2a);
        // Room2 connected to Room4 and Room5
        room2.addConnectedRoom(room4);
        room2.addConnectedRoom(room5);

        // Room 4 and subrooms
        r4a.addContent(r4aItem);
        r4a.addConnectedRoom(r4b);
        r4b.addContent(p4b); // solving p4b grants Exit Key
        room4.addContent(r4a);
        room4.addConnectedRoom(room5); // Room4 <-> Room5

        // Room5 and subrooms
        r5a.addContent(r5aItem);
        r5a.addConnectedRoom(r5b);
        r5b.addContent(p5_1);
        r5b.addContent(p5_2);
        r5b.addContent(p5_3);
        r5b.addConnectedRoom(exit);
        room5.addContent(r5a);

        // Add rooms to map
        map.add(entrance);
        map.add(hallway);
        map.add(room1);
        map.add(room2);
        map.add(room3);
        map.add(room4);
        map.add(room5);
        map.add(exit);
        map.add(r1a);
        map.add(r1b);
        map.add(r3a);
        map.add(r2a);
        map.add(r4a);
        map.add(r4b);
        map.add(r5a);
        map.add(r5b);

        // Create player in Entrance
        player = new Player(entrance);

        // Hints
        hintQueue.offer("Explore subrooms to find puzzles and keys.");
        hintQueue.offer("Some rooms require specific keys to enter (they may be consumed).");
        hintQueue.offer("Solve a puzzle in 5B to reveal the Exit room.");
    }

    /**
     * Start the game loop.
     */
    public void start() {
        gameRunning = true;
        System.out.println("========================================");
        System.out.println("    WELCOME TO THE ESCAPE ROOM GAME!");
        System.out.println("========================================");
        System.out.println("You wake up in a locked facility...");
        System.out.println("Goal: Find your way to the exit!\n");
        System.out.println("Available actions (type the word and press Enter):");
        System.out.println("  - look           : inspect the current room");
        System.out.println("  - move <room>    : move to a connected room");
        System.out.println("  - back           : go back to the previous room");
        System.out.println("  - pickup <item>  : pick up an item in the room");
        System.out.println("  - inventory / i  : view your inventory");
        System.out.println("  - solve <puzzle> : attempt to solve a puzzle");
        System.out.println("  - map            : show the full map");
        System.out.println("  - help           : show this list again");
        System.out.println("  - quit / exit    : leave the game");
        System.out.println("\nTip: type 'help' any time to see this list again.\n");

        printStatus();

        while (gameRunning) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            try {
                boolean consumed = processCommand(input);
                if (consumed) {
                    turnCounter++;
                    // After a move/back that consumed a turn, print updated status
                    printStatus();
                }

                // Every 3 turns, show a hint if available
                if (turnCounter % 3 == 0 && !hintQueue.isEmpty()) {
                    String hint = hintQueue.poll();
                    System.out.println("\nHint: " + hint);
                }

                winConditionCheck();

            } catch (InvalidCommandException | LockedRoomException | InvalidPuzzleAnswerException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    /**
     * Process commands from the player.
     */
    public boolean processCommand(String cmd)
            throws InvalidCommandException, LockedRoomException, InvalidPuzzleAnswerException {
        String[] parts = cmd.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "look":
                player.getCurrentRoom().inspect();
                return false;

            case "move":
                if (argument.isEmpty()) {
                    throw new InvalidCommandException("Syntax: move <room_name>");
                }
                moveToRoom(argument);
                return true;

            case "back":
                if (player.goBack()) {
                    System.out.println("You moved back to room: " + player.getCurrentRoom().getName());
                    return true;
                } else {
                    System.out.println("Cannot move back!");
                    return false;
                }

            case "pickup":
                if (argument.isEmpty()) {
                    throw new InvalidCommandException("Syntax: pickup <item_name>");
                }
                if (player.pickupItem(argument)) {
                    System.out.println("Picked up: " + argument);
                    // After pickup, if current room (including subrooms) has no more items, notify
                    // player
                    Room curr = player.getCurrentRoom();
                    if (!curr.hasAnyItemRecursive()) {
                        System.out.println("This room is quite empty, try to look in other rooms");
                    }
                } else {
                    System.out.println("Item not found: " + argument);
                }
                return false;

            case "inventory":
            case "i":
                if (argument.equalsIgnoreCase("alpha") || argument.equalsIgnoreCase("alphabet")) {
                    player.sortInventoryAlphabetical();
                    player.showInventory();
                } else if (argument.equalsIgnoreCase("value")) {
                    player.sortInventory();
                    player.showInventory();
                } else if (argument.isEmpty()) {
                    player.showInventory();
                } else {
                    System.out.println("Usage: inventory [value|alpha]");
                }
                return false;

            case "solve":
                if (argument.isEmpty()) {
                    throw new InvalidCommandException("Syntax: solve <puzzle_name>");
                }
                solvePuzzle(argument);
                return false;

            case "map":
                printFullMap();
                return false;

            case "help":
                showHelp();
                return false;

            case "quit":
            case "exit":
                gameRunning = false;
                System.out.println("Thank you for playing!");
                return false;

            default:
                throw new InvalidCommandException("Invalid command. Type 'help' to see the command list.");
        }

        // Default: no turn consumed
        // (shouldn't reach here because cases return or throw)
        // Return false as safe default.
        // return false;
    }

    /**
     * Move to another room (connected room or subroom).
     */
    private void moveToRoom(String roomName) throws LockedRoomException {
        Room currentRoom = player.getCurrentRoom();

        // First, check connected rooms
        Room targetRoom = null;
        for (Room room : currentRoom.getConnectedRooms()) {
            if (matchesRoomName(room.getName(), roomName)) {
                targetRoom = room;
                break;
            }
        }

        // If not found in connected rooms, check subrooms (rooms in contents)
        if (targetRoom == null) {
            for (GameComponent component : currentRoom.getContents()) {
                if (component instanceof Room && matchesRoomName(component.getName(), roomName)) {
                    targetRoom = (Room) component;
                    break;
                }
            }
        }

        if (targetRoom == null) {
            System.out.println("Room not found: " + roomName);
            System.out.println("Available rooms:");
            // Show connected rooms
            for (Room room : currentRoom.getConnectedRooms()) {
                System.out.println("- " + room.getName() + " (connected)");
            }
            // Show subrooms
            for (GameComponent component : currentRoom.getContents()) {
                if (component instanceof Room) {
                    System.out.println("- " + component.getName() + " (subroom)");
                }
            }
            return;
        }

        // Check whether the room requires a key
        if (targetRoom.getRequiredKey() != null) {
            String req = targetRoom.getRequiredKey();
            if (!player.hasKey(req)) {
                throw new LockedRoomException("This room is locked! Required key: " + req);
            } else {
                // If it's the final exit room, do not consume the Exit Key (keeps it in
                // inventory)
                if (targetRoom.isExit()) {
                    System.out.println(
                            "Used " + req + " to open the door to " + targetRoom.getName() + " (not consumed).");
                    System.out.println(
                            "Room " + targetRoom.getName() + " has been unlocked using " + req + " (not consumed).");
                } else {
                    // consume the key for regular locked rooms
                    player.removeKey(req);
                    // mark room as unlocked so it won't require the key again
                    targetRoom.setRequiredKey(null);
                    System.out.println(
                            "Used " + req + " to unlock " + targetRoom.getName() + ". Key removed from inventory.");
                    System.out.println("Room " + targetRoom.getName() + " has been unlocked with the key.");
                }
            }
        }

        player.moveTo(targetRoom);
        System.out.println("Moved to: " + targetRoom.getName());

        // If this is a subroom with no connected rooms and no subrooms inside, it's a
        // dead end
        boolean hasSubroom = false;
        for (GameComponent comp : targetRoom.getContents()) {
            if (comp instanceof Room) {
                hasSubroom = true;
                break;
            }
        }
        if (targetRoom.getConnectedRooms().isEmpty() && !hasSubroom) {
            System.out.println("Dead end — try the \"back\" command to return to the previous room");
        }
    }

    /**
     * Solve a puzzle.
     */
    private void solvePuzzle(String puzzleName) throws InvalidPuzzleAnswerException {
        Room currentRoom = player.getCurrentRoom();
        Puzzle puzzle = currentRoom.findPuzzle(puzzleName);

        // If not found as a single puzzle, check if player requested to solve a room
        // that
        // contains multiple puzzles (e.g., subroom 5B). Support calling: solve
        // <subroomName>
        if (puzzle == null) {
            // Try to find a Room component with that name inside current room
            Room puzzleRoom = null;
            for (GameComponent comp : currentRoom.getContents()) {
                if (comp instanceof Room && comp.getName().equalsIgnoreCase(puzzleName)) {
                    puzzleRoom = (Room) comp;
                    break;
                }
            }
            if (puzzleRoom != null) {
                // Count puzzles inside
                ArrayList<Puzzle> puzzlesInside = new ArrayList<>();
                for (GameComponent comp : puzzleRoom.getContents()) {
                    if (comp instanceof Puzzle) {
                        puzzlesInside.add((Puzzle) comp);
                    }
                }
                if (puzzlesInside.size() > 1) {
                    solveMultiplePuzzles(puzzleRoom, puzzlesInside);
                    return;
                }
            }

            System.out.println("Puzzle not found: " + puzzleName);
            return;
        }

        if (puzzle.isSolved()) {
            System.out.println("This puzzle is already solved!");
            return;
        }

        // Show puzzle info
        puzzle.inspect();

        // Ask if player wants to solve now or continue
        System.out.println("\nDo you want to solve this puzzle now? (yes/no)");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (!choice.equals("yes") && !choice.equals("y")) {
            System.out.println("You decided to continue without solving the puzzle.");
            return;
        }

        // Player wants to solve, ask for answer
        System.out.println("\nType only the answer word or phrase.");
        System.out.println("Example: for the riddle 'I have cities, but no houses...");
        System.out.print("Your answer is: ");
        String answer = scanner.nextLine().trim();

        if (puzzle.attemptSolve(answer)) {
            System.out.println("Congratulations! You solved the puzzle!");
            puzzle.setSolved(true);

            // Puzzle giải được có thể unlock các thứ (ví dụ: thêm key, unlock room)
            handlePuzzleSolved(puzzle);
        } else {
            System.out.println("Wrong answer! Try again.");
        }
    }

    /**
     * Solve when a room contains multiple puzzles (selection + optional sort)
     */
    private void solveMultiplePuzzles(Room puzzleRoom, ArrayList<Puzzle> puzzlesInside)
            throws InvalidPuzzleAnswerException {
        System.out.println("This area contains multiple puzzles. Do you want to sort them by difficulty? (yes/no)");
        System.out.print("Your choice: ");
        String ch = scanner.nextLine().trim().toLowerCase();
        if (ch.equals("yes") || ch.equals("y")) {
            sortPuzzlesByDifficulty(puzzlesInside);
        }

        System.out.println("Available puzzles:");
        for (int i = 0; i < puzzlesInside.size(); i++) {
            Puzzle p = puzzlesInside.get(i);
            System.out.println((i + 1) + ") " + p.getName() + " (Difficulty: " + p.getDifficulty() + ")");
        }

        System.out.print("Pick a puzzle number to attempt: ");
        String sel = scanner.nextLine().trim();
        int idx = -1;
        try {
            idx = Integer.parseInt(sel) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection.");
            return;
        }
        if (idx < 0 || idx >= puzzlesInside.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Puzzle chosen = puzzlesInside.get(idx);
        if (chosen.isSolved()) {
            System.out.println("This puzzle is already solved!");
            return;
        }

        // Show puzzle info and ask to solve
        chosen.inspect();
        System.out.println("\nDo you want to solve this puzzle now? (yes/no)");
        System.out.print("Your choice: ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (!choice.equals("yes") && !choice.equals("y")) {
            System.out.println("You decided to continue without solving the puzzle.");
            return;
        }

        System.out.print("Your answer is: ");
        String answer = scanner.nextLine().trim();
        if (chosen.attemptSolve(answer)) {
            System.out.println("Congratulations! You solved the puzzle!");
            chosen.setSolved(true);
            handlePuzzleSolved(chosen);
        } else {
            System.out.println("Wrong answer! Try again.");
        }
    }

    /**
     * Handle logic when a puzzle has been solved.
     * Puzzles can unlock keys, unlock room connections, or add hints.
     */
    private void handlePuzzleSolved(Puzzle puzzle) {
        String puzzleName = puzzle.getName();

        // Room1 subroom 1A solved -> place key for Room3 inside subroom 1A
        if (puzzleName.equalsIgnoreCase("1A Riddle")) {
            Item key = new Item("key_room3", 1, Item.ItemType.KEY);
            Room where = findRoom("1A");
            if (where != null) {
                where.addContent(key);
                System.out.println("A key to Room 3 has been revealed in " + where.getName()
                        + ". Use 'pickup key_room3' to collect it.");
            } else {
                player.addToInventory(key);
                System.out.println("A key to Room 3 has been added to your inventory (fallback): " + key.getName());
            }
        }

        // Room1 subroom 1B solved -> place key for Room4 inside subroom 1B
        if (puzzleName.equalsIgnoreCase("1B Riddle")) {
            Item key = new Item("key_room4", 1, Item.ItemType.KEY);
            Room where = findRoom("1B");
            if (where != null) {
                where.addContent(key);
                System.out.println("A key to Room 4 has been revealed in " + where.getName()
                        + ". Use 'pickup key_room4' to collect it.");
            } else {
                player.addToInventory(key);
                System.out.println("A key to Room 4 has been added to your inventory (fallback): " + key.getName());
            }
        }

        // Room3 puzzle solved -> place key for Room5 inside Room 3
        if (puzzleName.equalsIgnoreCase("Room3 Riddle")) {
            Item key = new Item("key_room5", 1, Item.ItemType.KEY);
            Room where = findRoom("Room 3");
            if (where != null) {
                where.addContent(key);
                System.out.println("A key to Room 5 has been revealed in " + where.getName()
                        + ". Use 'pickup key_room5' to collect it.");
            } else {
                player.addToInventory(key);
                System.out.println("A key to Room 5 has been added to your inventory (fallback): " + key.getName());
            }
        }

        // 4B code lock -> place Exit Key inside subroom 4B
        if (puzzleName.equalsIgnoreCase("4B Code Lock")) {
            Item exitKey = new Item("Exit_Key", 50, Item.ItemType.KEY);
            Room where = findRoom("4B");
            if (where != null) {
                where.addContent(exitKey);
                System.out.println("You solved the 4B code lock. The Exit_Key has appeared in " + where.getName()
                        + ". Use 'pickup Exit_Key' to collect it.");
            } else {
                player.addToInventory(exitKey);
                System.out.println("You solved the 4B code lock and received the Exit_Key (fallback)!");
            }
        }

        // If a puzzle inside 5B was solved, reveal the Exit Room connection
        if (puzzleName.startsWith("5B Puzzle")) {
            // mark solved and reveal exit room connection from Room 5
            puzzle5Bsolved = true;
            Room room5 = findRoom("Room 5");
            Room exitRoom = findRoom("Exit Room");
            if (room5 != null && exitRoom != null) {
                room5.addConnectedRoom(exitRoom);
                hintQueue.offer(
                        "You heard a distant rumble: A new door has opened somewhere (the Exit might be visible now).");
                System.out.println("Solving this puzzle has revealed a new path. Use 'look' to inspect nearby rooms.");
            }
        }
    }

    /**
     * Find a room by name in the map.
     */
    private Room findRoom(String name) {
        for (Room r : map) {
            if (r.getName().equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Print the current game status.
     */
    public void printStatus() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("Turns: " + turnCounter);
        System.out.println("Current room: " + player.getCurrentRoom().getName());
        if (player.getCurrentRoom().isExit()) {
            System.out.println(">>> YOU HAVE FOUND THE EXIT! <<<");
        }
        // Show required items status
        if (!requiredItems.isEmpty()) {
            System.out.println("\nRequired items to escape:");
            for (String requiredItem : requiredItems) {
                boolean hasItem = false;
                for (Item item : player.getInventory()) {
                    if (item.getName().equalsIgnoreCase(requiredItem)) {
                        hasItem = true;
                        break;
                    }
                }
                System.out.println("  " + (hasItem ? "✓" : "✗") + " " + requiredItem);
            }
        }
        System.out.println("=".repeat(40));
    }

    /**
     * Check win condition.
     * Player must collect all required items AND reach the EXIT.
     */
    public void winConditionCheck() {
        if (!player.getCurrentRoom().isExit()) {
            return; // Not at exit yet
        }

        // Check if player has all required items
        ArrayList<String> missingItems = new ArrayList<>();
        for (String requiredItem : requiredItems) {
            boolean hasItem = false;
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase(requiredItem)) {
                    hasItem = true;
                    break;
                }
            }
            if (!hasItem) {
                missingItems.add(requiredItem);
            }
        }

        if (missingItems.isEmpty()) {
            // Win condition met: at exit AND has all required items
            System.out.println("\n" + "=".repeat(50));
            System.out.println("CONGRATULATIONS! YOU ESCAPED THE ROOM!");
            System.out.println("You collected all required items and reached the exit!");
            System.out.println("Number of turns: " + turnCounter);
            System.out.println("=".repeat(50));
            gameRunning = false;
        } else {
            // At exit but missing required items
            System.out.println("\n" + "=".repeat(50));
            System.out.println("You reached the EXIT, but you're missing required items:");
            for (String item : missingItems) {
                System.out.println("  - " + item);
            }
            System.out.println("You need to collect all required items to escape!");
            System.out.println("=".repeat(50));
        }
    }

    /**
     * Display help menu.
     */
    private void showHelp() {
        System.out.println("\n=== COMMAND LIST ===");
        System.out.println("look                - Inspect the current room");
        System.out.println("move <room_name>    - Move to another room");
        System.out.println("back                - Move back to the previous room");
        System.out.println("pickup <item_name>  - Pick up an item");
        System.out.println("inventory           - Show your inventory");
        System.out.println("solve <puzzle_name> - Solve a puzzle");
        System.out.println("map                 - View the full map (debug)");
        System.out.println("help                - Show this menu");
        System.out.println("quit/exit           - Exit the game");
    }

    /**
     * y * Sort puzzles by difficulty using selection sort.
     */
    public void sortPuzzlesByDifficulty(ArrayList<Puzzle> puzzles) {
        // Selection sort
        for (int i = 0; i < puzzles.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < puzzles.size(); j++) {
                if (puzzles.get(j).compareTo(puzzles.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Collections.swap(puzzles, i, minIndex);
            }
        }
    }
}
