package org.arsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BoardGenerator is responsible for creating random game boards with various entities
 * including Neo (player), phones (targets), agents (enemies), and walls (obstacles).
 * The board follows a grid-based layout where different entities are strategically placed
 * to ensure playable and balanced game scenarios.
 *
 * @author diego
 */
public class BoardGenerator {
    private int size;
    private Random random;
    private char[][] board;

    /**
     * Constructs a new BoardGenerator with the specified board size.
     * Initializes the random number generator and creates an empty board matrix.
     *
     * @param size The dimension of the square board (size x size)
     */
    public BoardGenerator(int size) {
        this.size = size;
        this.random = new Random();
        this.board = new char[size][size];
    }

    /**
     * Generates a complete random board with all entities placed according to game rules.
     * This is the main method that orchestrates the generation process by calling
     * individual generation methods for each entity type in the correct order.
     *
     * @return GeneratedBoard object containing positions of all entities on the board
     */
    public GeneratedBoard generateRandomBoard() {
        clearBoard();
        Position neoPos = generateNeoPosition();
        List<Position> phonePositions = generatePhonePositions(neoPos);
        List<Position> agentPositions = generateAgentPositions(neoPos, phonePositions);
        List<Position> wallPositions = generateWallPositions(neoPos, phonePositions, agentPositions);

        return new GeneratedBoard(neoPos, phonePositions, agentPositions, wallPositions);
    }

    /**
     * Clears the entire board by setting all positions to empty ('.') characters.
     * This method is called at the beginning of board generation to ensure a clean slate.
     */
    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }
    }

    /**
     * Generates Neo's starting position in one of the four corners of the board.
     * Neo is placed randomly in the first third of one of the board edges to ensure
     * he starts near a corner but with some variation.
     *
     * @return Position object representing Neo's coordinates on the board
     */
    private Position generateNeoPosition() {
        int corner = random.nextInt(4);
        Position pos;
        switch (corner) {
            case 0: pos = new Position(0, random.nextInt(size/3)); break; // Top edge
            case 1: pos = new Position(size-1, random.nextInt(size/3)); break; // Bottom edge
            case 2: pos = new Position(random.nextInt(size/3), 0); break; // Left edge
            default: pos = new Position(random.nextInt(size/3), size-1); break; // Right edge
        }
        board[pos.x][pos.y] = 'N';
        return pos;
    }

    /**
     * Generates up to 2 phone positions in the second half of the board.
     * Phones are placed far from Neo's position (at least half the board size away)
     * to create meaningful objectives. Uses multiple attempts to find valid positions.
     *
     * @param neoPos Neo's position to calculate minimum distance requirements
     * @return List of Position objects representing phone locations (may be less than 2 if placement fails)
     */
    private List<Position> generatePhonePositions(Position neoPos) {
        List<Position> phonePositions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Position phonePos = null;
            int attempts = 0;

            do {
                int x = size/2 + random.nextInt(size/2);
                int y = size/2 + random.nextInt(size/2);
                phonePos = new Position(x, y);
                attempts++;
            } while ((isPositionOccupied(phonePos) || manhattanDistance(neoPos, phonePos) < size/2)
                    && attempts < 50);

            if (phonePos != null && !isPositionOccupied(phonePos)) {
                board[phonePos.x][phonePos.y] = 'T';
                phonePositions.add(phonePos);
            }
        }

        return phonePositions;
    }

    /**
     * Generates up to 2 agent positions in the middle area of the board.
     * Agents are placed at least 3 Manhattan distance units away from Neo
     * to prevent immediate game over scenarios. Agents act as moving obstacles/enemies.
     *
     * @param neoPos Neo's position to ensure minimum safe distance
     * @param phonePositions List of phone positions (currently unused but maintained for consistency)
     * @return List of Position objects representing agent starting locations
     */
    private List<Position> generateAgentPositions(Position neoPos, List<Position> phonePositions) {
        List<Position> agentPositions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Position agentPos = null;
            int attempts = 0;

            do {
                int x = size/4 + random.nextInt(size/2);
                int y = size/4 + random.nextInt(size/2);
                agentPos = new Position(x, y);
                attempts++;
            } while ((isPositionOccupied(agentPos) || manhattanDistance(neoPos, agentPos) < 3)
                    && attempts < 50);

            if (agentPos != null && !isPositionOccupied(agentPos)) {
                board[agentPos.x][agentPos.y] = 'A';
                agentPositions.add(agentPos);
            }
        }

        return agentPositions;
    }

    /**
     * Generates 15-25 wall positions randomly across the board.
     * Walls serve as static obstacles but are prevented from being placed on direct
     * horizontal or vertical paths between Neo and phones to ensure game solvability.
     *
     * @param neoPos Neo's position for path validation
     * @param phonePositions List of phone positions for path validation
     * @param agentPositions List of agent positions (currently unused but maintained for consistency)
     * @return List of Position objects representing wall locations
     */
    private List<Position> generateWallPositions(Position neoPos, List<Position> phonePositions, List<Position> agentPositions) {
        List<Position> wallPositions = new ArrayList<>();
        int numWalls = 15 + random.nextInt(11); // 15-25 walls

        for (int i = 0; i < numWalls; i++) {
            Position wallPos = null;
            int attempts = 0;

            do {
                int x = random.nextInt(size);
                int y = random.nextInt(size);
                wallPos = new Position(x, y);
                attempts++;
            } while ((isPositionOccupied(wallPos) || wouldBlockPath(wallPos, neoPos, phonePositions))
                    && attempts < 100);

            if (wallPos != null && !isPositionOccupied(wallPos)) {
                board[wallPos.x][wallPos.y] = '#';
                wallPositions.add(wallPos);
            }
        }

        return wallPositions;
    }

    /**
     * Checks if a given position is already occupied by another entity or is out of bounds.
     * This method is used throughout the generation process to avoid overlapping entities.
     *
     * @param pos The position to check for occupation
     * @return true if the position is occupied or out of bounds, false if it's available
     */
    private boolean isPositionOccupied(Position pos) {
        if (pos.x < 0 || pos.x >= size || pos.y < 0 || pos.y >= size) return true;
        return board[pos.x][pos.y] != '.';
    }

    /**
     * Determines if placing a wall at the given position would block direct paths
     * from Neo to any of the phones. This ensures basic game solvability by preventing
     * walls from creating impossible scenarios on straight-line paths.
     *
     * @param wallPos The proposed wall position
     * @param neoPos Neo's position
     * @param phonePositions List of all phone positions to check paths to
     * @return true if the wall would block a direct path, false otherwise
     */
    private boolean wouldBlockPath(Position wallPos, Position neoPos, List<Position> phonePositions) {
        for (Position phonePos : phonePositions) {
            if (isOnDirectPath(neoPos, phonePos, wallPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a wall position lies on a direct horizontal or vertical path between two points.
     * This method only considers straight-line paths (same row or column) and determines
     * if the wall would be positioned directly between the start and end points.
     *
     * @param start The starting position of the path
     * @param end The ending position of the path
     * @param wall The wall position to test
     * @return true if the wall is on the direct path between start and end, false otherwise
     */
    private boolean isOnDirectPath(Position start, Position end, Position wall) {
        if (start.x == end.x && wall.x == start.x) {
            int minY = Math.min(start.y, end.y);
            int maxY = Math.max(start.y, end.y);
            return wall.y >= minY && wall.y <= maxY;
        }
        if (start.y == end.y && wall.y == start.y) {
            int minX = Math.min(start.x, end.x);
            int maxX = Math.max(start.x, end.x);
            return wall.x >= minX && wall.x <= maxX;
        }
        return false;
    }

    /**
     * Calculates the Manhattan distance between two positions.
     * Manhattan distance is the sum of absolute differences of coordinates,
     * representing the minimum number of moves needed in a grid-based movement system.
     *
     * @param a First position
     * @param b Second position
     * @return The Manhattan distance between the two positions
     */
    private int manhattanDistance(Position a, Position b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Represents a coordinate position on the game board.
     * This inner class encapsulates x and y coordinates and provides
     * basic functionality for position representation.
     */
    public static class Position {
        public int x;
        public int y;

        /**
         * Constructs a new Position with the specified coordinates.
         *
         * @param x The x-coordinate (row)
         * @param y The y-coordinate (column)
         */
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns a string representation of the position in the format "(x, y)".
         *
         * @return String representation of the position coordinates
         */
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    /**
     * Container class that holds all the generated board information.
     * This class encapsulates the results of the board generation process,
     * providing easy access to all entity positions for game initialization.
     */
    public static class GeneratedBoard {
        public Position neoPosition;
        public List<Position> phonePositions;
        public List<Position> agentPositions;
        public List<Position> wallPositions;

        /**
         * Constructs a GeneratedBoard with all entity positions.
         *
         * @param neoPosition Neo's position on the board
         * @param phonePositions List of phone positions
         * @param agentPositions List of agent positions
         * @param wallPositions List of wall positions
         */
        public GeneratedBoard(Position neoPosition, List<Position> phonePositions,
                              List<Position> agentPositions, List<Position> wallPositions) {
            this.neoPosition = neoPosition;
            this.phonePositions = phonePositions;
            this.agentPositions = agentPositions;
            this.wallPositions = wallPositions;
        }
    }
}