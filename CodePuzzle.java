/**
 * CodePuzzle - a code-based puzzle (e.g., numeric lock)
 */
public class CodePuzzle extends Puzzle {
    private String correctCode;

    public CodePuzzle(String name, int difficulty, String correctCode) {
        super(name, difficulty);
        this.correctCode = correctCode;
    }

    @Override
    public void inspect() {
        // Only show minimal metadata when inspecting generally.
        // Full prompt/code should only be shown when the player chooses to solve.
        super.inspect();
    }

    @Override
    public boolean attemptSolve(String playerCode) throws InvalidPuzzleAnswerException {
        if (playerCode == null || playerCode.trim().isEmpty()) {
            throw new InvalidPuzzleAnswerException("Code cannot be empty!");
        }

        if (playerCode.trim().equals(correctCode)) {
            solved = true;
            return true;
        }
        return false;
    }

    /**
     * Return the correct code (hack) for display when the player chooses to solve.
     */
    public String getCorrectCode() {
        return correctCode;
    }
}
