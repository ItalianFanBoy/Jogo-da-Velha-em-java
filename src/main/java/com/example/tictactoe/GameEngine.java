package com.example.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class GameEngine {
    private static final int SIZE = 3;

    private final char[][] board = new char[SIZE][SIZE];
    private Player currentPlayer;
    private GameResult currentResult = GameResult.inProgress();

    GameEngine(Player startingPlayer) {
        reset(startingPlayer);
    }

    void reset(Player startingPlayer) {
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }
        currentPlayer = startingPlayer;
        currentResult = GameResult.inProgress();
    }

    Player currentPlayer() {
        return currentPlayer;
    }

    GameResult result() {
        return currentResult;
    }

    char[][] snapshot() {
        char[][] copy = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    boolean playMove(int row, int col) {
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
            return false;
        }
        if (board[row][col] != ' ' || currentResult.status() != GameResult.Status.IN_PROGRESS) {
            return false;
        }

        board[row][col] = currentPlayer.symbol();
        currentResult = evaluate(board);
        if (currentResult.status() == GameResult.Status.IN_PROGRESS) {
            currentPlayer = currentPlayer.next();
        }
        return true;
    }

    static GameResult evaluate(char[][] state) {
        for (int[][] line : winningLines()) {
            char first = state[line[0][0]][line[0][1]];
            if (first == ' ') {
                continue;
            }
            boolean win = true;
            for (int i = 1; i < line.length; i++) {
                int r = line[i][0];
                int c = line[i][1];
                if (state[r][c] != first) {
                    win = false;
                    break;
                }
            }
            if (win) {
                Player winner = first == Player.X.symbol() ? Player.X : Player.O;
                List<int[]> cells = new ArrayList<>();
                for (int[] pos : line) {
                    cells.add(new int[] {pos[0], pos[1]});
                }
                return GameResult.win(winner, cells);
            }
        }

        if (isBoardFull(state)) {
            return GameResult.draw();
        }

        return GameResult.inProgress();
    }

    private static boolean isBoardFull(char[][] state) {
        for (char[] row : state) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<int[][]> winningLines() {
        return List.of(
            new int[][] {{0, 0}, {0, 1}, {0, 2}},
            new int[][] {{1, 0}, {1, 1}, {1, 2}},
            new int[][] {{2, 0}, {2, 1}, {2, 2}},
            new int[][] {{0, 0}, {1, 0}, {2, 0}},
            new int[][] {{0, 1}, {1, 1}, {2, 1}},
            new int[][] {{0, 2}, {1, 2}, {2, 2}},
            new int[][] {{0, 0}, {1, 1}, {2, 2}},
            new int[][] {{0, 2}, {1, 1}, {2, 0}}
        );
    }
}
