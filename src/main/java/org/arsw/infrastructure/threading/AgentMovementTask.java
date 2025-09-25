package org.arsw.infrastructure.threading;

import org.arsw.domain.entities.GameEntity;
import org.arsw.usecases.impl.MoveAgentUseCase;

/**
 * Threading infrastructure for Agent's movement.
 * This handles the threading concerns separate from business logic.
 */
public class AgentMovementTask implements Runnable {
    private final MoveAgentUseCase moveAgentUseCase;
    private final GameEntity agent;
    private volatile boolean running = true;

    public AgentMovementTask(MoveAgentUseCase moveAgentUseCase, GameEntity agent) {
        this.moveAgentUseCase = moveAgentUseCase;
        this.agent = agent;
    }

    @Override
    public void run() {
        while (running) {
            moveAgentUseCase.execute(agent);
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