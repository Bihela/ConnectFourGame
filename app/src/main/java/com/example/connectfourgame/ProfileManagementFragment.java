package com.example.connectfourgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class ProfileManagementFragment extends Fragment {

    private static final String TAG = "ProfileManagementFragment";

    private EditText editTextName;
    private ImageView imageViewAvatar;
    private Button buttonChooseAvatar;
    private Button buttonSaveProfile;
    private Button buttonBack;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfilePrefs";
    private static final String KEY_NAME = "name_";
    private static final String KEY_AVATAR = "avatar_";
    private static final String KEY_USER_ID = "user_id"; // Adjusted for consistent use
    private static final String KEY_TOTAL_GAMES = "total_games_";
    private static final String KEY_WINS = "wins_";
    private static final String KEY_LOSSES = "losses_";

    private int selectedAvatarResId = R.drawable.default_avatar;
    private String currentUserId; // Store the current user ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_profile_management, container, false);

        initializeViews(view);
        sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (getArguments() != null) {
            currentUserId = getArguments().getString(KEY_USER_ID, UUID.randomUUID().toString());
            Log.d(TAG, "Received User ID from arguments: " + currentUserId);
        } else {
            currentUserId = UUID.randomUUID().toString();
            Log.d(TAG, "Generated new User ID: " + currentUserId);
        }

        loadProfile();

        buttonChooseAvatar.setOnClickListener(v -> {
            Log.d(TAG, "Choose Avatar button clicked");
            showAvatarSelectionDialog();
        });
        buttonSaveProfile.setOnClickListener(v -> {
            Log.d(TAG, "Save Profile button clicked");
            saveProfile();
        });
        buttonBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked, popping back stack");
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    private void initializeViews(View view) {
        editTextName = view.findViewById(R.id.editTextName);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        buttonChooseAvatar = view.findViewById(R.id.buttonChooseAvatar);
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);
        buttonBack = view.findViewById(R.id.buttonBack);
    }

    private void loadProfile() {
        String name = sharedPreferences.getString(KEY_NAME + currentUserId, "");
        selectedAvatarResId = sharedPreferences.getInt(KEY_AVATAR + currentUserId, R.drawable.default_avatar);

        Log.d(TAG, "Loading profile for User ID: " + currentUserId);
        Log.d(TAG, "Loaded Name: " + name);
        Log.d(TAG, "Loaded Avatar Resource ID: " + selectedAvatarResId);

        editTextName.setText(name);
        imageViewAvatar.setImageResource(selectedAvatarResId);
    }

    private void saveProfile() {
        String newUserName = editTextName.getText().toString();
        String newUserId = UUID.randomUUID().toString(); // Generate a unique ID

        Log.d(TAG, "Saving profile. New User Name: " + newUserName);
        Log.d(TAG, "Generated New User ID: " + newUserId);

        if (!newUserId.equals(currentUserId)) {
            Log.d(TAG, "User ID changed. Previous User ID: " + currentUserId);
            clearUserStats(); // Clear the stats for the previous user
            updateProfile(newUserId, newUserName);
            changeUser(newUserId); // Update to the new user
        } else {
            Log.d(TAG, "No changes in User ID. Current User ID: " + newUserId);
        }
    }

    private void updateProfile(String newUserId, String newUserName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, newUserId);
        editor.putString(KEY_NAME + newUserId, newUserName);
        editor.putInt(KEY_AVATAR + newUserId, selectedAvatarResId);
        editor.apply();

        Log.d(TAG, "Profile saved:");
        Log.d(TAG, "User ID: " + newUserId);
        Log.d(TAG, "Name Key: " + KEY_NAME + newUserId + ", Name: " + newUserName);
        Log.d(TAG, "Avatar Key: " + KEY_AVATAR + newUserId + ", Avatar Resource ID: " + selectedAvatarResId);
    }

    private void showAvatarSelectionDialog() {
        int[] avatarResIds = {
                R.drawable.avatar_1,
                R.drawable.avatar_2,
                R.drawable.avatar_3,
                R.drawable.avatar_4
        };

        String[] avatarNames = {"Avatar 1", "Avatar 2", "Avatar 3", "Avatar 4"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Avatar");

        builder.setItems(avatarNames, (dialog, which) -> {
            selectedAvatarResId = avatarResIds[which];
            imageViewAvatar.setImageResource(selectedAvatarResId);
            Log.d(TAG, "Selected Avatar Resource ID: " + selectedAvatarResId);
        });

        builder.show();
    }

    private void changeUser(String newUserId) {
        // Update the current user ID
        this.currentUserId = newUserId;

        // Clear the existing statistics for the previous user ID
        clearUserStats();

        // Debug logs for checking which fragment is currently displayed
        Fragment currentFragment = getParentFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (currentFragment == null) {
            Log.e(TAG, "No fragment found in fragmentContainer");
        } else {
            Log.d(TAG, "Current Fragment: " + currentFragment.getClass().getSimpleName());
        }

        // Only update statistics in GameFragment
        if (currentFragment instanceof GameFragment) {
            GameFragment gameFragment = (GameFragment) currentFragment;
            gameFragment.changeUser(newUserId);  // Update the game fragment with the new user ID
            Log.d(TAG, "Changed user ID to: " + newUserId + " in GameFragment");
        } else {
            Log.e(TAG, "The current fragment is not a GameFragment. Current Fragment: " + currentFragment.getClass().getSimpleName());
        }

        Log.d(TAG, "User ID changed to: " + newUserId);
    }

    private void clearUserStats() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_GAMES + currentUserId, 0);
        editor.putInt(KEY_WINS + currentUserId, 0);
        editor.putInt(KEY_LOSSES + currentUserId, 0);
        editor.apply();

        Log.d(TAG, "User statistics have been cleared for user ID: " + currentUserId);
    }
}
