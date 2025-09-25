package org.arsw.usecases.interfaces;

/**
 * Port for game state repository following Clean Architecture principles.
 * This interface defines the contract for persisting and retrieving game state.
 */
public interface GameStateRepository {
    
    /**
     * Saves the current game state.
     */
    void saveGameState(GameStateData gameState);
    
    /**
     * Loads the current game state.
     */
    GameStateData loadGameState();
    
    /**
     * Clears the current game state.
     */
    void clearGameState();
}