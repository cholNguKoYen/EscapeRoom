/**
 * Exception thrown when the player gives an invalid answer to a puzzle.
 */
public class InvalidPuzzleAnswerException extends Exception {
    public InvalidPuzzleAnswerException(String message) {
        super(message);
    }
}

