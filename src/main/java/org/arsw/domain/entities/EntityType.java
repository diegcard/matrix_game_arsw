package org.arsw.domain.entities;

/**
 * Enumeration representing the different types of entities in the game.
 */
public enum EntityType {
    NEO('N'),
    AGENT('A'),
    TARGET('T'),
    WALL('#');

    private final char symbol;

    EntityType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}