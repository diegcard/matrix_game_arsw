package org.arsw.infrastructure.config;

import org.arsw.adapters.presenters.SwingGamePresenter;
import org.arsw.adapters.repositories.InMemoryGameStateRepository;
import org.arsw.domain.services.CollisionService;
import org.arsw.domain.services.CollisionServiceImpl;
import org.arsw.domain.services.MovementService;
import org.arsw.domain.services.MovementServiceImpl;
import org.arsw.usecases.impl.MoveAgentUseCase;
import org.arsw.usecases.impl.MoveNeoUseCase;
import org.arsw.usecases.impl.StartGameUseCase;
import org.arsw.usecases.interfaces.GamePresenter;
import org.arsw.usecases.interfaces.GameStateRepository;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Dependency injection configuration for the Clean Architecture.
 * This class wires up all the dependencies following the dependency rule.
 */
public class DependencyConfig {
    
    // Infrastructure
    private final GameStateRepository gameStateRepository;
    private final GamePresenter gamePresenter;
    
    // Domain Services
    private final MovementService movementService;
    private final CollisionService collisionService;
    
    // Use Cases
    private final StartGameUseCase startGameUseCase;
    private final MoveNeoUseCase moveNeoUseCase;
    private final MoveAgentUseCase moveAgentUseCase;

    public DependencyConfig(JLabel statusLabel, JButton[][] buttons, int boardSize) {
        // Create infrastructure dependencies
        this.gameStateRepository = new InMemoryGameStateRepository();
        this.gamePresenter = new SwingGamePresenter(statusLabel, buttons, boardSize);
        
        // Create domain services
        this.movementService = new MovementServiceImpl();
        this.collisionService = new CollisionServiceImpl();
        
        // Create use cases with dependencies
        this.startGameUseCase = new StartGameUseCase(gameStateRepository, gamePresenter);
        this.moveNeoUseCase = new MoveNeoUseCase(movementService, collisionService, 
                                               gameStateRepository, gamePresenter);
        this.moveAgentUseCase = new MoveAgentUseCase(movementService, collisionService, 
                                                   gameStateRepository, gamePresenter);
    }

    public StartGameUseCase getStartGameUseCase() {
        return startGameUseCase;
    }

    public MoveNeoUseCase getMoveNeoUseCase() {
        return moveNeoUseCase;
    }

    public MoveAgentUseCase getMoveAgentUseCase() {
        return moveAgentUseCase;
    }

    public GameStateRepository getGameStateRepository() {
        return gameStateRepository;
    }

    public GamePresenter getGamePresenter() {
        return gamePresenter;
    }
}