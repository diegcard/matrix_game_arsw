package org.arsw;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private int size;
    private char[][] board;
    private Neo neo;
    private List<Target> targets = new ArrayList<>();
    private List<Agent> agents = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private MatrixGameGUI gui;
    private Thread neoThread;
    private List<Thread> agentThreads = new ArrayList<>();
    private boolean gameOver = false;

    public Matrix(int size) {
        this.size = size;
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
            }
        }
    }

    public Matrix(int size, MatrixGameGUI gui) {
        this(size);
        this.gui = gui;
    }

    public void addNeo(Neo n) {
        neo = n;
        board[n.getX()][n.getY()] = n.getSymbol();
    }

    public void addTarget(Target t) {
        targets.add(t);
        board[t.getX()][t.getY()] = t.getSymbol();
    }

    public void addAgent(Agent a) {
        agents.add(a);
        board[a.getX()][a.getY()] = a.getSymbol();
    }

    public void addWall(Wall w) {
        walls.add(w);
        board[w.getX()][w.getY()] = w.getSymbol();
    }

    public synchronized void moveNeo() {
        if (isGameOver()) return;

        System.out.println("Neo está en posición: (" + neo.getX() + ", " + neo.getY() + ")");

        int[][] directions = { {1,0}, {-1,0}, {0,1}, {0,-1} };
        int bestX = neo.getX(), bestY = neo.getY();
        int minDist = Integer.MAX_VALUE;
        boolean foundSafeMove = false;

        for (int[] dir : directions) {
            int nx = neo.getX() + dir[0];
            int ny = neo.getY() + dir[1];
            if (isFree(nx, ny) && !isAgentNear(nx, ny)) {
                int dist = getMinDistanceToAnyTarget(nx, ny);
                if (dist < minDist) {
                    minDist = dist;
                    bestX = nx;
                    bestY = ny;
                    foundSafeMove = true;
                }
            }
        }

        if (!foundSafeMove) {
            minDist = Integer.MAX_VALUE;
            for (int[] dir : directions) {
                int nx = neo.getX() + dir[0];
                int ny = neo.getY() + dir[1];
                if (isFree(nx, ny)) {
                    int dist = getMinDistanceToAnyTarget(nx, ny);
                    if (dist < minDist) {
                        minDist = dist;
                        bestX = nx;
                        bestY = ny;
                    }
                }
            }
        }

        if (bestX != neo.getX() || bestY != neo.getY()) {
            board[neo.getX()][neo.getY()] = '.';
            neo.setPosition(bestX, bestY);
            board[bestX][bestY] = neo.getSymbol();
            System.out.println("Neo se movió a: (" + bestX + ", " + bestY + ")");
        } else {
            System.out.println("Neo no puede moverse - está bloqueado");
        }

        if (gui != null) {
            gui.updateGUI();
        } else {
            printBoard();
        }
        checkVictory();
    }

    public synchronized void moveAgent(Agent agent) {
        if (isGameOver()) return;
        int[][] directions = { {1,0}, {-1,0}, {0,1}, {0,-1} };
        int bestX = agent.getX(), bestY = agent.getY();
        int minDist = Integer.MAX_VALUE;
        for (int[] dir : directions) {
            int nx = agent.getX() + dir[0];
            int ny = agent.getY() + dir[1];
            if (isFreeForAgent(nx, ny)) {
                int dist = manhattan(nx, ny, neo.getX(), neo.getY());
                if (dist < minDist) {
                    minDist = dist;
                    bestX = nx;
                    bestY = ny;
                }
            }
        }
        board[agent.getX()][agent.getY()] = '.';
        agent.setPosition(bestX, bestY);
        board[bestX][bestY] = agent.getSymbol();
        if (gui != null) {
            gui.updateGUI();
        } else {
            printBoard();
        }
        checkDefeat();
    }

    private boolean isFreeForAgent(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return false;
        if (board[x][y] == '#' || board[x][y] == 'A') return false;
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public void startGame() {
        neoThread = new Thread(neo);
        for (Agent agent : agents) {
            Thread agentThread = new Thread(agent);
            agentThreads.add(agentThread);
            agentThread.start();
        }
        neoThread.start();
    }

    public void stopGame() {
        gameOver = true;
        if (neo != null) neo.stop();
        for (Agent agent : agents) {
            agent.stop();
        }
    }

    private boolean isGameOver() {
        return gameOver;
    }

    private void checkVictory() {
        for (Target target : targets) {
            if (neo.getX() == target.getX() && neo.getY() == target.getY()) {
                System.out.println("¡Neo ha llegado al teléfono! ¡Victoria!");
                gameOver = true;
                if (gui != null) {
                    gui.showVictory();
                }
                stopAllThreads();
                return;
            }
        }
    }

    private void checkDefeat() {
        for (Agent a : agents) {
            if (a.getX() == neo.getX() && a.getY() == neo.getY()) {
                System.out.println("¡Neo ha sido atrapado por un agente! ¡Derrota!");
                gameOver = true;
                if (gui != null) {
                    gui.showDefeat();
                }
                stopAllThreads();
                break;
            }
        }
    }

    private void stopAllThreads() {
        neo.stop();
        for (Agent a : agents) {
            a.stop();
        }
    }

    private int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private int getMinDistanceToAnyTarget(int x, int y) {
        int minDist = Integer.MAX_VALUE;
        for (Target target : targets) {
            int dist = manhattan(x, y, target.getX(), target.getY());
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    private boolean isAgentNear(int x, int y) {
        for (Agent a : agents) {
            if (a.getX() == x && a.getY() == y) return true;
        }
        return false;
    }

    public boolean isFree(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return false;
        if (board[x][y] == '#') return false;
        for (Agent a : agents) {
            if (a.getX() == x && a.getY() == y) return false;
        }
        return true;
    }

    public void printBoard() {
        System.out.println("\nTablero:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
