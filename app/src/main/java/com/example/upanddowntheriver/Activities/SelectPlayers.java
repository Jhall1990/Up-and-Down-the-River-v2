package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.PlayerCollection;
import com.example.upanddowntheriver.Backend.SelectPlayerRecyclerAdapter;
import com.example.upanddowntheriver.Backend.SelectedPlayerRecyclerAdapter;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;


public class SelectPlayers extends AppCompatActivity {
    PlayerCollection availablePlayers;
    PlayerCollection selectedPlayers;

    public void submit(View view) {
        Intent intent = Utils.createIntentWithData(this, FinalizeSettings.class, "players", selectedPlayers);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);

        // Load all the available players
        availablePlayers = new PlayerCollection(this, Constants.PLAYERS_FILE);
        selectedPlayers = new PlayerCollection();

        // Disable the finish button until enough players are selected.
        Button submitButton = findViewById(R.id.finishPlayersButtonId);
        Utils.disableButton(submitButton, Color.GRAY);

        // Create the recycler view and add an adapter for the available players.
        RecyclerView availablePlayerView = findViewById(R.id.selectPlayerRecyclerId);
        availablePlayerView.setLayoutManager(new LinearLayoutManager(this));
        SelectPlayerRecyclerAdapter availableAdapter = new SelectPlayerRecyclerAdapter(availablePlayers, selectedPlayers, submitButton);

        // Do the same for the selected players.
        RecyclerView selectedPlayerView = findViewById(R.id.selectedPlayerRecyclerId);
        selectedPlayerView.setLayoutManager(new LinearLayoutManager(this));
        SelectedPlayerRecyclerAdapter selectedAdapter = new SelectedPlayerRecyclerAdapter(availablePlayers, selectedPlayers, submitButton);

        // Pass the adapters to one another.
        availableAdapter.setSelectedAdapter(selectedAdapter);
        selectedAdapter.setAvailableAdapter(availableAdapter);
        availablePlayerView.setAdapter(availableAdapter);
        selectedPlayerView.setAdapter(selectedAdapter);

        // Add a line between each item in the recyclers.
        SimpleDividerItemDecoration divider1 = new SimpleDividerItemDecoration(availablePlayerView.getContext());
        availablePlayerView.addItemDecoration(divider1);
        SimpleDividerItemDecoration divider2 = new SimpleDividerItemDecoration(selectedPlayerView.getContext());
        selectedPlayerView.addItemDecoration(divider2);
    }
}