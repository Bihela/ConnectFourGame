package com.example.connectfourgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.connectfourgame.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Setup grid size options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.grid_size_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.gridSizeSpinner.setAdapter(adapter);

        // Load existing preferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("ConnectFourPrefs", Context.MODE_PRIVATE);
        int savedGridSize = sharedPref.getInt("GridSize", 7);  // Default to 7x6 grid
        String player1Color = sharedPref.getString("Player1Color", "#FF0000");  // Default red
        String player2Color = sharedPref.getString("Player2Color", "#FFFF00");  // Default yellow

        // Set selected grid size
        binding.gridSizeSpinner.setSelection(adapter.getPosition(savedGridSize == 6 ? "6x5" : savedGridSize == 8 ? "8x7" : "7x6"));

        binding.saveSettingsButton.setOnClickListener(v -> {
            // Save the chosen settings
            int gridSize = Integer.parseInt(binding.gridSizeSpinner.getSelectedItem().toString().split("x")[0]);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("GridSize", gridSize);
            editor.putString("Player1Color", binding.player1ColorEditText.getText().toString());
            editor.putString("Player2Color", binding.player2ColorEditText.getText().toString());
            editor.apply();

            Toast.makeText(getContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        });

        binding.backButton.setOnClickListener(v -> {
            // Navigate back to the previous fragment
            getFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
