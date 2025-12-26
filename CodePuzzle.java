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
        super.inspect();
        if (!solved) {
            System.out.println("Enter the code to unlock...");
            System.out.println("Hack answer: " + correctCode);
            System.out.println();
        }
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
}
