package org.arsw.domain.services;

import org.arsw.domain.entities.Direction;
import org.arsw.domain.entities.GameEntity;
import org.arsw.domain.entities.Position;

import java.util.List;

/**
 * Domain service responsible for movement calculations and validations.
 * This service contains the core business logic for entity movement.
 */
public interface MovementService {
    
    /**
     * Calculates the best move for Neo towards the nearest target while avoiding agents.
     *
     * @param neoPosition Current position of Neo
     * @param targets List of target positions
     * @param agents List of agent entities
     * @param boardSize Size of the game board
     * @return The best direction to move, or null if no valid move exists
     */
    Position calculateBestMoveForNeo(Position neoPosition, List<Position> targets, 
                                   List<GameEntity> agents, int boardSize);
    
    /**
     * Calculates the best move for an agent to chase Neo.
     *
     * @param agentPosition Current position of the agent
     * @param neoPosition Current position of Neo
     * @param boardSize Size of the game board
     * @return The best position to move to chase Neo
     */
    Position calculateBestMoveForAgent(Position agentPosition, Position neoPosition, int boardSize);
    
    /**
     * Validates if a position is valid for movement (within bounds and not blocked).
     *
     * @param position Position to validate
     * @param boardSize Size of the game board
     * @param walls List of wall positions
     * @return true if the position is valid for movement
     */
    boolean isValidMove(Position position, int boardSize, List<Position> walls);
    
    /**
     * Checks if a position is safe from agents (no agent is adjacent).
     *
     * @param position Position to check
     * @param agents List of agent entities
     * @return true if the position is safe from agents
     */
    boolean isSafeFromAgents(Position position, List<GameEntity> agents);
}