package org.arsw;

/**
 * Test to verify backward compatibility with original Matrix class.
 */
public class BackwardCompatibilityTest {

    public static void main(String[] args) {
        System.out.println("Testing backward compatibility...");
        
        // Test that CleanMatrix can be used as Matrix
        Matrix matrix = new CleanMatrix(10, null);
        
        // Test basic operations
        Neo neo = new Neo(0, 0, matrix);
        matrix.addNeo(neo);
        
        Agent agent = new Agent(1, 1, matrix);
        matrix.addAgent(agent);
        
        Target target = new Target(9, 9);
        matrix.addTarget(target);
        
        Wall wall = new Wall(5, 5);
        matrix.addWall(wall);
        
        // Test board generation
        char[][] board = matrix.getBoard();
        assert board != null : "Board should not be null";
        assert board.length == 10 : "Board size should be 10";
        
        // Test isFree method
        assert matrix.isFree(2, 2) : "Position (2,2) should be free";
        assert !matrix.isFree(5, 5) : "Position (5,5) should be blocked by wall";
        
        System.out.println("✓ Backward compatibility maintained");
        System.out.println("✓ CleanMatrix works as a drop-in replacement for Matrix");
    }
}