package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.PlayerCollection;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;

import java.util.ArrayList;

public class FinalizeSettings extends AppCompatActivity {
    Spinner trumpModeSpinner;
    Spinner numberOfDecksSpinner;
    Spinner maxCardsSpinner;
    Spinner upOrDownSpinner;
    Spinner dealerSpinner;
    Game game;

    private void populateSpinners() {
        populateTrumpModeSpinner();
        populateNumberOfDecksSpinner();
        populateMaxCardsSpinner();
        populateUpOrDownSpinner();
        populateDealerSpinner();
    }

    private void populateTrumpModeSpinner() {
        trumpModeSpinner = findViewById(R.id.trumpModeSpinnerId);
        ArrayList<String> trumpModes = new ArrayList<>();

        for (Constants.TRUMP_MODE mode: Constants.TRUMP_MODE.values()) {
            trumpModes.add(Constants.formatTrumpMode(mode));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, trumpModes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trumpModeSpinner.setAdapter(adapter);
    }

    private void populateNumberOfDecksSpinner() {
        numberOfDecksSpinner = findViewById(R.id.numberOfDecksSpinnerId);
        Integer[] numDecks = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, numDecks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberOfDecksSpinner.setAdapter(adapter);

        // Set up a listener to update the max cards when number of decks is changed/
        numberOfDecksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateMaxCardsSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do here...
            }
        });
    }

    private void populateMaxCardsSpinner() {
        maxCardsSpinner = findViewById(R.id.maxCardsSpinnerId);
        ArrayList<Integer> maxCards = new ArrayList<>();
        int maxHandSize = game.getMaxHandSize(getCurrentNumDecks());

        for (int i = 1; i <= maxHandSize; i++) {
            maxCards.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, maxCards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxCardsSpinner.setAdapter(adapter);
    }

    private void populateUpOrDownSpinner() {
        upOrDownSpinner = findViewById(R.id.upOrDownSpinnerId);
        ArrayList<String> upOrDown = new ArrayList<>();

        for (Constants.UP_AND_DOWN_MODE mode: Constants.UP_AND_DOWN_MODE.values()) {
            upOrDown.add(Constants.formatUpAndDownMode(mode));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, upOrDown);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upOrDownSpinner.setAdapter(adapter);
    }

    private void populateDealerSpinner() {
        dealerSpinner = findViewById(R.id.dealerSelectSpinnerId);
        ArrayList<String> dealers = new ArrayList<>();

        for (GamePlayer p: game.getPlayers()) {
            dealers.add(p.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, dealers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dealerSpinner.setAdapter(adapter);
    }

    private int getCurrentNumDecks() {
        numberOfDecksSpinner = findViewById(R.id.numberOfDecksSpinnerId);
        return Integer.parseInt(numberOfDecksSpinner.getSelectedItem().toString());
    }

    public void finalizeSettingsSubmit(View view) {
        // Get all the values from the spinners.
        Constants.TRUMP_MODE trumpMode = Constants.trumpModeFromString(trumpModeSpinner.getSelectedItem().toString());
        Constants.UP_AND_DOWN_MODE upOrDown = Constants.upAndDownModeFromString(upOrDownSpinner.getSelectedItem().toString());
        int maxCards = Integer.parseInt(maxCardsSpinner.getSelectedItem().toString());
        String dealerName = dealerSpinner.getSelectedItem().toString();

        GamePlayer dealer = game.getPlayer(dealerName);
        game.finalizeSettings(trumpMode, upOrDown, maxCards, dealer);

        Intent intent = Utils.createIntentWithData(this, GameStart.class, "game",  game);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_settings);

        // Create a new game from the provided players.
        PlayerCollection players = (PlayerCollection) getIntent().getExtras().getSerializable("players");
        game = new Game(players);

        populateSpinners();
    }
}