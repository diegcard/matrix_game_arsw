package org.arsw.domain.services;

import org.arsw.domain.entities.GameEntity;
import org.arsw.domain.entities.Position;

import java.util.List;

/**
 * Domain service responsible for collision detection and game state validation.
 */
public interface CollisionService {
    
    /**
     * Checks if Neo has reached any target.
     *
     * @param neoPosition Current position of Neo
     * @param targets List of target positions
     * @return true if Neo is at a target position
     */
    boolean hasReachedTarget(Position neoPosition, List<Position> targets);
    
    /**
     * Checks if Neo has been caught by any agent.
     *
     * @param neoPosition Current position of Neo
     * @param agents List of agent entities
     * @return true if Neo is at the same position as any agent
     */
    boolean isCaughtByAgent(Position neoPosition, List<GameEntity> agents);
    
    /**
     * Checks if a position is blocked by a wall.
     *
     * @param position Position to check
     * @param walls List of wall positions
     * @return true if the position is blocked by a wall
     */
    boolean isBlockedByWall(Position position, List<Position> walls);
}