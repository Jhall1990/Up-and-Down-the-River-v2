package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.Speak;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;

import java.util.ArrayList;


public class Scoreboard extends AppCompatActivity {
    Game game;

    public void details(View view) {
        Intent intent = Utils.createIntentWithData(this, DetailedScores.class, "game", game);
        startActivity(intent);
    }

    private void displayScores(ArrayList<GamePlayer> players) {
        LinearLayout scoreLayout = findViewById(R.id.scoreLinearLayoutId);
        TableLayout table = new TableLayout(this);
        addScoreHeader(table);

        for (GamePlayer p: players) {
            createRow(p, table);
        }

        scoreLayout.addView(table);
    }

    private void addScoreHeader(TableLayout table) {
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(params);

        row.addView(createText("Player",6, 16, false, 64, false));
        row.addView(createText("Streak",1, 16, true, 0, true));
        row.addView(createText("Bids Made",1, 16, true, 0, true));
        row.addView(createText("Score",1, 16, true, 0, false));

        table.addView(row);

        View view = new View(this);
        view.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 3));
        view.setBackgroundColor(Color.BLACK);
        table.addView(view);
    }

    private void createRow(GamePlayer player, TableLayout table) {
        TableRow row = new TableRow(this);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );

        row.setLayoutParams(params);

        String name = player.getName();
        String streak = String.valueOf(game.getPlayerStreak(player));
        String bidsMade = String.valueOf(game.getPlayerBidsMade(player));
        String score = String.valueOf(game.getPlayerScore(player));

        row.addView(createText(name,6, 24, false, 32, false));
        row.addView(createText(streak,1, 24, true, 0, true));
        row.addView(createText(bidsMade,1, 24, true, 0, true));
        row.addView(createText(score,1, 24, true, 0, false));

        table.addView(row);

        View view = new View(this);
        view.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 2));
        view.setBackgroundColor(Color.BLACK);
        table.addView(view);
    }

    private TextView createText(String s, int weight, int textSize, boolean center, int margin, boolean border) {
        TextView text = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                weight
        );
        params.setMarginStart(margin);
        text.setLayoutParams(params);
        text.setPadding(0, 16, 0, 16);

        text.setText(s);
        text.setTextColor(Color.BLACK);
        text.setTextSize(textSize);

        if (border) {
            text.setBackgroundResource(R.drawable.border_right);
        }

        if (center) {
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        return text;
    }

    public void recapScores(View view) {
        ArrayList<GamePlayer> players = game.getPlayersInScoreOrder();
        Speak speak = Speak.getInstance();
        TextToSpeech tts = speak.getTTS();

        for (GamePlayer player: players) {
            Speak.say(player.getNickName() + " with " + player.getScore());

            try {
                Thread.sleep(250);
            }
            catch (InterruptedException e) {
                // stupid java, just let me not handle the exception
            }

            // Wait for tts to stop speaking
            while (tts.isSpeaking()) {
                // waiting for tts to stop speaking
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        // Get the game info.
        Bundle bundle = getIntent().getExtras();
        game = (Game) bundle.getSerializable("game");

        // Get the players in score order.
        ArrayList<GamePlayer> players = game.getPlayersInScoreOrder();

        // Add text views for each player and their score.
        displayScores(players);
    }
}