package com.example.connectfourgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.connectfourgame.databinding.ItemGameCellBinding;

public class GameGridAdapter extends RecyclerView.Adapter<GameGridAdapter.ViewHolder> {

    private Game game;  // Removed final keyword
    private final OnCellClickListener onCellClickListener;

    public interface OnCellClickListener {
        void onCellClick(int column);
    }

    public GameGridAdapter(Game game, OnCellClickListener onCellClickListener) {
        this.game = game;
        this.onCellClickListener = onCellClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGameCellBinding binding = ItemGameCellBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, onCellClickListener, game);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (game == null) {
            return;
        }

        int row = position / game.getNumCols();
        int col = position % game.getNumCols();
        char cellValue = game.getBoard()[row][col];

        // Update cell appearance based on game state
        if (cellValue == 'X') {
            holder.binding.cellView.setBackgroundColor(0xFFFF0000); // Red for player 1
        } else if (cellValue == 'O') {
            holder.binding.cellView.setBackgroundColor(0xFFFFFF00); // Yellow for player 2
        } else {
            holder.binding.cellView.setBackgroundColor(0xFFFFFFFF); // Empty cell
        }
    }

    @Override
    public int getItemCount() {
        return game.getNumRows() * game.getNumCols();
    }

    public void updateGame(Game newGame) {
        this.game = newGame;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGameCellBinding binding;

        public ViewHolder(@NonNull ItemGameCellBinding binding, OnCellClickListener listener, Game game) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int col = position % game.getNumCols();
                    listener.onCellClick(col);
                }
            });
        }
    }
}
