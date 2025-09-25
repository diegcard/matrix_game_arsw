package org.arsw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatrixGameGUI extends JFrame {
    private CleanMatrix matrix;
    private JButton[][] buttons;
    private int size;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton resetButton;
    private Thread gameThread;

    public MatrixGameGUI(int size) {
        this.size = size;
        initializeGUI();
        matrix = new CleanMatrix(size, this);
    }

    private void initializeGUI() {
        setTitle("Matrix Game - Neo vs Agentes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Iniciar Juego");
        resetButton = new JButton("Reiniciar");
        statusLabel = new JLabel("Presiona 'Iniciar Juego' para generar un tablero aleatorio");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        controlPanel.add(startButton);
        controlPanel.add(resetButton);
        controlPanel.add(statusLabel);
        add(controlPanel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(size, size));
        buttons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setPreferredSize(new Dimension(40, 40));
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 16));
                buttons[i][j].setBackground(Color.WHITE);
                gamePanel.add(buttons[i][j]);
            }
        }
        add(gamePanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame() {
        startButton.setEnabled(false);
        statusLabel.setText("Juego en progreso...");

        gameThread = new Thread(() -> {
            setupGame();

            matrix.startGame();
        });
        gameThread.start();
    }

    private void setupGame() {
        BoardGenerator generator = new BoardGenerator(size);
        BoardGenerator.GeneratedBoard generatedBoard = generator.generateRandomBoard();

            Neo neo = new Neo(generatedBoard.neoPosition.x, generatedBoard.neoPosition.y, matrix);
        matrix.addNeo(neo);

        for (BoardGenerator.Position phonePos : generatedBoard.phonePositions) {
            Target target = new Target(phonePos.x, phonePos.y);
            matrix.addTarget(target);
        }

        for (BoardGenerator.Position agentPos : generatedBoard.agentPositions) {
            Agent agent = new Agent(agentPos.x, agentPos.y, matrix);
            matrix.addAgent(agent);
        }

        for (BoardGenerator.Position wallPos : generatedBoard.wallPositions) {
            Wall wall = new Wall(wallPos.x, wallPos.y);
            matrix.addWall(wall);
        }

        System.out.println("Tablero generado aleatoriamente:");
        System.out.println("Neo: " + generatedBoard.neoPosition);
        System.out.println("Teléfonos: " + generatedBoard.phonePositions.size());
        System.out.println("Agentes: " + generatedBoard.agentPositions.size());
        System.out.println("Muros: " + generatedBoard.wallPositions.size());

        updateGUI();
    }

    private void resetGame() {
        if (gameThread != null && gameThread.isAlive()) {
            matrix.stopGame();
            try {
                gameThread.join(1000);
            } catch (InterruptedException e) {
                gameThread.interrupt();
            }
        }

        matrix = new CleanMatrix(size, this);
        clearBoard();
        startButton.setEnabled(true);
        statusLabel.setText("Presiona 'Iniciar Juego' para generar un tablero aleatorio");
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText(" ");
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public void updateGUI() {
        SwingUtilities.invokeLater(() -> {
            char[][] board = matrix.getBoard();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    char symbol = board[i][j];
                    buttons[i][j].setText(String.valueOf(symbol));

                    switch (symbol) {
                        case 'N':
                            buttons[i][j].setBackground(Color.BLUE);
                            buttons[i][j].setForeground(Color.WHITE);
                            break;
                        case 'A':
                            buttons[i][j].setBackground(Color.RED);
                            buttons[i][j].setForeground(Color.WHITE);
                            break;
                        case 'T':
                            buttons[i][j].setBackground(Color.GREEN);
                            buttons[i][j].setForeground(Color.WHITE);
                            break;
                        case '#':
                            buttons[i][j].setBackground(Color.BLACK);
                            buttons[i][j].setForeground(Color.WHITE);
                            break;
                        default:
                            buttons[i][j].setBackground(Color.WHITE);
                            buttons[i][j].setForeground(Color.BLACK);
                            break;
                    }
                }
            }
        });
    }

    public void showVictory() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("¡Victoria! Neo llegó al teléfono");
            startButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "¡Neo ha llegado al teléfono! ¡Victoria!",
                    "Victoria", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void showDefeat() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("¡Derrota! Neo fue atrapado");
            startButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "¡Neo ha sido atrapado por un agente! ¡Derrota!",
                    "Derrota", JOptionPane.ERROR_MESSAGE);
        });
    }

    // Helper methods for Clean Architecture
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JButton[][] getButtons() {
        return buttons;
    }
}
