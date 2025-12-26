/**
 * Abstract class representing puzzles in the game.
 * Extends GameComponent and implements Comparable<Puzzle>.
 */
public abstract class Puzzle extends GameComponent implements Comparable<Puzzle> {
    protected int difficulty;
    protected boolean solved;
    
    public Puzzle(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
        this.solved = false;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public boolean isSolved() {
        return solved;
    }
    
    public void setSolved(boolean solved) {
        this.solved = solved;
    }
    
    @Override
    public void inspect() {
        System.out.println("Puzzle: " + name + " (Difficulty: " + difficulty + ", Solved: " + solved + ")");
    }
    
    /**
     * Abstract method to solve the puzzle.
     * @param answer Player's answer.
     * @return true if solved, false otherwise.
     * @throws InvalidPuzzleAnswerException if the answer is invalid.
     */
    public abstract boolean attemptSolve(String answer) throws InvalidPuzzleAnswerException;
    
    /**
     * Compare by difficulty.
     */
    @Override
    public int compareTo(Puzzle other) {
        return Integer.compare(this.difficulty, other.difficulty);
    }
}

