package org.arsw;

import org.arsw.domain.entities.*;
import org.arsw.domain.services.MovementServiceImpl;
import org.arsw.domain.services.CollisionServiceImpl;

import java.util.Arrays;
import java.util.Collections;

/**
 * Basic test to verify Clean Architecture implementation works correctly.
 */
public class CleanArchitectureTest {

    public static void main(String[] args) {
        System.out.println("Testing Clean Architecture implementation...");

        // Test domain entities
        testDomainEntities();
        
        // Test domain services
        testDomainServices();
        
        System.out.println("All tests passed! Clean Architecture is working correctly.");
    }

    private static void testDomainEntities() {
        System.out.println("Testing domain entities...");
        
        // Test Position
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(1, 1);
        Position moved = pos1.move(Direction.DOWN);
        
        assert moved.getX() == 1 : "Position move failed";
        assert pos1.manhattanDistance(pos2) == 2 : "Manhattan distance calculation failed";
        assert pos1.isWithinBounds(10) : "Bounds check failed";
        
        // Test GameEntity
        GameEntity neo = new GameEntity(EntityType.NEO, pos1);
        assert neo.getType() == EntityType.NEO : "Entity type failed";
        assert neo.getSymbol() == 'N' : "Entity symbol failed";
        
        System.out.println("✓ Domain entities working correctly");
    }

    private static void testDomainServices() {
        System.out.println("Testing domain services...");
        
        MovementServiceImpl movementService = new MovementServiceImpl();
        CollisionServiceImpl collisionService = new CollisionServiceImpl();
        
        // Test movement service
        Position neoPos = new Position(0, 0);
        Position target = new Position(2, 2);
        
        Position bestMove = movementService.calculateBestMoveForNeo(
            neoPos, Arrays.asList(target), Collections.emptyList(), 10);
        
        assert bestMove != null : "Movement calculation failed";
        assert bestMove.manhattanDistance(target) <= neoPos.manhattanDistance(target) 
            : "Movement should get closer to target";
        
        // Test collision service
        assert !collisionService.hasReachedTarget(neoPos, Arrays.asList(target)) 
            : "Should not have reached target";
        assert collisionService.hasReachedTarget(target, Arrays.asList(target)) 
            : "Should have reached target";
        
        System.out.println("✓ Domain services working correctly");
    }
}