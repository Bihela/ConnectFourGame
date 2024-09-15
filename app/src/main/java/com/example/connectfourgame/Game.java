package com.example.connectfourgame;

import android.util.Log;
import java.util.Random;

public class Game {
    private static final String TAG = "Game";
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private char[][] board;
    private int numRows;
    private int numCols;
    private int turnsPlayed;
    private boolean isAIEnabled;
    private String player1Color;
    private String player2Color;

    // Constructor for dynamic game settings with String colors
    public Game(Player player1, Player player2, int numRows, int numCols, String player1Color, String player2Color) {
        this.player1 = player1;
        this.player2 = player2;
        this.numRows = numRows;
        this.numCols = numCols;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        this.currentPlayer = player1;
        this.board = new char[numRows][numCols];
        this.turnsPlayed = 0;
        this.isAIEnabled = false;  // Default to false
        initializeBoard();
        Log.d(TAG, "Game initialized: Player1=" + player1.getName() + ", Player2=" + player2.getName() +
                ", numRows=" + numRows + ", numCols=" + numCols + ", player1Color=" + player1Color + ", player2Color=" + player2Color);
    }

    // Constructor for restoring the game state
    public Game(Player player1, Player player2, char[][] board, int currentPlayerTurn, int turnsPlayed) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayerTurn == 1 ? player1 : player2;
        this.board = board;
        this.turnsPlayed = turnsPlayed;
        this.numRows = board.length;
        this.numCols = board[0].length;
        Log.d(TAG, "Game restored: Player1=" + player1.getName() + ", Player2=" + player2.getName() +
                ", currentPlayer=" + currentPlayer.getName() + ", turnsPlayed=" + turnsPlayed);
    }

    // Setter for enabling AI
    public void setAIEnabled(boolean isAIEnabled) {
        this.isAIEnabled = isAIEnabled;
        Log.d(TAG, "AI Enabled: " + isAIEnabled);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getTurnsPlayed() {
        return turnsPlayed;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public boolean makeMove(int col) {
        for (int row = numRows - 1; row >= 0; row--) {
            if (board[row][col] == '\u0000') {
                board[row][col] = currentPlayer.getMarker();
                turnsPlayed++;
                Log.d(TAG, "Move made at column " + col + " by " + currentPlayer.getName());
                return true;
            }
        }
        Log.d(TAG, "Invalid move attempt at column " + col + " by " + currentPlayer.getName());
        return false;
    }

    public boolean checkWin() {
        boolean win = checkHorizontalWin() || checkVerticalWin() || checkDiagonalWin();
        Log.d(TAG, "Check win: " + win);
        return win;
    }

    private boolean checkHorizontalWin() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (board[row][col] != '\u0000' &&
                        board[row][col] == board[row][col + 1] &&
                        board[row][col] == board[row][col + 2] &&
                        board[row][col] == board[row][col + 3]) {
                    Log.d(TAG, "Horizontal win at row " + row + " starting column " + col);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVerticalWin() {
        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row <= numRows - 4; row++) {
                if (board[row][col] != '\u0000' &&
                        board[row][col] == board[row + 1][col] &&
                        board[row][col] == board[row + 2][col] &&
                        board[row][col] == board[row + 3][col]) {
                    Log.d(TAG, "Vertical win at column " + col + " starting row " + row);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalWin() {
        for (int row = 0; row <= numRows - 4; row++) {
            for (int col = 0; col <= numCols - 4; col++) {
                if (board[row][col] != '\u0000' &&
                        board[row][col] == board[row + 1][col + 1] &&
                        board[row][col] == board[row + 2][col + 2] &&
                        board[row][col] == board[row + 3][col + 3]) {
                    Log.d(TAG, "Diagonal win (down-right) starting at row " + row + " column " + col);
                    return true;
                }
            }
            for (int col = 3; col < numCols; col++) {
                if (board[row][col] != '\u0000' &&
                        board[row][col] == board[row + 1][col - 1] &&
                        board[row][col] == board[row + 2][col - 2] &&
                        board[row][col] == board[row + 3][col - 3]) {
                    Log.d(TAG, "Diagonal win (down-left) starting at row " + row + " column " + col);
                    return true;
                }
            }
        }
        return false;
    }

    public void nextTurn() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
        Log.d(TAG, "Next turn: " + currentPlayer.getName());

        if (isAIEnabled && currentPlayer == player2) {
            Log.d(TAG, "AI's turn.");
            aiMove();
            Log.d(TAG, "AI has made its move.");
            nextTurn();
        }
    }

    public void resetGame() {
        initializeBoard();
        turnsPlayed = 0;
        currentPlayer = player1;
        Log.d(TAG, "Game reset.");
    }

    private void initializeBoard() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                board[row][col] = '\u0000';
            }
        }
        Log.d(TAG, "Board initialized.");
    }

    public int aiMove() {
        Random random = new Random();
        int col;
        do {
            col = random.nextInt(numCols);
            Log.d(TAG, "AI attempting move at column " + col);
        } while (!isValidMove(col));
        makeMove(col);
        Log.d(TAG, "AI moved at column " + col);
        return col;
    }

    private boolean isValidMove(int col) {
        boolean valid = board[0][col] == '\u0000';
        Log.d(TAG, "Move validity check for column " + col + ": " + valid);
        return valid;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayer == player1 ? 1 : 2;
    }

    public char[][] getBoardState() {
        char[][] boardCopy = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, numCols);
        }
        return boardCopy;
    }
}
