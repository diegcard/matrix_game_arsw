package org.arsw;

/**
 * Represents a basic entity in the game matrix with a position and a symbol.
 *
 * @author diego
 */
public class Entity {
    protected int x;
    protected int y;
    protected char symbol;

    /**
     * Constructs a new Entity with the specified position and symbol.
     *
     * @param x the x-coordinate of the entity's position
     * @param y the y-coordinate of the entity's position
     * @param symbol the character symbol representing the entity
     */
    public Entity(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    /**
     * Returns the X coordinate of this entity.
     *
     * @return the X position as an integer.
     */
    public int getX() { return x; }

    /**
     * Returns the Y-coordinate of this entity.
     *
     * @return the Y-coordinate value.
     */
    public int getY() { return y; }

    /**
     * Returns the symbol representing this entity.
     *
     * @return the character symbol of the entity
     */
    public char getSymbol() { return symbol; }

    /**
     * Sets the position of the entity to the specified coordinates.
     *
     * @param x the new x-coordinate of the entity
     * @param y the new y-coordinate of the entity
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
