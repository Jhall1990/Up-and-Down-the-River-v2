package com.example.upanddowntheriver.Backend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.R;

import java.util.ArrayList;

public class PreviousGamesRecyclerAdapter extends RecyclerView.Adapter<PreviousGamesRecyclerAdapter.ViewHolder> {
    private final ArrayList<Game> games;
    private final OnClickListener listener;

    public interface OnClickListener {
        void onItemClick(Game game);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        PreviousGamesRecyclerAdapter adapter;

        public ViewHolder(View view, PreviousGamesRecyclerAdapter adapter) {
            super(view);
            textView = view.findViewById(R.id.nameTextId);
            this.adapter = adapter;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public PreviousGamesRecyclerAdapter(ArrayList<Game> games, OnClickListener listener) {
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_player_row, viewGroup, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Game game = games.get(position);
        viewHolder.getTextView().setOnClickListener(v -> listener.onItemClick(game));
        viewHolder.getTextView().setText(game.getGameName());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}