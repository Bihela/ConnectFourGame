package com.example.connectfourgame;

public class Player {
    private final String name;
    private final char marker; // Marker 'X' or 'O'
    private int score;
    private int wins; // Counter for wins

    // Constructor with name and marker
    public Player(String name, char marker) {
        this.name = name;
        this.marker = marker;
        this.score = 0;
        this.wins = 0; // Initialize wins to 0
    }

    public String getName() {
        return name;
    }

    public char getMarker() {
        return marker;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }
}
