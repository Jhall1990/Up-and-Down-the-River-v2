package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;


public class AfterBidding extends AppCompatActivity {
    Game game;
    TextView dealerText;
    ImageView trumpImage;

    public void bids(View view) {
        Intent intent = Utils.createIntentWithData(this, Bids.class, "game", game);
        startActivity(intent);
    }

    public void scores(View view) {
        Intent intent = Utils.createIntentWithData(this, Scoreboard.class, "game", game);
        startActivity(intent);
    }

    public void enterTricks(View view) {
        Intent intent = Utils.createIntentWithData(this, EnterTricks.class, "game", game);
        startActivity(intent);
    }

    // Do nothing on back press at this point. It will just exit
    // the app and that's annoying.
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_bidding);

        // Get the game info from the intent
        game = (Game) getIntent().getExtras().getSerializable("game");

        // Get and set the dealer text.
        dealerText = findViewById(R.id.afterBidDealerTextId);
        dealerText.setText(game.getCurDealer().getName());

        // Set the trump image.
        trumpImage = findViewById(R.id.afterBidTrumpImageId);
        trumpImage.setImageResource(Constants.getTrumpImage(game.getCurTrump()));
    }
}