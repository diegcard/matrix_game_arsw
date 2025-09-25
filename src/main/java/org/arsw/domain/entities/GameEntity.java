package org.arsw.domain.entities;

/**
 * Domain entity representing a game entity with position and type.
 * This replaces the original Entity class with domain-focused design.
 */
public final class GameEntity {
    private final EntityType type;
    private Position position;

    public GameEntity(EntityType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public EntityType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public char getSymbol() {
        return type.getSymbol();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameEntity that = (GameEntity) obj;
        return type == that.type && position.equals(that.position);
    }

    @Override
    public int hashCode() {
        return 31 * type.hashCode() + position.hashCode();
    }
}