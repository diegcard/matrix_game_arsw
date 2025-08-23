package org.arsw;

public class Agent extends Entity implements Runnable {
    private Matrix matrix;
    private boolean running = true;

    public Agent(int x, int y, Matrix matrix) {
        super(x, y, 'A');
        this.matrix = matrix;
    }

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

    public void stop() {
        running = false;
    }
}
