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

public class StatisticsFragment extends Fragment {

    private static final String TAG = "StatisticsFragment";
    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String KEY_TOTAL_GAMES = "totalGames_";
    private static final String KEY_WINS = "wins_";
    private static final String KEY_LOSSES = "losses_";
    private String currentUserId;

    private TextView totalGamesTextView;
    private TextView winsTextView;
    private TextView lossesTextView;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        totalGamesTextView = view.findViewById(R.id.total_games_textview);
        winsTextView = view.findViewById(R.id.wins_textview);
        lossesTextView = view.findViewById(R.id.losses_textview);
        backButton = view.findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);

        if (getArguments() != null) {
            currentUserId = getArguments().getString("USER_ID", "default_user");
            Log.d(TAG, "Current user ID: " + currentUserId);

            int totalGames = sharedPreferences.getInt(KEY_TOTAL_GAMES + currentUserId, 0);
            int wins = sharedPreferences.getInt(KEY_WINS + currentUserId, 0);
            int losses = sharedPreferences.getInt(KEY_LOSSES + currentUserId, 0);

            if (totalGames == 0 && wins == 0 && losses == 0) {
                totalGamesTextView.setText("No statistics available.");
                winsTextView.setText("No statistics available.");
                lossesTextView.setText("No statistics available.");
            } else {
                totalGamesTextView.setText("Total Games: " + totalGames);
                winsTextView.setText("Wins: " + wins);
                lossesTextView.setText("Losses: " + losses);
            }
        } else {
            Log.e(TAG, "Arguments are null, unable to retrieve user ID.");
            totalGamesTextView.setText("No statistics available.");
            winsTextView.setText("No statistics available.");
            lossesTextView.setText("No statistics available.");
        }

        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked, navigating back.");
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
