package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;
import com.example.upanddowntheriver.Backend.TricksTakenRecyclerAdapter;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;


public class EnterTricks extends AppCompatActivity {
    Game game;
    Button submitButton;
    TricksTakenRecyclerAdapter tricksAdapter;

    public void finishTricksTaken(View view) {
        // Set the number of tricks taken for each player
        for (GamePlayer p: game.getPlayers()) {
            game.getRound().addTaken(p.getName(), p.getTricksTaken());
            p.setTricksTaken(0);
        }

        // Finish the current round.
        game.finishRound();

        Intent intent;
        // See if the game is over if it is, go to the scoreboard.
        // Otherwise start the next round.
        if (game.isOver()) {
            intent = Utils.createIntentWithData(this, GameEnd.class, "game", game);
        } else {
            intent = Utils.createIntentWithData(this, GameStart.class, "game", game);
        }

        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_tricks);

        game = (Game) getIntent().getExtras().getSerializable("game");
        submitButton = findViewById(R.id.finishTricksTakenButtonId);
        Utils.disableButton(submitButton, Color.GRAY);

        // Create the recycler view and add an adapter so it shows the stuff in the player
        // list
        RecyclerView tricksView = findViewById(R.id.tricksTakenRecyclerId);
        tricksView.setLayoutManager(new LinearLayoutManager(this));
        tricksAdapter = new TricksTakenRecyclerAdapter(game, submitButton);
        tricksView.setAdapter(tricksAdapter);

        // Add a line between each item in the recycler
        SimpleDividerItemDecoration divider = new SimpleDividerItemDecoration(tricksView.getContext());
        tricksView.addItemDecoration(divider);
    }
}