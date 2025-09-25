package org.arsw.infrastructure.threading;

import org.arsw.domain.entities.GameEntity;
import org.arsw.usecases.impl.MoveNeoUseCase;
import org.arsw.usecases.impl.MoveAgentUseCase;

/**
 * Threading infrastructure for Neo's movement.
 * This handles the threading concerns separate from business logic.
 */
public class NeoMovementTask implements Runnable {
    private final MoveNeoUseCase moveNeoUseCase;
    private volatile boolean running = true;

    public NeoMovementTask(MoveNeoUseCase moveNeoUseCase) {
        this.moveNeoUseCase = moveNeoUseCase;
    }

    @Override
    public void run() {
        while (running) {
            moveNeoUseCase.execute();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }
}