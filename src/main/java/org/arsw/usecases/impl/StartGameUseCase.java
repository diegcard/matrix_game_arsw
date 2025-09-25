package org.arsw.usecases.impl;

import org.arsw.domain.entities.GameState;
import org.arsw.usecases.interfaces.GamePresenter;
import org.arsw.usecases.interfaces.GameStateData;
import org.arsw.usecases.interfaces.GameStateRepository;

/**
 * Use case for starting a new game.
 * This encapsulates the business logic for game initialization.
 */
public class StartGameUseCase {
    private final GameStateRepository gameStateRepository;
    private final GamePresenter gamePresenter;

    public StartGameUseCase(GameStateRepository gameStateRepository, 
                           GamePresenter gamePresenter) {
        this.gameStateRepository = gameStateRepository;
        this.gamePresenter = gamePresenter;
    }

    public void execute(GameStateData initialGameState) {
        // Set game state to running
        GameStateData gameState = new GameStateData(
            GameState.RUNNING,
            initialGameState.getNeoPosition(),
            initialGameState.getAgents(),
            initialGameState.getTargets(),
            initialGameState.getWalls(),
            initialGameState.getBoardSize()
        );
        
        gameStateRepository.saveGameState(gameState);
        gamePresenter.updateGameDisplay(gameState);
        gamePresenter.updateStatus("Juego iniciado. ¡Ayuda a Neo a llegar al teléfono!");
    }
}