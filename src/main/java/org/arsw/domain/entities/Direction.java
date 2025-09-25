package org.arsw.domain.entities;

/**
 * Enumeration representing the four possible movement directions in the game.
 * Each direction contains the displacement values for x and y coordinates.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public static Direction[] getAllDirections() {
        return values();
    }
}