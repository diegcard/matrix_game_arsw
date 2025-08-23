package org.arsw;

public class Neo extends Entity implements Runnable {
    private Matrix matrix;
    private boolean running = true;

    public Neo(int x, int y, Matrix matrix) {
        super(x, y, 'N');
        this.matrix = matrix;
    }

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

    public void stop() {
        running = false;
    }
}
