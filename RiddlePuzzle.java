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
        super.inspect();
        if (!solved) {
            System.out.println("Riddle: " + riddle);
            System.out.println("Hack answer: " + answer);
        }
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
}

