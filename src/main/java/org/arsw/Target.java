package org.arsw;

/**
 * Represents a target entity in the game matrix.
 *
 * @author diego
 */
public class Target extends Entity {
    
    /**
     * Constructs a new Target object at the specified coordinates.
     *
     * @param x the x-coordinate of the target
     * @param y the y-coordinate of the target
     */
    public Target(int x, int y) {
        super(x, y, 'T');
    }
}
