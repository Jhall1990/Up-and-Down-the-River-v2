package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.PlayerCollection;
import com.example.upanddowntheriver.Backend.PlayerStatAdapter;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;
import com.example.upanddowntheriver.R;

import java.util.Collections;


public class Stats extends AppCompatActivity {
    private PlayerCollection players;
    private PlayerStatAdapter adapter;

    public void clearMedals(View view) {
        players.resetStats(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medals);

        players = new PlayerCollection(this, Constants.PLAYERS_FILE);
        Collections.sort(players.getPlayers(), (x, y) -> {
            int val = y.getMedalCount(Constants.PLACE.FIRST).compareTo(x.getMedalCount(Constants.PLACE.FIRST));
            if (val == 0)
                val = y.getMedalCount(Constants.PLACE.SECOND).compareTo(x.getMedalCount(Constants.PLACE.SECOND));
            if (val == 0)
                val = y.getMedalCount(Constants.PLACE.THIRD).compareTo(x.getMedalCount(Constants.PLACE.THIRD));
            if (val == 0)
                val = x.getName().compareTo(y.getName());
            return val;
        });

        RecyclerView playerStatRecycler = findViewById(R.id.playerStatsRecyclerId);
        adapter = new PlayerStatAdapter(players);
        playerStatRecycler.setLayoutManager(new LinearLayoutManager(this));
        playerStatRecycler.setAdapter(adapter);
        SimpleDividerItemDecoration div = new SimpleDividerItemDecoration(playerStatRecycler.getContext());
        playerStatRecycler.addItemDecoration(div);
    }
}