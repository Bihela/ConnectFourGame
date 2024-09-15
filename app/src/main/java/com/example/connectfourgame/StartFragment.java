package com.example.connectfourgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.connectfourgame.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {

    private static final String TAG = "StartFragment";
    private FragmentStartBinding binding;
    private OnStartGameListener callback;
    private OnManageProfileListener profileCallback;

    public interface OnStartGameListener {
        void onStartGame(boolean isAIEnabled, int numRows, int numCols, String player1Color,
                         String player2Color, String player1Name, String player2Name);
    }

    public interface OnManageProfileListener {
        void onManageProfile();  // Method for managing profile
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnStartGameListener) {
            callback = (OnStartGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartGameListener");
        }

        if (context instanceof OnManageProfileListener) {
            profileCallback = (OnManageProfileListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnManageProfileListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Retrieve player names from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("ConnectFourPrefs", Context.MODE_PRIVATE);
        String player1Name = sharedPref.getString("Player1Name", "Player 1");
        String player2Name = sharedPref.getString("Player2Name", "Player 2");
        binding.player1NameEditText.setText(player1Name);
        binding.player2NameEditText.setText(player2Name);

        // Load grid size and player colors from SharedPreferences
        int numRows = sharedPref.getInt("GridSize", 7);  // Default grid size is 7x6
        String player1Color = sharedPref.getString("Player1Color", "#FF0000");  // Default color for Player 1 is red
        String player2Color = sharedPref.getString("Player2Color", "#FFFF00");  // Default color for Player 2 is yellow

        binding.startGameButton.setOnClickListener(v -> {
            boolean isAIEnabled = binding.aiSwitch.isChecked();
            String p1Name = binding.player1NameEditText.getText().toString();
            String p2Name = binding.player2NameEditText.getText().toString();

            // Debug logs for verification
            Log.d(TAG, "AI Enabled: " + isAIEnabled);
            Log.d(TAG, "Player 1 Name: " + p1Name);
            Log.d(TAG, "Player 2 Name: " + p2Name);
            Log.d(TAG, "Grid Size: " + numRows);
            Log.d(TAG, "Player 1 Color: " + player1Color);
            Log.d(TAG, "Player 2 Color: " + player2Color);

            // Start the game with selected settings
            callback.onStartGame(isAIEnabled, numRows, numRows - 1, player1Color, player2Color, p1Name, p2Name);
        });

        // Manage profile button functionality
        binding.manageProfileButton.setOnClickListener(v -> {
            profileCallback.onManageProfile();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
