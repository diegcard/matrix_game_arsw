package org.arsw.usecases.interfaces;

/**
 * Port for game presentation following Clean Architecture principles.
 * This interface defines how the game communicates with the presentation layer.
 */
public interface GamePresenter {
    
    /**
     * Updates the game display with current state.
     */
    void updateGameDisplay(GameStateData gameState);
    
    /**
     * Shows victory message to the user.
     */
    void showVictory();
    
    /**
     * Shows defeat message to the user.
     */
    void showDefeat();
    
    /**
     * Updates the game status message.
     */
    void updateStatus(String message);
}