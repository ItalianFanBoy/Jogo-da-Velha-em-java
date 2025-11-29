package com.example.tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

final class TicTacToeFrame extends JFrame {
    private final JButton[][] cells = new JButton[3][3];
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Placar", SwingConstants.CENTER);
    private final GameEngine engine;
    private final JComboBox<Player> starterSelector = new JComboBox<>(Player.values());
    private final JToggleButton aiToggle = new JToggleButton("Jogar contra IA (O)");
    private int scoreX;
    private int scoreO;
    private int draws;

    TicTacToeFrame() {
        super("Jogo da Velha - Desafio Complexo");
        this.engine = new GameEngine(Player.X);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildLayout();
        refreshBoard();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void buildLayout() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                button.setFont(buttonFont);
                button.setFocusPainted(false);
                button.addActionListener(evt -> handleHumanMove(button));
                cells[r][c] = button;
                boardPanel.add(button);
            }
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel controls = new JPanel();
        JButton newGame = new JButton("Nova partida");
        newGame.addActionListener(this::restartGame);

        JButton resetScore = new JButton("Zerar placar");
        resetScore.addActionListener(evt -> {
            scoreX = 0;
            scoreO = 0;
            draws = 0;
            updateScore();
        });

        starterSelector.addActionListener(evt -> restartGame(evt));
        aiToggle.addActionListener(evt -> restartGame(evt));

        controls.add(new JLabel("Começa: "));
        controls.add(starterSelector);
        controls.add(aiToggle);
        controls.add(newGame);
        controls.add(resetScore);

        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        scoreLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        infoPanel.add(statusLabel);
        infoPanel.add(scoreLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        add(controls, BorderLayout.NORTH);
    }

    private void handleHumanMove(JButton button) {
        if (engine.result().status() != GameResult.Status.IN_PROGRESS) {
            return;
        }

        int[] coord = locateButton(button);
        if (coord == null) {
            return;
        }

        if (engine.playMove(coord[0], coord[1])) {
            refreshBoard();
            afterMoveActions();
        }
    }

    private void afterMoveActions() {
        GameResult result = engine.result();
        if (result.status() == GameResult.Status.IN_PROGRESS && aiToggle.isSelected() && engine.currentPlayer() == Player.O) {
            Timer timer = new Timer(180, evt -> {
                int[] move = AiEngine.bestMove(engine.snapshot(), Player.O);
                engine.playMove(move[0], move[1]);
                refreshBoard();
                afterMoveActions();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            updateStatus(result);
            if (result.status() == GameResult.Status.WIN || result.status() == GameResult.Status.DRAW) {
                updateScoreForResult(result);
            }
        }
    }

    private void updateScoreForResult(GameResult result) {
        if (result.status() == GameResult.Status.WIN) {
            if (result.winner() == Player.X) {
                scoreX++;
            } else {
                scoreO++;
            }
        } else if (result.status() == GameResult.Status.DRAW) {
            draws++;
        }
        updateScore();
    }

    private void updateStatus(GameResult result) {
        switch (result.status()) {
            case IN_PROGRESS -> statusLabel.setText("Vez de: " + engine.currentPlayer().symbol());
            case DRAW -> statusLabel.setText("Empate! Pressione Nova partida para jogar novamente.");
            case WIN -> statusLabel.setText("Vitória de " + result.winner().symbol() + "!");
            default -> statusLabel.setText("");
        }
    }

    private void refreshBoard() {
        GameResult result = engine.result();
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                JButton button = cells[r][c];
                char symbol = engine.snapshot()[r][c];
                button.setText(symbol == ' ' ? "" : Character.toString(symbol));
                button.setEnabled(result.status() == GameResult.Status.IN_PROGRESS && symbol == ' ');
                button.setBackground(Color.WHITE);
            }
        }

        if (result.status() == GameResult.Status.WIN) {
            highlightWinningCells(result.winningCells());
        }

        updateStatus(result);
        updateScore();
    }

    private void highlightWinningCells(List<int[]> cellsToHighlight) {
        for (int[] pos : cellsToHighlight) {
            cells[pos[0]][pos[1]].setBackground(new Color(144, 238, 144));
        }
    }

    private void updateScore() {
        String text = String.format("X: %d    O: %d    Empates: %d", scoreX, scoreO, draws);
        scoreLabel.setText(text);
    }

    private int[] locateButton(JButton button) {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c] == button) {
                    return new int[] {r, c};
                }
            }
        }
        return null;
    }

    private void restartGame(ActionEvent evt) {
        Player starter = (Player) starterSelector.getSelectedItem();
        engine.reset(starter == null ? Player.X : starter);
        refreshBoard();
    }

    static void launch() {
        SwingUtilities.invokeLater(() -> {
            TicTacToeFrame frame = new TicTacToeFrame();
            frame.setVisible(true);
        });
    }
}
