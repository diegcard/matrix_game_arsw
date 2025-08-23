package org.arsw;

/**
 * Represents an agent entity that can move within a Matrix.
 * The Agent runs in its own thread and periodically moves within the matrix.
 * The movement is controlled by the {@link Matrix#moveAgent(Agent)} method.
 * The agent can be stopped by calling the {@link #stop()} method.
 *
 * @author diego
 * 
 */
public class Agent extends Entity implements Runnable {
    private Matrix matrix;
    private boolean running = true;

    /**
     * Constructs an Agent at the specified coordinates within the given matrix.
     *
     * @param x the x-coordinate of the Agent's initial position
     * @param y the y-coordinate of the Agent's initial position
     * @param matrix the Matrix object in which the Agent operates
     */
    public Agent(int x, int y, Matrix matrix) {
        super(x, y, 'A');
        this.matrix = matrix;
    }

    /**
     * Continuously moves the agent within the matrix while the agent is running.
     * The agent's movement is handled by the {@code matrix.moveAgent(this)} method.
     * The thread sleeps for 500 milliseconds between each move to control the movement speed.
     * If the thread is interrupted during sleep, the interrupt status is restored.
     */
    @Override
    public void run() {
        while (running) {
            matrix.moveAgent(this);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stops the agent by setting the running flag to false.
     * This method can be used to signal the agent to halt its execution.
     */
    public void stop() {
        running = false;
    }
}
