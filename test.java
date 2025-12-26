// public void winConditionCheck() {
// if (!player.getCurrentRoom().isExit()) {
// return; // Not at exit yet
// }

// // Check if at least one puzzle in Room 5B is solved
// boolean puzzle5BSolved = isAnyPuzzleIn5BSolved();

// if (puzzle5BSolved) {
// // Win condition met: at exit, and solved a puzzle in 5B
// System.out.println("\n" + "=".repeat(50));
// System.out.println("CONGRATULATIONS! YOU ESCAPED THE ROOM!");
// System.out.println("You solved a puzzle in Room 5B, and reached the exit!");
// System.out.println("Number of turns: " + turnCounter);
// System.out.println("=".repeat(50));
// gameRunning = false;
// } else {
// // At exit but missing required items or unsolved puzzles
// System.out.println("\n" + "=".repeat(50));
// if (!puzzle5BSolved) {
// System.out.println("You need to solve at least one puzzle in Room 5B to
// escape!");
// }
// System.out.println("You need to fulfill all conditions to escape!");
// System.out.println("=".repeat(50));
// }
// }