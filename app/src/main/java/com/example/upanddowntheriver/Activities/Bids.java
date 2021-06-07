package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.upanddowntheriver.Backend.BiddingRecyclerAdapter;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.R;

import java.util.ArrayList;

public class Bids extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids);

        Game game = (Game) getIntent().getExtras().getSerializable("game");

        ArrayList<GamePlayer> players = game.getPlayers();
        ArrayList<String> playerNames = new ArrayList<>();

        for (GamePlayer p: players) {
            playerNames.add(p.getName());
        }

        RecyclerView bidsRecycler = findViewById(R.id.bidsRecyclerId);
        bidsRecycler.setLayoutManager((new LinearLayoutManager(this)));
        BiddingRecyclerAdapter adapter = new BiddingRecyclerAdapter(playerNames, game.getRound());
        bidsRecycler.setAdapter(adapter);
    }
}