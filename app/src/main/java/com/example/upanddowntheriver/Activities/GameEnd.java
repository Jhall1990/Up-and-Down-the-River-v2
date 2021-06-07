package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.MainActivity;
import com.example.upanddowntheriver.R;

import java.util.ArrayList;

public class GameEnd extends AppCompatActivity {
    Game game;
    LinearLayout winnerList;

    public void scores(View view) {
        Intent intent = Utils.createIntentWithData(this, DetailedScores.class, "game", game);
        startActivity(intent);
    }

    public void newGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    private void createRow(GamePlayer player, Constants.PLACE place) {
        LinearLayout row = new LinearLayout(this);
        row.setBackground(getBackground(place));
        row.setOrientation(LinearLayout.VERTICAL);

        TextView winnerNameRow = new TextView(this);
        winnerNameRow.setText(player.getName());
        winnerNameRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        winnerNameRow.setTextColor(Color.BLACK);
        winnerNameRow.setPadding(32, 32, 32, 8);
        winnerNameRow.setGravity(Gravity.CENTER);

        TextView winnerScoreRow = new TextView(this);
        winnerScoreRow.setText(String.valueOf(player.getScore()));
        winnerScoreRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        winnerScoreRow.setTextColor(Color.BLACK);
        winnerScoreRow.setPadding(32, 8, 32, 32);
        winnerScoreRow.setGravity(Gravity.CENTER);

        row.addView(winnerNameRow);
        row.addView(winnerScoreRow);
        winnerList.addView(row);
    }

    private Drawable getBackground(Constants.PLACE place) {
        switch (place) {
            case FIRST:
                return AppCompatResources.getDrawable(this, R.drawable.gold_border);
            case SECOND:
                return AppCompatResources.getDrawable(this, R.drawable.silver_border);
            case THIRD:
                return AppCompatResources.getDrawable(this, R.drawable.bronze_border);
        }
        return AppCompatResources.getDrawable(this, R.drawable.black_border_winner);
    }

    private Constants.PLACE getPlace(GamePlayer player, int score, Constants.PLACE place) {
        if (score == -1) {
            return Constants.PLACE.FIRST;
        }

        if (player.getScore() == score) {
            return place;
        }

        switch (place) {
            case FIRST:
                return Constants.PLACE.SECOND;
            case SECOND:
                return Constants.PLACE.THIRD;
            default:
                return Constants.PLACE.OTHER;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        winnerList = findViewById(R.id.winnerLinearLayoutId);

        game = (Game) getIntent().getExtras().getSerializable("game");
        ArrayList<GamePlayer> players = game.getPlayersInScoreOrder();

        int curScore = -1;
        Constants.PLACE place = Constants.PLACE.FIRST;

        for (int i = 0; i < players.size(); i++) {
            GamePlayer p = players.get(i);
            place = getPlace(p, curScore, place);
            p.setPlace(place);
            curScore = p.getScore();
            createRow(p, place);
        }

        game.save(this);
        Utils.updatePlayerStats(this, game.getPlayers());
    }
}