package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.GamePlayer;
import com.example.upanddowntheriver.Backend.Round;
import com.example.upanddowntheriver.R;


public class DetailedScores extends AppCompatActivity {
    Game game;
    TableLayout detailedScoreLayout;

    private int intToDp(int i) {
        Resources resources = this.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, resources.getDisplayMetrics());
    }

    private View createHorizontalDivider() {
        View div = new View(this);
        div.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, intToDp(1)));
        div.setBackgroundColor(Color.BLACK);
        return div;
    }

    private View createVerticalDivider() {
        View div = new View(this);
        div.setLayoutParams(new TableRow.LayoutParams(intToDp(1), TableRow.LayoutParams.MATCH_PARENT));
        div.setBackgroundColor(Color.BLACK);
        return div;
    }

    private TextView createName(String name) {
        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        nameText.setTextColor(Color.BLACK);
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameText.setPadding(intToDp(8), 0, intToDp(8), 0);
        return nameText;
    }

    private TextView createScore(String val) {
        TextView scoreText = new TextView(this);
        scoreText.setText(val);
        scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        scoreText.setTextColor(Color.BLACK);
        scoreText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scoreText.setPadding(intToDp(8), 0, intToDp(8), 0);
        scoreText.setMinEms(2);
        return scoreText;
    }

    private TableRow createNameRow() {
        TableRow row = new TableRow(this);

        // Add an empty view as a spacer
        View view = new View(this);
        view.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(view);
        row.addView(createVerticalDivider());

        for (GamePlayer player: game.getPlayersInScoreOrder()) {
            TextView text = createName(player.getName());
            row.addView(text);
            row.addView(createVerticalDivider());
        }

        return row;
    }

    private TableRow createScoreRow(int roundIdx) {
        Round round = game.getRound(roundIdx);
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Add a trump image
        ImageView trumpIcon = new ImageView(this);
        trumpIcon.setImageResource(Constants.getTrumpImage(round.getTrump()));
        trumpIcon.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT
        ));
        row.setGravity(Gravity.CENTER);
        row.addView(trumpIcon);
        row.addView(createVerticalDivider());

        for (GamePlayer player: game.getPlayersInScoreOrder()) {
            // Create a horizontal linear layout, this will house a vertical linear layout
            // and the score text view.
            LinearLayout inner = new LinearLayout(this);
            inner.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout bidAndTaken = new LinearLayout(this);
            bidAndTaken.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT, 1));
            bidAndTaken.setOrientation(LinearLayout.VERTICAL);
            inner.addView(bidAndTaken);

            // Add a vertical divider.
            View vert = new View(this);
            vert.setBackgroundColor(Color.BLACK);
            vert.setLayoutParams(new TableRow.LayoutParams(
                    intToDp(1),
                    TableRow.LayoutParams.MATCH_PARENT));
            inner.addView(vert);

            // Add the score text.
            TextView scoreText = createScore(String.valueOf(game.getScoreAtRound(player, roundIdx)));
            TableRow.LayoutParams scoreParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT, 1);
            scoreText.setGravity(Gravity.CENTER);
            scoreText.setLayoutParams(scoreParams);

            if (round.madeBid(player.getName())) {
                scoreText.setTypeface(Typeface.DEFAULT_BOLD);
            }
            inner.addView(scoreText);

            // Add the bid text
            TextView bidText = createScore(String.valueOf(round.getBid(player.getName())));
            LinearLayout.LayoutParams bidParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            bidText.setGravity(Gravity.CENTER);
            bidText.setLayoutParams(bidParams);
            bidAndTaken.addView(bidText);

            // Add a horizontal divider.
            View horz = new View(this);
            horz.setBackgroundColor(Color.BLACK);
            horz.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    intToDp(1)));
            bidAndTaken.addView(horz);

            // Add the taken text.
            TextView takenText = createScore(String.valueOf(round.getTaken(player.getName())));
            LinearLayout.LayoutParams takenParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            takenText.setGravity(Gravity.CENTER);
            takenText.setLayoutParams(takenParams);
            bidAndTaken.addView(takenText);

            row.addView(inner);
            row.addView(createVerticalDivider());
        }

        return row;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_scores);

        game = (Game) getIntent().getExtras().getSerializable("game");
        detailedScoreLayout = findViewById(R.id.detailedScoreLayoutId);

        detailedScoreLayout.addView(createNameRow());
        detailedScoreLayout.addView(createHorizontalDivider());

        for (int i = 0; i < game.getRounds().size(); i++) {
            Round round = game.getRound(i);
            if (!round.getFinished()) {
                break;
            }

            detailedScoreLayout.addView(createScoreRow(i));
            detailedScoreLayout.addView(createHorizontalDivider());
        }
    }
}