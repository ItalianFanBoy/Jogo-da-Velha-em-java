package com.example.tictactoe;

import java.util.ArrayList;
import java.util.List;

final class AiEngine {
    private AiEngine() {
    }

    static int[] bestMove(char[][] board, Player aiPlayer) {
        Player human = aiPlayer.next();
        int bestScore = Integer.MIN_VALUE;
        int[] best = {-1, -1};

        for (int[] move : availableMoves(board)) {
            char[][] clone = cloneBoard(board);
            clone[move[0]][move[1]] = aiPlayer.symbol();
            int score = minimax(clone, false, aiPlayer, human, 0);
            if (score > bestScore) {
                bestScore = score;
                best = move;
            }
        }
        return best;
    }

    private static int minimax(char[][] state, boolean maximizing, Player aiPlayer, Player human, int depth) {
        GameResult result = GameEngine.evaluate(state);
        if (result.status() == GameResult.Status.WIN) {
            if (result.winner() == aiPlayer) {
                return 10 - depth;
            }
            return depth - 10;
        }
        if (result.status() == GameResult.Status.DRAW) {
            return 0;
        }

        int bestScore = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Player current = maximizing ? aiPlayer : human;

        for (int[] move : availableMoves(state)) {
            char[][] clone = cloneBoard(state);
            clone[move[0]][move[1]] = current.symbol();
            int score = minimax(clone, !maximizing, aiPlayer, human, depth + 1);
            if (maximizing) {
                bestScore = Math.max(bestScore, score);
            } else {
                bestScore = Math.min(bestScore, score);
            }
        }
        return bestScore;
    }

    private static List<int[]> availableMoves(char[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == ' ') {
                    moves.add(new int[] {r, c});
                }
            }
        }
        return moves;
    }

    private static char[][] cloneBoard(char[][] board) {
        char[][] clone = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, board[i].length);
        }
        return clone;
    }
}
