package org.arsw.adapters.repositories;

import org.arsw.usecases.interfaces.GameStateData;
import org.arsw.usecases.interfaces.GameStateRepository;

/**
 * In-memory implementation of GameStateRepository.
 * This adapter manages the game state in memory.
 */
public class InMemoryGameStateRepository implements GameStateRepository {
    private GameStateData currentGameState;

    @Override
    public void saveGameState(GameStateData gameState) {
        this.currentGameState = gameState;
    }

    @Override
    public GameStateData loadGameState() {
        return currentGameState;
    }

    @Override
    public void clearGameState() {
        this.currentGameState = null;
    }
}