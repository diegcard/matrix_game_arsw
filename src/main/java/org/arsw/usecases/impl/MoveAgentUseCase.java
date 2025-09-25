package org.arsw.usecases.impl;

import org.arsw.domain.entities.*;
import org.arsw.domain.services.CollisionService;
import org.arsw.domain.services.MovementService;
import org.arsw.usecases.interfaces.GamePresenter;
import org.arsw.usecases.interfaces.GameStateData;
import org.arsw.usecases.interfaces.GameStateRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for moving agents in the game.
 * This encapsulates the business logic for agent movement.
 */
public class MoveAgentUseCase {
    private final MovementService movementService;
    private final CollisionService collisionService;
    private final GameStateRepository gameStateRepository;
    private final GamePresenter gamePresenter;

    public MoveAgentUseCase(MovementService movementService, 
                           CollisionService collisionService,
                           GameStateRepository gameStateRepository, 
                           GamePresenter gamePresenter) {
        this.movementService = movementService;
        this.collisionService = collisionService;
        this.gameStateRepository = gameStateRepository;
        this.gamePresenter = gamePresenter;
    }

    public void execute(GameEntity agent) {
        GameStateData currentState = gameStateRepository.loadGameState();
        
        if (currentState.getGameState() != GameState.RUNNING) {
            return;
        }

        Position currentAgentPosition = agent.getPosition();
        Position neoPosition = currentState.getNeoPosition();
        List<Position> walls = currentState.getWalls();
        int boardSize = currentState.getBoardSize();

        // Calculate best move for agent
        Position newAgentPosition = movementService.calculateBestMoveForAgent(
            currentAgentPosition, neoPosition, boardSize);

        // Validate the move
        if (!movementService.isValidMove(newAgentPosition, boardSize, walls)) {
            newAgentPosition = currentAgentPosition; // Stay in place if invalid move
        }

        // Update agent position
        agent.setPosition(newAgentPosition);

        // Update agents list
        List<GameEntity> updatedAgents = new ArrayList<>(currentState.getAgents());
        for (int i = 0; i < updatedAgents.size(); i++) {
            if (updatedAgents.get(i).getPosition().equals(currentAgentPosition)) {
                updatedAgents.set(i, agent);
                break;
            }
        }

        // Check for defeat
        GameState newGameState = currentState.getGameState();
        if (collisionService.isCaughtByAgent(neoPosition, updatedAgents)) {
            newGameState = GameState.DEFEAT;
            gamePresenter.showDefeat();
        }

        // Update game state
        GameStateData newState = new GameStateData(
            newGameState, neoPosition, updatedAgents, 
            currentState.getTargets(), walls, boardSize);
        
        gameStateRepository.saveGameState(newState);
        gamePresenter.updateGameDisplay(newState);
    }
}