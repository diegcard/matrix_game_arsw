package org.arsw.usecases.impl;

import org.arsw.domain.entities.*;
import org.arsw.domain.services.CollisionService;
import org.arsw.domain.services.MovementService;
import org.arsw.usecases.interfaces.GamePresenter;
import org.arsw.usecases.interfaces.GameStateData;
import org.arsw.usecases.interfaces.GameStateRepository;

import java.util.List;

/**
 * Use case for moving Neo in the game.
 * This encapsulates the business logic for Neo's movement.
 */
public class MoveNeoUseCase {
    private final MovementService movementService;
    private final CollisionService collisionService;
    private final GameStateRepository gameStateRepository;
    private final GamePresenter gamePresenter;

    public MoveNeoUseCase(MovementService movementService, 
                         CollisionService collisionService,
                         GameStateRepository gameStateRepository, 
                         GamePresenter gamePresenter) {
        this.movementService = movementService;
        this.collisionService = collisionService;
        this.gameStateRepository = gameStateRepository;
        this.gamePresenter = gamePresenter;
    }

    public void execute() {
        GameStateData currentState = gameStateRepository.loadGameState();
        
        if (currentState.getGameState() != GameState.RUNNING) {
            return;
        }

        Position currentNeoPosition = currentState.getNeoPosition();
        List<Position> targets = currentState.getTargets();
        List<GameEntity> agents = currentState.getAgents();
        List<Position> walls = currentState.getWalls();
        int boardSize = currentState.getBoardSize();

        // Calculate best move for Neo
        Position newNeoPosition = movementService.calculateBestMoveForNeo(
            currentNeoPosition, targets, agents, boardSize);

        // Validate the move
        if (!movementService.isValidMove(newNeoPosition, boardSize, walls)) {
            newNeoPosition = currentNeoPosition; // Stay in place if invalid move
        }

        // Check for victory
        GameState newGameState = currentState.getGameState();
        if (collisionService.hasReachedTarget(newNeoPosition, targets)) {
            newGameState = GameState.VICTORY;
            gamePresenter.showVictory();
        }

        // Check for defeat
        if (collisionService.isCaughtByAgent(newNeoPosition, agents)) {
            newGameState = GameState.DEFEAT;
            gamePresenter.showDefeat();
        }

        // Update game state
        GameStateData newState = new GameStateData(
            newGameState, newNeoPosition, agents, targets, walls, boardSize);
        
        gameStateRepository.saveGameState(newState);
        gamePresenter.updateGameDisplay(newState);
        
        if (!newNeoPosition.equals(currentNeoPosition)) {
            gamePresenter.updateStatus("Neo se movió a: " + newNeoPosition);
        } else {
            gamePresenter.updateStatus("Neo no puede moverse - está bloqueado");
        }
    }
}