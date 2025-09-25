package org.arsw.domain.services;

import org.arsw.domain.entities.*;

import java.util.List;

/**
 * Implementation of the MovementService that contains the core movement algorithms.
 */
public class MovementServiceImpl implements MovementService {

    @Override
    public Position calculateBestMoveForNeo(Position neoPosition, List<Position> targets, 
                                          List<GameEntity> agents, int boardSize) {
        Position bestPosition = neoPosition;
        int minDistance = Integer.MAX_VALUE;
        boolean foundSafeMove = false;

        // First try to find safe moves (away from agents)
        for (Direction direction : Direction.getAllDirections()) {
            Position candidatePosition = neoPosition.move(direction);
            
            if (!candidatePosition.isWithinBounds(boardSize)) {
                continue;
            }
            
            if (isSafeFromAgents(candidatePosition, agents)) {
                int distanceToNearestTarget = getMinDistanceToTargets(candidatePosition, targets);
                if (distanceToNearestTarget < minDistance) {
                    minDistance = distanceToNearestTarget;
                    bestPosition = candidatePosition;
                    foundSafeMove = true;
                }
            }
        }

        // If no safe move found, try any valid move
        if (!foundSafeMove) {
            minDistance = Integer.MAX_VALUE;
            for (Direction direction : Direction.getAllDirections()) {
                Position candidatePosition = neoPosition.move(direction);
                
                if (!candidatePosition.isWithinBounds(boardSize)) {
                    continue;
                }
                
                int distanceToNearestTarget = getMinDistanceToTargets(candidatePosition, targets);
                if (distanceToNearestTarget < minDistance) {
                    minDistance = distanceToNearestTarget;
                    bestPosition = candidatePosition;
                }
            }
        }

        return bestPosition;
    }

    @Override
    public Position calculateBestMoveForAgent(Position agentPosition, Position neoPosition, int boardSize) {
        Position bestPosition = agentPosition;
        int minDistance = Integer.MAX_VALUE;

        for (Direction direction : Direction.getAllDirections()) {
            Position candidatePosition = agentPosition.move(direction);
            
            if (!candidatePosition.isWithinBounds(boardSize)) {
                continue;
            }
            
            int distanceToNeo = candidatePosition.manhattanDistance(neoPosition);
            if (distanceToNeo < minDistance) {
                minDistance = distanceToNeo;
                bestPosition = candidatePosition;
            }
        }

        return bestPosition;
    }

    @Override
    public boolean isValidMove(Position position, int boardSize, List<Position> walls) {
        if (!position.isWithinBounds(boardSize)) {
            return false;
        }
        
        return walls == null || !walls.contains(position);
    }

    @Override
    public boolean isSafeFromAgents(Position position, List<GameEntity> agents) {
        if (agents == null) {
            return true;
        }
        
        for (GameEntity agent : agents) {
            if (agent.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    private int getMinDistanceToTargets(Position position, List<Position> targets) {
        if (targets.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        
        int minDistance = Integer.MAX_VALUE;
        for (Position target : targets) {
            int distance = position.manhattanDistance(target);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
}