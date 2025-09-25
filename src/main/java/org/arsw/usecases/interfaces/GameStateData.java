package org.arsw.usecases.interfaces;

import org.arsw.domain.entities.GameEntity;
import org.arsw.domain.entities.GameState;
import org.arsw.domain.entities.Position;

import java.util.List;

/**
 * Data transfer object for game state.
 * This class encapsulates all the game state information.
 */
public class GameStateData {
    private final GameState gameState;
    private final Position neoPosition;
    private final List<GameEntity> agents;
    private final List<Position> targets;
    private final List<Position> walls;
    private final int boardSize;

    public GameStateData(GameState gameState, Position neoPosition, 
                        List<GameEntity> agents, List<Position> targets, 
                        List<Position> walls, int boardSize) {
        this.gameState = gameState;
        this.neoPosition = neoPosition;
        this.agents = agents;
        this.targets = targets;
        this.walls = walls;
        this.boardSize = boardSize;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Position getNeoPosition() {
        return neoPosition;
    }

    public List<GameEntity> getAgents() {
        return agents;
    }

    public List<Position> getTargets() {
        return targets;
    }

    public List<Position> getWalls() {
        return walls;
    }

    public int getBoardSize() {
        return boardSize;
    }
}