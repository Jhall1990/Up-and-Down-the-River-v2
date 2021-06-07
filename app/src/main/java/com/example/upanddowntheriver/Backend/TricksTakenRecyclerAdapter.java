package com.example.upanddowntheriver.Backend;

import android.graphics.Color;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.R;


public class TricksTakenRecyclerAdapter extends RecyclerView.Adapter<TricksTakenRecyclerAdapter.ViewHolder> {
    private final Game game;
    private final Button button;
    int totalTricks;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final TextView scoreText;
        private final Button plusButton;
        private final Button minusButton;
        private int playerTricks;
        private final Game game;
        private final TricksTakenRecyclerAdapter adapter;

        public ViewHolder(View view, Game game, TricksTakenRecyclerAdapter adapter) {
            super(view);
            textView = view.findViewById(R.id.nameTextId);
            scoreText = view.findViewById(R.id.editTextNumber);
            plusButton = view.findViewById(R.id.tricksTakenPlusButtonId);
            minusButton = view.findViewById(R.id.tricksTakenMinusButtonId);
            playerTricks = 0;
            this.adapter = adapter;
            this.game = game;

            plusButton.setOnClickListener(this);
            minusButton.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getScoreText() {
            return scoreText;
        }

        public String getPlayerTricks() {
            return String.valueOf(playerTricks);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == minusButton.getId()) {
                playerTricks--;
                adapter.totalTricks--;
                if (playerTricks < 0) {
                    playerTricks++;
                    adapter.totalTricks++;
                }
            } else if (v.getId() == plusButton.getId()) {
                playerTricks++;
                adapter.totalTricks++;
                if (playerTricks > game.getHandSize() || adapter.totalTricks > game.getHandSize()) {
                    playerTricks--;
                    adapter.totalTricks--;
                }
            }

            // See if the submit button should be enabled/disabled.
            if (adapter.totalTricks == game.getHandSize()) {
                Utils.enableButton(adapter.getButton(), v.getResources().getColor(R.color.teal_700));
            } else {
                Utils.disableButton(adapter.getButton(), Color.GRAY);
            }

            game.getPlayer(getAdapterPosition()).setTricksTaken(playerTricks);
            scoreText.setText(String.valueOf(playerTricks));
        }
    }

    public TricksTakenRecyclerAdapter(Game game, Button button) {
        this.game = game;
        this.totalTricks = 0;
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tricks_taken_row, viewGroup, false);
        return new ViewHolder(view, game, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(this.game.getPlayers().get(position).getName());
        viewHolder.getScoreText().setText(viewHolder.getPlayerTricks());
    }

    @Override
    public int getItemCount() {
        return this.game.getPlayers().size();
    }
}