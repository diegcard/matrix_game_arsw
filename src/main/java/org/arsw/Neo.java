package org.arsw;

/**
 * Represents the Neo entity in the Matrix game.
 * Neo is a character that moves within the matrix and runs in its own thread.
 * The movement logic is handled by the associated Matrix instance.
 *
 * @author diego
 */
public class Neo extends Entity implements Runnable {
    private Matrix matrix;
    private boolean running = true;

    /**
     * Constructs a new Neo object at the specified coordinates within the given Matrix.
     *
     * @param x the x-coordinate of Neo's initial position
     * @param y the y-coordinate of Neo's initial position
     * @param matrix the Matrix instance in which Neo exists
     */
    public Neo(int x, int y, Matrix matrix) {
        super(x, y, 'N');
        this.matrix = matrix;
    }

    /**
     * Continuously moves Neo within the matrix while the thread is running.
     * The method loops as long as the {@code running} flag is true, invoking {@code matrix.moveNeo()} 
     * to update Neo's position. After each move, the thread sleeps for 500 milliseconds to control 
     * the movement speed. If the thread is interrupted during sleep, it resets the interrupt status.
     */
    @Override
    public void run() {
        while (running) {
            matrix.moveNeo();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stops the current process or thread by setting the running flag to false.
     * This method can be used to gracefully halt execution.
     */
    public void stop() {
        running = false;
    }
}
