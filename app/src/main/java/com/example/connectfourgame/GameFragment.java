package com.example.connectfourgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";
    private static final String ARG_IS_AI_ENABLED = "isAIEnabled";
    private static final String ARG_NUM_ROWS = "numRows";
    private static final String ARG_NUM_COLS = "numCols";
    private static final String ARG_PLAYER1_COLOR = "player1Color";
    private static final String ARG_PLAYER2_COLOR = "player2Color";
    private static final String ARG_PLAYER1_NAME = "player1Name";
    private static final String ARG_PLAYER2_NAME = "player2Name";
    private static final String BOARD_STATE_KEY = "boardState";
    private static final String CURRENT_PLAYER_KEY = "currentPlayer";
    private static final String TURNS_PLAYED_KEY = "turnsPlayed";

    private Game game;
    private RecyclerView gameBoard;
    private TextView currentPlayerTextView;
    private Button resetButton;
    private Button statsButton;
    private Button backButton;
    private GameGridAdapter adapter;

    private boolean isAIEnabled;
    private int numRows;
    private int numCols;
    private int player1Color;
    private int player2Color;
    private String player1Name;
    private String player2Name;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String KEY_TOTAL_GAMES = "totalGames_";
    private static final String KEY_WINS = "wins_";
    private static final String KEY_LOSSES = "losses_";
    private String currentUserId;

    public static GameFragment newInstance(boolean isAIEnabled, int numRows, int numCols,
                                           int player1Color, int player2Color,
                                           String player1Name, String player2Name) {
        Log.d(TAG, "Creating new instance with AI Enabled: " + isAIEnabled +
                ", Rows: " + numRows + ", Cols: " + numCols +
                ", Player 1 Color: " + player1Color + ", Player 2 Color: " + player2Color +
                ", Player 1 Name: " + player1Name + ", Player 2 Name: " + player2Name);
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_AI_ENABLED, isAIEnabled);
        args.putInt(ARG_NUM_ROWS, numRows);
        args.putInt(ARG_NUM_COLS, numCols);
        args.putInt(ARG_PLAYER1_COLOR, player1Color);
        args.putInt(ARG_PLAYER2_COLOR, player2Color);
        args.putString(ARG_PLAYER1_NAME, player1Name);
        args.putString(ARG_PLAYER2_NAME, player2Name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");

        if (getArguments() != null) {
            isAIEnabled = getArguments().getBoolean(ARG_IS_AI_ENABLED);
            numRows = getArguments().getInt(ARG_NUM_ROWS);
            numCols = getArguments().getInt(ARG_NUM_COLS);
            player1Color = getArguments().getInt(ARG_PLAYER1_COLOR);
            player2Color = getArguments().getInt(ARG_PLAYER2_COLOR);
            player1Name = getArguments().getString(ARG_PLAYER1_NAME);
            player2Name = getArguments().getString(ARG_PLAYER2_NAME);
            Log.d(TAG, "Arguments received: isAIEnabled=" + isAIEnabled +
                    ", numRows=" + numRows + ", numCols=" + numCols +
                    ", player1Color=" + player1Color + ", player2Color=" + player2Color +
                    ", player1Name=" + player1Name + ", player2Name=" + player2Name);
        }

        View view = inflater.inflate(R.layout.fragment_game, container, false);
        gameBoard = view.findViewById(R.id.game_board);
        currentPlayerTextView = view.findViewById(R.id.current_player_textview);
        resetButton = view.findViewById(R.id.reset_button);
        statsButton = view.findViewById(R.id.stats_button);
        backButton = view.findViewById(R.id.game_back_button);

        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        currentUserId = getArguments() != null ? getArguments().getString("USER_ID", "default_user") : "default_user";
        Log.d(TAG, "Current User ID: " + currentUserId);

        if (savedInstanceState != null) {
            char[][] savedBoard = (char[][]) savedInstanceState.getSerializable(BOARD_STATE_KEY);
            int savedCurrentPlayer = savedInstanceState.getInt(CURRENT_PLAYER_KEY);
            int savedTurnsPlayed = savedInstanceState.getInt(TURNS_PLAYED_KEY);
            game = new Game(new Player(player1Name, 'X'), new Player(player2Name, 'O'), savedBoard, savedCurrentPlayer, savedTurnsPlayed);
            Log.d(TAG, "Game restored from savedInstanceState");
        } else {
            game = new Game(new Player(player1Name, 'X'), new Player(player2Name, 'O'), numRows, numCols);
            game.setAIEnabled(isAIEnabled);
            Log.d(TAG, "New game created: AI Enabled: " + isAIEnabled);
        }

        setupGameBoard();
        updateUI();

        resetButton.setOnClickListener(v -> {
            Log.d(TAG, "Reset button clicked");
            game.resetGame();
            updateUI();
        });

        statsButton.setOnClickListener(v -> {
            Log.d(TAG, "Stats button clicked");
            navigateToStatistics();
        });

        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            navigateToStartFragment();
        });

        return view;
    }

    private void setupGameBoard() {
        Log.d(TAG, "Setting up game board");
        adapter = new GameGridAdapter(game, column -> {
            if (game.makeMove(column)) {
                Log.d(TAG, "Move made in column: " + column);
                if (game.checkWin()) {
                    boolean player1Wins = game.getCurrentPlayer().equals(game.getPlayer1());
                    Log.d(TAG, "Game won. Player 1 wins: " + player1Wins);
                    updateStatistics(player1Wins);
                    navigateToStatistics();
                } else {
                    game.nextTurn();
                    updateUI();
                }
            }
        });

        gameBoard.setLayoutManager(new GridLayoutManager(getContext(), game.getNumCols()));
        gameBoard.setAdapter(adapter);
        Log.d(TAG, "Game board set up with " + game.getNumCols() + " columns");
    }

    private void updateUI() {
        Log.d(TAG, "Updating UI. Current player: " + game.getCurrentPlayer().getName());
        currentPlayerTextView.setText(game.getCurrentPlayer().getName() + "'s turn");
        gameBoard.getAdapter().notifyDataSetChanged();
    }

    private void updateStatistics(boolean player1Wins) {
        Log.d(TAG, "Updating statistics. Player 1 wins: " + player1Wins);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int totalGames = sharedPreferences.getInt(KEY_TOTAL_GAMES + currentUserId, 0) + 1;
        int wins = sharedPreferences.getInt(KEY_WINS + currentUserId, 0);
        int losses = sharedPreferences.getInt(KEY_LOSSES + currentUserId, 0);

        if (player1Wins) {
            wins++;
        } else {
            losses++;
        }

        editor.putInt(KEY_TOTAL_GAMES + currentUserId, totalGames);
        editor.putInt(KEY_WINS + currentUserId, wins);
        editor.putInt(KEY_LOSSES + currentUserId, losses);
        editor.apply();

        Log.d(TAG, "Statistics updated - User ID: " + currentUserId + ", Total Games: " + totalGames + ", Wins: " + wins + ", Losses: " + losses);
    }

    private void navigateToStatistics() {
        Log.d(TAG, "Navigating to StatisticsFragment");
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", currentUserId);
        StatisticsFragment statisticsFragment = new StatisticsFragment();
        statisticsFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, statisticsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToStartFragment() {
        Log.d(TAG, "Navigating to StartFragment");
        StartFragment startFragment = new StartFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, startFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (game != null) {
            outState.putSerializable(BOARD_STATE_KEY, game.getBoardState());
            outState.putInt(CURRENT_PLAYER_KEY, game.getCurrentPlayerIndex());
            outState.putInt(TURNS_PLAYED_KEY, game.getTurnsPlayed());
            Log.d(TAG, "Game state saved");
        }
    }

    public void changeUser(String newUserId) {
        Log.d(TAG, "Changing user to: " + newUserId);
        // Clear the existing statistics for the old user ID
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOTAL_GAMES + currentUserId);
        editor.remove(KEY_WINS + currentUserId);
        editor.remove(KEY_LOSSES + currentUserId);
        editor.apply();

        // Update the current user ID
        this.currentUserId = newUserId;

        // Reset game state for the new user
        if (game != null) {
            game.resetGame();
        }
        updateUI();

        Log.d(TAG, "User ID changed to: " + newUserId);
    }
}
