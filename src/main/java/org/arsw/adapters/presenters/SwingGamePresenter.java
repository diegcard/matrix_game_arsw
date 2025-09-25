package org.arsw.adapters.presenters;

import org.arsw.domain.entities.EntityType;
import org.arsw.domain.entities.GameEntity;
import org.arsw.domain.entities.Position;
import org.arsw.usecases.interfaces.GamePresenter;
import org.arsw.usecases.interfaces.GameStateData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Swing implementation of GamePresenter.
 * This adapter handles the presentation logic for the Swing GUI.
 */
public class SwingGamePresenter implements GamePresenter {
    private final JLabel statusLabel;
    private final JButton[][] buttons;
    private final int size;

    public SwingGamePresenter(JLabel statusLabel, JButton[][] buttons, int size) {
        this.statusLabel = statusLabel;
        this.buttons = buttons;
        this.size = size;
    }

    @Override
    public void updateGameDisplay(GameStateData gameState) {
        SwingUtilities.invokeLater(() -> {
            // Clear the board first
            clearBoard();
            
            // Update Neo position
            Position neoPos = gameState.getNeoPosition();
            if (neoPos != null && isValidPosition(neoPos)) {
                buttons[neoPos.getX()][neoPos.getY()].setText("N");
                buttons[neoPos.getX()][neoPos.getY()].setBackground(Color.BLUE);
                buttons[neoPos.getX()][neoPos.getY()].setForeground(Color.WHITE);
            }
            
            // Update agents
            if (gameState.getAgents() != null) {
                for (GameEntity agent : gameState.getAgents()) {
                    Position pos = agent.getPosition();
                    if (isValidPosition(pos)) {
                        buttons[pos.getX()][pos.getY()].setText("A");
                        buttons[pos.getX()][pos.getY()].setBackground(Color.RED);
                        buttons[pos.getX()][pos.getY()].setForeground(Color.WHITE);
                    }
                }
            }
            
            // Update targets
            if (gameState.getTargets() != null) {
                for (Position target : gameState.getTargets()) {
                    if (isValidPosition(target)) {
                        buttons[target.getX()][target.getY()].setText("T");
                        buttons[target.getX()][target.getY()].setBackground(Color.GREEN);
                        buttons[target.getX()][target.getY()].setForeground(Color.WHITE);
                    }
                }
            }
            
            // Update walls
            if (gameState.getWalls() != null) {
                for (Position wall : gameState.getWalls()) {
                    if (isValidPosition(wall)) {
                        buttons[wall.getX()][wall.getY()].setText("#");
                        buttons[wall.getX()][wall.getY()].setBackground(Color.BLACK);
                        buttons[wall.getX()][wall.getY()].setForeground(Color.WHITE);
                    }
                }
            }
        });
    }

    @Override
    public void showVictory() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "¡Neo ha llegado al teléfono! ¡Victoria!", 
                                        "Victoria", JOptionPane.INFORMATION_MESSAGE);
            updateStatus("¡Victoria! Neo ha escapado de la Matrix.");
        });
    }

    @Override
    public void showDefeat() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "¡Neo ha sido atrapado por un agente! ¡Derrota!", 
                                        "Derrota", JOptionPane.ERROR_MESSAGE);
            updateStatus("¡Derrota! Neo ha sido capturado.");
        });
    }

    @Override
    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            if (statusLabel != null) {
                statusLabel.setText(message);
            }
        });
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText(" ");
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setForeground(Color.BLACK);
            }
        }
    }

    private boolean isValidPosition(Position pos) {
        return pos != null && pos.getX() >= 0 && pos.getX() < size && 
               pos.getY() >= 0 && pos.getY() < size;
    }
}