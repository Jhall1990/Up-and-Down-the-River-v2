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


public class SelectedPlayerRecyclerAdapter extends RecyclerView.Adapter<SelectedPlayerRecyclerAdapter.ViewHolder> {
    private final Button button;
    private final PlayerCollection available;
    private final PlayerCollection selected;
    private SelectPlayerRecyclerAdapter availableAdapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view, SelectedPlayerRecyclerAdapter adapter) {

            super(view);
            this.textView = view.findViewById(R.id.nameTextId);

            // Create an onclick listener for select player recycler.
            View.OnClickListener clickListener = v -> {
                Player p = adapter.getSelected().removePlayer(getAdapterPosition());
                adapter.getAvailable().addPlayer(p);
                adapter.notifyDataSetChanged();
                adapter.getAvailableAdapter().notifyDataSetChanged();

                if (adapter.getSelected().size() < 2) {
                    Utils.disableButton(adapter.getButton(), Color.GRAY);
                } else {
                    Utils.enableButton(adapter.getButton(), view.getResources().getColor(R.color.teal_700));
                }
            };

            view.setOnClickListener(clickListener);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public SelectedPlayerRecyclerAdapter(PlayerCollection available, PlayerCollection selected, Button button) {
        this.available = available;
        this.selected = selected;
        this.button = button;
    }

    public void setAvailableAdapter(SelectPlayerRecyclerAdapter s) {
        this.availableAdapter = s;
    }

    public SelectPlayerRecyclerAdapter getAvailableAdapter() {
        return availableAdapter;
    }

    public PlayerCollection getAvailable() {
        return available;
    }

    public PlayerCollection getSelected() {
        return selected;
    }

    public Button getButton() {
        return button;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_player_row, viewGroup, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Player player = this.selected.getPlayer(position);
        viewHolder.getTextView().setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return this.selected.size();
    }
}