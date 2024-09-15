package com.example.connectfourgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements StartFragment.OnStartGameListener, StartFragment.OnManageProfileListener {

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
    public void onStartGame(boolean isAIEnabled, int numRows, int numCols, int player1Color,
                            int player2Color, String player1Name, String player2Name) {
        // Save player names in SharedPreferences (profile creation)
        SharedPreferences sharedPref = getSharedPreferences("ConnectFourPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Player1Name", player1Name);
        editor.putString("Player2Name", player2Name);
        editor.apply();

        // Start the game
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
