package org.arsw;

import org.arsw.domain.entities.*;
import org.arsw.infrastructure.config.DependencyConfig;
import org.arsw.infrastructure.threading.GameThreadManager;
import org.arsw.usecases.interfaces.GameStateData;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

/**
 * Clean Architecture implementation of the Matrix game controller.
 * This class extends Matrix for backward compatibility while implementing clean architecture principles.
 */
public class CleanMatrix extends Matrix {
    private final int size;
    private final DependencyConfig dependencies;
    private final GameThreadManager threadManager;
    private final MatrixGameGUI gui;

    // Game entities
    private Position neoPosition;
    private final List<GameEntity> agents = new ArrayList<>();
    private final List<Position> targets = new ArrayList<>();
    private final List<Position> walls = new ArrayList<>();

    public CleanMatrix(int size, MatrixGameGUI gui) {
        super(size, gui);
        this.size = size;
        this.gui = gui;
        this.dependencies = new DependencyConfig(
            gui != null ? gui.getStatusLabel() : null,
            gui != null ? gui.getButtons() : null,
            size
        );
        this.threadManager = new GameThreadManager();
    }

    @Override
    public void addNeo(Neo n) {
        super.addNeo(n); // Call parent for backward compatibility
        this.neoPosition = new Position(n.getX(), n.getY());
    }

    @Override
    public void addAgent(Agent a) {
        super.addAgent(a); // Call parent for backward compatibility
        GameEntity agentEntity = new GameEntity(EntityType.AGENT, 
                                              new Position(a.getX(), a.getY()));
        this.agents.add(agentEntity);
    }

    @Override
    public void addTarget(Target t) {
        super.addTarget(t); // Call parent for backward compatibility
        this.targets.add(new Position(t.getX(), t.getY()));
    }

    @Override
    public void addWall(Wall w) {
        super.addWall(w); // Call parent for backward compatibility
        this.walls.add(new Position(w.getX(), w.getY()));
    }

    @Override
    public void startGame() {
        // Create initial game state
        GameStateData initialState = new GameStateData(
            GameState.NOT_STARTED, neoPosition, agents, targets, walls, size
        );

        // Start the game
        dependencies.getStartGameUseCase().execute(initialState);

        // Start game threads
        threadManager.startGameThreads(
            dependencies.getMoveNeoUseCase(),
            dependencies.getMoveAgentUseCase(),
            agents
        );
    }

    @Override
    public void stopGame() {
        threadManager.stopAllThreads();
        dependencies.getGameStateRepository().clearGameState();
    }

    @Override
    public char[][] getBoard() {
        // Create board representation for legacy compatibility
        char[][] board = new char[size][size];
        
        // Initialize with empty spaces
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }

        // Get current game state
        GameStateData currentState = dependencies.getGameStateRepository().loadGameState();
        if (currentState == null) {
            return board;
        }

        // Place walls
        if (currentState.getWalls() != null) {
            for (Position wall : currentState.getWalls()) {
                if (wall.isWithinBounds(size)) {
                    board[wall.getX()][wall.getY()] = '#';
                }
            }
        }

        // Place targets
        if (currentState.getTargets() != null) {
            for (Position target : currentState.getTargets()) {
                if (target.isWithinBounds(size)) {
                    board[target.getX()][target.getY()] = 'T';
                }
            }
        }

        // Place agents
        if (currentState.getAgents() != null) {
            for (GameEntity agent : currentState.getAgents()) {
                Position pos = agent.getPosition();
                if (pos.isWithinBounds(size)) {
                    board[pos.getX()][pos.getY()] = 'A';
                }
            }
        }

        // Place Neo
        if (currentState.getNeoPosition() != null) {
            Position neo = currentState.getNeoPosition();
            if (neo.isWithinBounds(size)) {
                board[neo.getX()][neo.getY()] = 'N';
            }
        }

        return board;
    }

    // Legacy methods for backward compatibility
    public synchronized void moveNeo() {
        // This is now handled by the use case and threading infrastructure
        // Kept for backward compatibility but functionality is delegated
    }

    public synchronized void moveAgent(Agent agent) {
        // This is now handled by the use case and threading infrastructure
        // Kept for backward compatibility but functionality is delegated
    }

    @Override
    public boolean isFree(int x, int y) {
        Position pos = new Position(x, y);
        GameStateData currentState = dependencies.getGameStateRepository().loadGameState();
        if (currentState == null) {
            return pos.isWithinBounds(size);
        }

        // Check walls
        if (currentState.getWalls() != null && currentState.getWalls().contains(pos)) {
            return false;
        }

        // Check agents
        if (currentState.getAgents() != null) {
            for (GameEntity agent : currentState.getAgents()) {
                if (agent.getPosition().equals(pos)) {
                    return false;
                }
            }
        }

        return pos.isWithinBounds(size);
    }

    @Override
    public void printBoard() {
        char[][] board = getBoard();
        System.out.println("\nTablero:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}