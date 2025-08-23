package org.arsw;

/**
 * The entry point of the Matrix Game application.
 * Initializes the game GUI with a matrix of size 10.
 *
 * Usage:
 * Run this class to start the Matrix Game.
 *
 * @author diego
 */
public class Main {

    /**
     * The entry point of the MatrixGame application.
     * Initializes the game GUI with a grid size of 10.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        int gridSize = (args.length > 0) ? Integer.parseInt(args[0]) : 10;
        new MatrixGameGUI(gridSize);
    }
}