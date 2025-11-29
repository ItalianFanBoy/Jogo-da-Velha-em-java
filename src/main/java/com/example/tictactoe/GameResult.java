package com.example.tictactoe;

import java.util.Collections;
import java.util.List;

final class GameResult {
    enum Status {
        IN_PROGRESS,
        DRAW,
        WIN
    }

    private final Status status;
    private final Player winner;
    private final List<int[]> winningCells;

    GameResult(Status status, Player winner, List<int[]> winningCells) {
        this.status = status;
        this.winner = winner;
        this.winningCells = winningCells == null ? List.of() : List.copyOf(winningCells);
    }

    static GameResult inProgress() {
        return new GameResult(Status.IN_PROGRESS, null, List.of());
    }

    static GameResult draw() {
        return new GameResult(Status.DRAW, null, List.of());
    }

    static GameResult win(Player winner, List<int[]> cells) {
        return new GameResult(Status.WIN, winner, cells);
    }

    Status status() {
        return status;
    }

    Player winner() {
        return winner;
    }

    List<int[]> winningCells() {
        return Collections.unmodifiableList(winningCells);
    }
}
