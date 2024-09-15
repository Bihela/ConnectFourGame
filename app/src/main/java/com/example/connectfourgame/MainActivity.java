package com.example.connectfourgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements StartFragment.OnStartGameListener, StartFragment.OnManageProfileListener {

    private static final String PREFS_NAME = "ConnectFourPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            StartFragment startFragment = new StartFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, startFragment)
                    .commit();
        }
    }

    @Override
    public void onStartGame(boolean isAIEnabled, String player1Color, String player2Color, String player1Name, String player2Name) {
        // Retrieve game settings from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int numRows = sharedPref.getInt("GridSize", 7);  // Default grid size is 7x6
        int numCols = numRows - 1;

        // Save game settings in SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("IsAIEnabled", isAIEnabled);
        editor.putInt("NumRows", numRows);
        editor.putInt("NumCols", numCols);
        editor.putString("Player1Color", player1Color);
        editor.putString("Player2Color", player2Color);
        editor.putString("Player1Name", player1Name);
        editor.putString("Player2Name", player2Name);
        editor.apply();

        // Start the game with the updated settings
        GameFragment gameFragment = GameFragment.newInstance(isAIEnabled, numRows, numCols,
                player1Color, player2Color, player1Name, player2Name);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onManageProfile() {
        // Navigate to the ProfileManagementFragment
        ProfileManagementFragment profileFragment = new ProfileManagementFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, profileFragment);
        transaction.addToBackStack(null);  // Add to back stack so user can return to StartFragment
        transaction.commit();
    }
}
