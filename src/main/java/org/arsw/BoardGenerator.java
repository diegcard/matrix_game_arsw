package org.arsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardGenerator {
    private int size;
    private Random random;
    private char[][] board;

    public BoardGenerator(int size) {
        this.size = size;
        this.random = new Random();
        this.board = new char[size][size];
    }

    public GeneratedBoard generateRandomBoard() {
        clearBoard();
        Position neoPos = generateNeoPosition();
        List<Position> phonePositions = generatePhonePositions(neoPos);
        List<Position> agentPositions = generateAgentPositions(neoPos, phonePositions);
        List<Position> wallPositions = generateWallPositions(neoPos, phonePositions, agentPositions);

        return new GeneratedBoard(neoPos, phonePositions, agentPositions, wallPositions);
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }
    }

    private Position generateNeoPosition() {
        int corner = random.nextInt(4);
        Position pos;
        switch (corner) {
            case 0: pos = new Position(0, random.nextInt(size/3)); break;
            case 1: pos = new Position(size-1, random.nextInt(size/3)); break;
            case 2: pos = new Position(random.nextInt(size/3), 0); break;
            default: pos = new Position(random.nextInt(size/3), size-1); break;
        }
        board[pos.x][pos.y] = 'N';
        return pos;
    }

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

    private List<Position> generateWallPositions(Position neoPos, List<Position> phonePositions, List<Position> agentPositions) {
        List<Position> wallPositions = new ArrayList<>();
        int numWalls = 15 + random.nextInt(11);

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

    private boolean isPositionOccupied(Position pos) {
        if (pos.x < 0 || pos.x >= size || pos.y < 0 || pos.y >= size) return true;
        return board[pos.x][pos.y] != '.';
    }

    private boolean wouldBlockPath(Position wallPos, Position neoPos, List<Position> phonePositions) {
        for (Position phonePos : phonePositions) {
            if (isOnDirectPath(neoPos, phonePos, wallPos)) {
                return true;
            }
        }
        return false;
    }

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

    private int manhattanDistance(Position a, Position b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static class Position {
        public int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static class GeneratedBoard {
        public Position neoPosition;
        public List<Position> phonePositions;
        public List<Position> agentPositions;
        public List<Position> wallPositions;

        public GeneratedBoard(Position neoPosition, List<Position> phonePositions,
                              List<Position> agentPositions, List<Position> wallPositions) {
            this.neoPosition = neoPosition;
            this.phonePositions = phonePositions;
            this.agentPositions = agentPositions;
            this.wallPositions = wallPositions;
        }
    }
}
