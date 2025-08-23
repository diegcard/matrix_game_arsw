package org.arsw;

public class Entity {
    protected int x;
    protected int y;
    protected char symbol;

    public Entity(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSymbol() { return symbol; }
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
