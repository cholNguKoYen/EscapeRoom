/**
 * RiddlePuzzle - a puzzle represented as a riddle.
 */
public class RiddlePuzzle extends Puzzle {
    private String riddle;
    private String answer;
    
    public RiddlePuzzle(String name, int difficulty, String riddle, String answer) {
        super(name, difficulty);
        this.riddle = riddle;
        this.answer = answer;
    }
    
    @Override
    public void inspect() {
        // Only show minimal metadata when inspecting generally.
        // The full riddle text should be shown only when the player chooses to solve.
        super.inspect(); 
    }
    
    @Override
    public boolean attemptSolve(String playerAnswer) throws InvalidPuzzleAnswerException {
        if (playerAnswer == null || playerAnswer.trim().isEmpty()) {
            throw new InvalidPuzzleAnswerException("Answer cannot be empty!");
        }
        
        if (playerAnswer.trim().equalsIgnoreCase(answer)) {
            solved = true;
            return true;
        }
        return false;
    }
    
    public String getRiddle() {
        return riddle;
    }

    /**
     * Return the answer (hack) for display when the player chooses to solve.
     */
    public String getAnswer() {
        return answer;
    }
}

