package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.BiddingRecyclerAdapter;
import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;
import com.example.upanddowntheriver.Backend.Speak;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;

import java.util.ArrayList;

// TODO Fix borders in bidding layout

public class Bidding extends AppCompatActivity {
    Game game;
    TextView playerText;
    TextView bidCountText;
    TextView bidText;
    TextView dealerBidText;
    Button nextButton;
    Button prevButton;
    Button plusButton;
    Button minusButton;
    RecyclerView bidsRecycler;
    BiddingRecyclerAdapter adapter;
    ArrayList<String> names;
    boolean doneBidding;
    int playerIndex;

    public void increaseBid(View view) {
        int curBid = Integer.parseInt(bidText.getText().toString());

        if (curBid < game.getHandSize()) {
            curBid++;
        }
        bidText.setText(String.valueOf(curBid));
        updateFinishButton(curBid);
    }

    public void decreaseBid(View view) {
        int curBid = Integer.parseInt(bidText.getText().toString());

        if (curBid > 0) {
            curBid--;
        }
        bidText.setText(String.valueOf(curBid));
        updateFinishButton(curBid);
    }

    public void updateFinishButton(int bid) {
        // If we are the dealer see if we need to enable/disable the finish button.
        // If we aren't the dealer, there's nothing to do.
        if (game.getPlayer(playerIndex).getDealer()) {
            if (bid + game.getRound().getBidTotal() == game.getHandSize()) {
                Utils.disableButton(nextButton, Color.GRAY);
            } else {
                Utils.enableButton(nextButton, getResources().getColor(R.color.teal_700, getTheme()));
            }
        }
    }

    public void nextBidder(View view) {
        // Before getting any bid info see if we are done bidding. If we are
        // it means that the bids are confirmed, move to the next activity.
        if (doneBidding) {
            Intent intent = Utils.createIntentWithData(this, AfterBidding.class, "game", game);
            finish();
            startActivity(intent);
            return;
        }

        int bid = Integer.parseInt(bidText.getText().toString());
        game.getRound().addBid(game.getPlayer(playerIndex).getName(), bid);

        names.add(game.getPlayer(playerIndex).getName());
        adapter.notifyDataSetChanged();
        bidsRecycler.scrollToPosition(names.size() - 1);

        // Check if the current player is the dealer. If it is we're done bidding.
        if (game.getPlayer(playerIndex).getDealer()) {
            doneBidding = true;
        } else {
            playerIndex++;
        }
        updateUI();
    }

    public void whatTrumpBe(View view) {
        // Say what trump is currently set to, unless it's not set
        // then remind the user it hasn't been set.
        Constants.SUIT curTrump = game.getCurTrump();
        String toSay = "";

        if (curTrump == Constants.SUIT.NONE) {
            toSay = "Set trump first";
        } else {
            toSay = "Trump is " + curTrump.name();
        }

        Speak.say(toSay);
    }

    public void previousBidder(View view) {
        if (doneBidding) {
            doneBidding = false;
        } else {
            playerIndex--;
        }

        names.remove(names.size()-1);
        adapter.notifyDataSetChanged();

        String playerName = game.getPlayer(playerIndex).getName();
        int bid = game.getRound().removeBid(playerName);

        updateUI();

        // Set the text box to the previous bid.
        bidText.setText(String.valueOf(bid));
    }

    @SuppressLint("DefaultLocale")
    private void updateUI() {
        GamePlayer curPlayer = game.getPlayer(playerIndex);

        // Set the player name
        playerText.setText(curPlayer.getName());

        // Set the bid count
        bidText.setText("0");
        bidCountText.setText(String.valueOf(game.getRound().getBidTotal()));

        // Update the buttons.
        if (playerIndex != 0) {
            Utils.enableButton(prevButton, getResources().getColor(R.color.teal_700, getTheme()));
        } else {
            Utils.disableButton(prevButton, Color.GRAY);
        }

        if (doneBidding) {
            nextButton.setText(R.string.finish_text);
            plusButton.setEnabled(false);
            minusButton.setEnabled(false);
        } else if (game.getPlayer(playerIndex).getDealer()) {
            plusButton.setEnabled(true);
            minusButton.setEnabled(true);
            nextButton.setText(R.string.next_button_text);
            int bidDiff = game.getHandSize() - game.getRoundBids();
            if (bidDiff >= 0) {
                dealerBidText.setText(String.format(curPlayer.getName() + " can't bid %d", bidDiff));
            } else {
                dealerBidText.setText(getString(R.string.dealer_can_bid_anything, curPlayer.getName()));
            }

            if (bidDiff == 0) {
                Utils.disableButton(nextButton, Color.GRAY);
            }
        } else {
            nextButton.setText(R.string.next_button_text);
            dealerBidText.setText("");
            Utils.enableButton(nextButton, getResources().getColor(R.color.teal_700, getTheme()));
        }

        if (!doneBidding) {
            Speak.say(game.getPlayer(playerIndex).getNickName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        game = (Game) getIntent().getExtras().getSerializable("game");
        playerIndex = 0;

        playerText = findViewById(R.id.biddingPlayerTextId);
        bidCountText = findViewById(R.id.biddingBidCountTextId);
        bidText = findViewById(R.id.biddingCurrentBidTextId);
        dealerBidText = findViewById(R.id.biddingDealerInfoTextId);
        nextButton = findViewById(R.id.biddingNextButtonId);
        prevButton = findViewById(R.id.biddingBackButtonId);
        plusButton = findViewById(R.id.biddingPlusButtonId);
        minusButton = findViewById(R.id.biddingMinusButtonId);

        Utils.disableButton(prevButton, Color.GRAY);

        playerText.setText(game.getPlayer(playerIndex).getName());
        bidCountText.setText("0");
        bidText.setText("0");
        dealerBidText.setText("");

        names = new ArrayList<>();

        bidsRecycler = findViewById(R.id.biddingRecyclerId);
        bidsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BiddingRecyclerAdapter(names, game.getRound());
        bidsRecycler.setAdapter(adapter);
        bidsRecycler.addItemDecoration(new SimpleDividerItemDecoration(bidsRecycler.getContext()));

        updateUI();
    }
}