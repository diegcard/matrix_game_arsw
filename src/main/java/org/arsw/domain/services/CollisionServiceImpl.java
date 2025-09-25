package org.arsw.domain.services;

import org.arsw.domain.entities.GameEntity;
import org.arsw.domain.entities.Position;

import java.util.List;

/**
 * Implementation of the CollisionService.
 */
public class CollisionServiceImpl implements CollisionService {

    @Override
    public boolean hasReachedTarget(Position neoPosition, List<Position> targets) {
        if (targets == null || neoPosition == null) {
            return false;
        }
        
        return targets.contains(neoPosition);
    }

    @Override
    public boolean isCaughtByAgent(Position neoPosition, List<GameEntity> agents) {
        if (agents == null || neoPosition == null) {
            return false;
        }
        
        for (GameEntity agent : agents) {
            if (agent.getPosition().equals(neoPosition)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBlockedByWall(Position position, List<Position> walls) {
        if (walls == null || position == null) {
            return false;
        }
        
        return walls.contains(position);
    }
}