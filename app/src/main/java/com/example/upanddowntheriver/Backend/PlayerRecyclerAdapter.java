package com.example.upanddowntheriver.Backend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upanddowntheriver.Activities.AddEditPlayers;
import com.example.upanddowntheriver.R;


public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.ViewHolder> {
    private final Activity ctx;
    private final PlayerCollection players;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView textView;
        private final PlayerCollection players;
        private final Activity ctx;
        private final PlayerRecyclerAdapter adapter;

        public ViewHolder(View view, PlayerCollection players, Activity ctx, PlayerRecyclerAdapter adapter) {

            super(view);
            this.textView = view.findViewById(R.id.nameTextId);
            this.players = players;
            this.ctx = ctx;
            this.adapter = adapter;
            view.setOnCreateContextMenuListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int index = getAdapterPosition();

                if (item.getItemId() == 1) {
                    Log.i("main", "Edit menu clicked");

                    Intent intent = new Intent(ctx, AddEditPlayers.class);
                    Bundle bundle = new Bundle();
                    Player player = players.getPlayer(index);
                    intent.putExtra("name", player.getName());
                    intent.putExtra("nickName", player.getNickName());
                    intent.putExtra("requestCode", Constants.EDIT_PLAYER);
                    intent.putExtra("index", index);
                    bundle.putSerializable("players", players);
                    intent.putExtras(bundle);
                    ctx.startActivityForResult(intent, Constants.EDIT_PLAYER);
                } else if (item.getItemId() == 2) {
                    Log.i("main", "Delete menu clicked");

                    players.removePlayer(index);
                    players.savePlayers(ctx);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        };
    }

    public PlayerRecyclerAdapter(Activity ctx, PlayerCollection players) {
        this.ctx = ctx;
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_player_row, viewGroup, false);
        return new ViewHolder(view, this.players, this.ctx, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(this.players.getPlayer(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.players.size();
    }
}