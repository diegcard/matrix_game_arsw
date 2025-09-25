package org.arsw.infrastructure.threading;

import org.arsw.domain.entities.GameEntity;
import org.arsw.usecases.impl.MoveAgentUseCase;
import org.arsw.usecases.impl.MoveNeoUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread manager for game entities.
 * This handles all threading concerns for the game.
 */
public class GameThreadManager {
    private Thread neoThread;
    private final List<Thread> agentThreads = new ArrayList<>();
    private NeoMovementTask neoTask;
    private final List<AgentMovementTask> agentTasks = new ArrayList<>();

    public void startGameThreads(MoveNeoUseCase moveNeoUseCase, 
                                MoveAgentUseCase moveAgentUseCase,
                                List<GameEntity> agents) {
        // Start Neo thread
        neoTask = new NeoMovementTask(moveNeoUseCase);
        neoThread = new Thread(neoTask);
        neoThread.start();

        // Start Agent threads
        for (GameEntity agent : agents) {
            AgentMovementTask agentTask = new AgentMovementTask(moveAgentUseCase, agent);
            agentTasks.add(agentTask);
            
            Thread agentThread = new Thread(agentTask);
            agentThreads.add(agentThread);
            agentThread.start();
        }
    }

    public void stopAllThreads() {
        // Stop Neo thread
        if (neoTask != null) {
            neoTask.stop();
        }

        // Stop Agent threads
        for (AgentMovementTask agentTask : agentTasks) {
            agentTask.stop();
        }

        // Wait for threads to finish
        try {
            if (neoThread != null && neoThread.isAlive()) {
                neoThread.join(1000);
            }
            
            for (Thread agentThread : agentThreads) {
                if (agentThread.isAlive()) {
                    agentThread.join(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Clear collections
        agentThreads.clear();
        agentTasks.clear();
        neoTask = null;
        neoThread = null;
    }
}