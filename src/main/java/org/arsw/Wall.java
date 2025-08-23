package org.arsw;

/**
 * Represents a wall entity in the game matrix.
 * A wall is a type of {@link Entity} that is typically used as an obstacle.
 * Walls are initialized with specific coordinates and a default symbol ('#').
 *
 * @author diego
 */
public class Wall extends Entity {

    /**
     * Constructs a Wall object at the specified coordinates.
     *
     * @param x the x-coordinate of the wall
     * @param y the y-coordinate of the wall
     */
    public Wall(int x, int y) {
        super(x, y, '#');
    }
}
