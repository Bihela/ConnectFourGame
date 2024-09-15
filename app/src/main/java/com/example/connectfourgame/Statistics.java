package com.example.connectfourgame;

public class Statistics {
    private final Player player1;
    private final Player player2;
    private int draws;

    public Statistics(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.draws = 0;
    }

    public void incrementWins(Player player) {
        player.incrementWins();
    }

    public void incrementDraws() {
        this.draws++;
    }

    public void printStats() {
        System.out.println("Game Statistics:");
        System.out.println(player1.getName() + " Wins: " + player1.getWins());
        System.out.println(player2.getName() + " Wins: " + player2.getWins());
        System.out.println("Draws: " + this.draws);
    }
}
