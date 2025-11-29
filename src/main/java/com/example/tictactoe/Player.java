package com.example.tictactoe;

enum Player {
    X('X'),
    O('O');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    char symbol() {
        return symbol;
    }

    Player next() {
        return this == X ? O : X;
    }
}
