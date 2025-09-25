# Clean Architecture Implementation

This document describes the Clean Architecture implementation applied to the Matrix Game.

## Architecture Overview

The project now follows Clean Architecture principles with clear separation of concerns:

```
src/main/java/org/arsw/
├── domain/                    # Domain Layer (Enterprise Business Rules)
│   ├── entities/             # Domain Entities
│   │   ├── Position.java     # Value object for coordinates
│   │   ├── Direction.java    # Movement directions enum
│   │   ├── GameState.java    # Game state enum
│   │   ├── GameEntity.java   # Domain entity
│   │   └── EntityType.java   # Entity types enum
│   └── services/             # Domain Services
│       ├── MovementService.java      # Movement business logic interface
│       ├── MovementServiceImpl.java  # Movement algorithms
│       ├── CollisionService.java     # Collision detection interface
│       └── CollisionServiceImpl.java # Collision detection logic
├── usecases/                 # Use Cases Layer (Application Business Rules)
│   ├── interfaces/           # Use Case Interfaces
│   │   ├── GameStateRepository.java  # Game state persistence port
│   │   ├── GameStateData.java        # Game state data transfer object
│   │   └── GamePresenter.java        # Presentation port
│   └── impl/                 # Use Case Implementations
│       ├── StartGameUseCase.java     # Game initialization
│       ├── MoveNeoUseCase.java       # Neo movement logic
│       └── MoveAgentUseCase.java     # Agent movement logic
├── adapters/                 # Interface Adapters Layer
│   ├── presenters/           # Presentation Adapters
│   │   └── SwingGamePresenter.java   # Swing GUI presenter
│   └── repositories/         # Data Access Adapters
│       └── InMemoryGameStateRepository.java # In-memory state storage
├── infrastructure/           # Infrastructure Layer
│   ├── config/               # Configuration and DI
│   │   └── DependencyConfig.java     # Dependency injection setup
│   └── threading/            # Threading infrastructure
│       ├── GameThreadManager.java    # Thread management
│       ├── NeoMovementTask.java      # Neo threading task
│       └── AgentMovementTask.java    # Agent threading task
├── CleanMatrix.java          # Clean Architecture implementation
└── [Original classes...]    # Legacy classes (maintained for compatibility)
```

## Key Benefits

### 1. **Separation of Concerns**
- **Domain Layer**: Pure business logic, no external dependencies
- **Use Cases**: Application-specific business rules
- **Interface Adapters**: Translation between external systems and use cases
- **Infrastructure**: External systems, frameworks, and tools

### 2. **Dependency Inversion**
- Inner layers don't depend on outer layers
- Dependencies point inward (following the Dependency Rule)
- Interfaces define contracts, implementations can be easily replaced

### 3. **Testability**
- Business logic is isolated and easily testable
- Dependencies can be mocked for unit testing
- Each layer can be tested independently

### 4. **Maintainability**
- Changes to external systems don't affect business logic
- New features can be added by extending use cases
- Code is organized by business capabilities, not technical concerns

### 5. **Flexibility**
- Easy to change UI frameworks (currently Swing, could be web-based)
- Easy to change persistence mechanisms (currently in-memory, could be database)
- Easy to add new features without affecting existing code

## Backward Compatibility

The `CleanMatrix` class extends the original `Matrix` class, ensuring:
- ✅ All existing code continues to work without modification
- ✅ Public APIs remain unchanged
- ✅ Legacy classes are preserved
- ✅ Drop-in replacement for the original Matrix class

## Usage Example

```java
// The clean architecture is used transparently
Matrix matrix = new CleanMatrix(10, gui);  // Uses clean architecture internally

// All original methods still work
Neo neo = new Neo(0, 0, matrix);
matrix.addNeo(neo);
matrix.startGame();
```

## Testing

Two test classes verify the implementation:

1. **CleanArchitectureTest**: Tests domain layer functionality
2. **BackwardCompatibilityTest**: Ensures original API still works

Run tests with:
```bash
mvn test-compile
java -cp target/classes:target/test-classes org.arsw.CleanArchitectureTest
java -cp target/classes:target/test-classes org.arsw.BackwardCompatibilityTest
```

## Future Enhancements

The clean architecture enables easy addition of:
- Database persistence
- Web-based UI
- AI-powered agents
- Multiplayer functionality
- Game replay system
- Performance monitoring
- Different game rules/modes

All of these can be added without modifying the core business logic in the domain layer.