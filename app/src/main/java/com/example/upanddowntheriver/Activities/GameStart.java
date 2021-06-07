package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;


public class GameStart extends AppCompatActivity {
    Game game;
    Button startBiddingButton;
    ImageView trumpImage;

    public void showScores(View view) {
        Intent intent = Utils.createIntentWithData(this, Scoreboard.class, "game", game);
        startActivity(intent);
    }

    public void startBidding(View view) {
        Intent intent = Utils.createIntentWithData(this, Bidding.class, "game", game);
        startActivity(intent);
    }

    private void setTrumpClickListener() {
        Context ctx = this;

        // Create the pop up menu and it's listener
        PopupMenu menu = new PopupMenu(ctx, trumpImage);
        for (Constants.SUIT suit: Constants.SUIT.values()) {
            menu.getMenu().add(suit.toString());
        }

        menu.setOnMenuItemClickListener(item -> {
            Constants.SUIT dealersChoiceSuit = Constants.SUIT.valueOf(item.toString());
            trumpImage.setImageResource(Constants.getTrumpImage(dealersChoiceSuit));
            game.setCurTrump(dealersChoiceSuit);
            startBiddingButton.setEnabled(true);
            return true;
        });

        // Show the menu when the trump icon is pressed.
        View.OnClickListener listener = v -> {
            menu.show();
        };

        trumpImage.setOnClickListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        // Get the game info from the bundle.
        Bundle bundle = getIntent().getExtras();
        game = (Game) bundle.getSerializable("game");

        // Update the text in all the text views.
        TextView numCardText = findViewById(R.id.numberOfCardsTextId);
        TextView dealerText = findViewById(R.id.dealerTextId);
        TextView roundText = findViewById(R.id.roundTextId);
        TextView upOrDownText = findViewById(R.id.upOrDownTextId);

        numCardText.setText(String.valueOf(game.getHandSize()));
        dealerText.setText(game.getCurDealer().getName());

        String round = String.format("%d of %d", game.getCurRound(), game.getNumRounds());
        roundText.setText(round);

        String upOrDown = "Going Down";
        if (game.getGoingUp()) {
            upOrDown = "Going Up";
        }
        upOrDownText.setText(upOrDown);

        // Set the trump image.
        trumpImage = findViewById(R.id.gameStartTrumpImageId);
        trumpImage.setImageResource(Constants.getTrumpImage(game.getCurTrump()));

        // Check if we are in dealer choice trump mode. If we are disable the start bidding
        // button until a trump suit is chosen. Also update the trump text header to so it
        // informs the user they can click the trump icon to set it.
        startBiddingButton = findViewById(R.id.startBiddingButtonId);
        if (game.getTrumpMode() == Constants.TRUMP_MODE.DEALER_CHOICE) {
            startBiddingButton.setEnabled(false);
            TextView trumpHeaderText = findViewById(R.id.trumpHeaderTextId);
            trumpHeaderText.setText("Trump (click to set)");
            setTrumpClickListener();
        }
    }
}