# Escape Room Game - Quick README

This is a simple console-based escape-room game.

How to run

- Compile all `.java` files and run `Main`.

Basic commands (type after the prompt `> `)

- `look` : inspect current room (shows contents and subrooms)
- `move <room_name>` : move to a connected room or subroom
- `back` : go back to previous room
- `pickup <item_name>` : pick up an item present in the current room (including keys revealed by puzzles)
- `inventory [value|alpha]` : show inventory; optional `value` sorts by item value, `alpha` sorts alphabetically
- `solve <puzzle_name>` : attempt to solve a specific puzzle present in the current room or inside a subroom
- `solve <subroomName>` : if the named subroom contains multiple puzzles (eg. `5B`), the engine will let you sort and pick one to solve
- `map` : show the map tree (debug view)
- `help` : show command list
- `quit` / `exit` : exit game

Key mechanics and special behaviors

- Keys revealed by solving puzzles are now placed as items inside a room (usually the same subroom where the puzzle was solved). You must `pickup <key_name>` to add it to your inventory before using it to open locked rooms.
- Some locked rooms consume keys when you enter them (the key is removed from inventory). The `Exit_Key` is NOT consumed when opening the Exit Room.
- `5B` contains three puzzles. Use `solve 5B` to list and optionally sort puzzles by difficulty, then pick one to attempt. Solving a puzzle in `5B` reveals the Exit Room (a new connection from Room 5).
- `4B` contains a code lock; solving it will spawn the `Exit Key` inside `4B` (you must `pickup Exit Key`).

Winning the game

- Reach the `Exit Room` and have the `Exit Key` in your inventory.

Notes

- Room and item names are matched case-insensitively.
- If a revealed key is not in the expected room (unexpected state), the engine may fall back to placing it directly into your inventory.

========================TOPIC========================

# Programming Assignment Name: Escape Room

**Description:**
You wake up in a locked facility with multiple rooms. Each room can contain:

- Items (keys, tools, clues)
- Puzzles (riddles, code locks)
- Doors to other rooms (some locked)
- **Your goal:** collect what you need, solve puzzles, and reach the Exit.

Classes and Interfaces

**1) Abstract class: GameComponent**

- Represents anything in the game world.
- **Fields**

protected String name;

- **Methods**

public abstract void inspect();

public String getName()

Used for polymorphism.

**2) Class: Room (Recursive structure)**

- A room can contain other rooms → recursion.
- **Fields**

private ArrayList<GameComponent> contents; *(items, puzzles, sub rooms)*

private ArrayList<Room> connectedRooms;

private boolean isExit;

- **Required recursive methods**

public void exploreRecursive(int depth)

public boolean containsItemRecursive(String itemName)

public int maxDepthRecursive()

**3) Interface: Collectible**

public interface Collectible {

void collect(Player p);

}

**4) Class: Item extends GameComponent implements Collectible, Comparable<Item>**

- **Fields**

private int value;

private String itemType; (KEY, TOOL, CLUE)

- **Comparable**

Compare by value (or alphabetically)

**5)  Abstract class: Puzzle extends GameComponent implements Comparable<Puzzle>**

- **Fields**

protected int difficulty;

protected boolean solved;

- **Methods**

public abstract boolean attemptSolve(String answer) throws InvalidPuzzleAnswerException;

compareTo by difficulty

- **Subclasses (at least 2)**

RiddlePuzzle

CodePuzzle (e.g., numeric lock)

(optional) SequencePuzzle

**6) Class: Player**

- **Fields**

private Stack<Room> moveHistory; *(backtracking)*

private ArrayList<Item> inventory;

private Room currentRoom;

- **Methods**

moveTo(Room r) pushes old room into stack

goBack() pops from stack

pickupItem(String name)

boolean hasKey(String keyName)

**7) Class: GameEngine**

- Runs the game loop.
- **Collections used**

ArrayList<Room> map;

Queue<String> hintQueue; *(hints unlock every N turns)*

- **Methods**

start()

processCommand(String cmd)

printStatus()

winConditionCheck()

Game Commands

Minimum commands:

- look → show current room contents
- move <roomName> → go to connected room (validate)
- back → undo movement (Stack)
- pickup <itemName>
- inventory
- solve <puzzleName> → prompt for answer
- map → show recursive listing of the whole map (debug/cheat)

Rules

**Locked Doors**

- Some rooms require a key item to enter.
- If player lacks key → throw LockedRoomException

**Puzzles Unlock Stuff**

- Solving puzzles can:
- Reveal a key
- Unlock a room connection
- Add hint to queue

**Turn Counter & Hint Queue**

- Every 3 turns:
- dequeue one hint (if exists) and show it

Requirements:

**Required Sorting Features (Comparable)**

- Sort inventory by Item value (Comparable)
- Sort puzzles by difficulty (Comparable)
- Use insertion sort or selection sort on an ArrayList
- **Recursion Requirements**

Must include at least **two** of:

1.Recursive room traversal printing

2.Recursive search for an item/puzzle

3.Recursive depth calculation / counts

**Exception Requirements (Good Java practice)**

- Create custom exceptions:

LockedRoomException extends Exception

InvalidPuzzleAnswerException extends Exception

InvalidCommandException extends Exception

- Use try/catch in the game loop so the game never crashes.

**What you should submit**

All class files with documentation

A short “design write where recursion is used (base + recursive case) -up” explaining:

- what collections were used and why (stack/queue/list)
- A sample run output (screenshot or text file)

==============END TOPIC================
