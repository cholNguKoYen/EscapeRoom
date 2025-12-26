/**
 * Exception thrown when the player tries to enter a locked room without a key.
 */
public class LockedRoomException extends Exception {
    public LockedRoomException(String message) {
        super(message);
    }
}

