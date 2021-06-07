package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.PlayerCollection;
import com.example.upanddowntheriver.Backend.PlayerRecyclerAdapter;
import com.example.upanddowntheriver.R;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;

public class AddPlayer extends AppCompatActivity {
    private PlayerCollection players;
    private PlayerRecyclerAdapter playerAdapter;

    public void openAddPlayer(View view) {
        Log.i("main", "Add player button clicked");

        Intent intent = new Intent(this, AddEditPlayers.class);
        Bundle bundle = new Bundle();
        intent.putExtra("requestCode", Constants.ADD_PLAYER);
        bundle.putSerializable("players", players);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.ADD_PLAYER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_PLAYER) {
                players.addPlayer(data);
            } else if (requestCode == Constants.EDIT_PLAYER) {
                players.editPlayer(data);
            }
            playerAdapter.notifyDataSetChanged();
            players.savePlayers(this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        // Get existing players from the players file
        players = new PlayerCollection(this, Constants.PLAYERS_FILE);

        // Create the recycler view and add an adapter so it shows the stuff in the player
        // list
        RecyclerView playerView = findViewById(R.id.player_recycler_view_id);
        playerView.setLayoutManager(new LinearLayoutManager(this));
        playerAdapter = new PlayerRecyclerAdapter(this, players);
        playerView.setAdapter(playerAdapter);

        // Register the recycler view for a long press menu
        registerForContextMenu(playerView);

        // Add a line between each item in the recycler
        SimpleDividerItemDecoration divider = new SimpleDividerItemDecoration(playerView.getContext());
        playerView.addItemDecoration(divider);
    }
}