package com.example.upanddowntheriver.Backend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.R;


public class PlayerStatAdapter extends RecyclerView.Adapter<PlayerStatAdapter.ViewHolder> {
    private final PlayerCollection players;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView goldText;
        private final TextView silverText;
        private final TextView bronzeText;
        private final TextView totalGamesText;

        public ViewHolder(View view) {
            super(view);

            nameText = view.findViewById(R.id.playerStatNameId);
            goldText = view.findViewById(R.id.playerStatGoldId);
            silverText = view.findViewById(R.id.playerStatSilverId);
            bronzeText = view.findViewById(R.id.playerStatBronzeId);
            totalGamesText = view.findViewById(R.id.playerStatTotalGames);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getGoldText() {
            return goldText;
        }

        public TextView getSilverText() {
            return silverText;
        }

        public TextView getBronzeText() {
            return bronzeText;
        }

        public TextView getTotalGamesText() {
            return totalGamesText;
        }
    }

    public PlayerStatAdapter(PlayerCollection players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_stat_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getNameText().setText(players.getPlayer(position).getName());
        viewHolder.getGoldText().setText(String.valueOf(players.getPlayer(position).getMedalCount(Constants.PLACE.FIRST)));
        viewHolder.getSilverText().setText(String.valueOf(players.getPlayer(position).getMedalCount(Constants.PLACE.SECOND)));
        viewHolder.getBronzeText().setText(String.valueOf(players.getPlayer(position).getMedalCount(Constants.PLACE.THIRD)));
        viewHolder.getTotalGamesText().setText(String.valueOf(players.getPlayer(position).getGamesPlayed()));
    }

    @Override
    public int getItemCount() {
        return this.players.size();
    }
}