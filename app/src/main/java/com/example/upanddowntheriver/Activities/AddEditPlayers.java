package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Player;
import com.example.upanddowntheriver.Backend.PlayerCollection;
import com.example.upanddowntheriver.R;

// TODO Figure out if nick name should be re-enabled.

public class AddEditPlayers extends AppCompatActivity {
    PlayerCollection players;
    String intentName;

    public void savePlayer(View view) {
        // Get the strings from the two text fields.
        EditText nameText = findViewById(R.id.playerNameTextId);
        EditText nickNameText = findViewById(R.id.playerNickNameTextId);
        String name = nameText.getText().toString();
        String nickName = nickNameText.getText().toString();
        Player p = new Player(name, nickName);

        // Before giving the okay, make sure at least a name was entered.
        if (TextUtils.isEmpty(nameText.getText())) {
            nameText.setError("Name is required");
        } else if (!name.equals(intentName) && !players.playerUnique(p)) {
            nameText.setError("Name must be unique");
        } else {
            // Create an intent and add name and nick name extras
            Intent intent = new Intent();
            intent.putExtra("name", nameText.getText().toString());
            intent.putExtra("nickName", nickNameText.getText().toString());

            // Set the intent index based on the index passed in the intent.
            // This is only applicable when editing a player.
            Intent editIntent = getIntent();
            int index = editIntent.getIntExtra("index", -1);
            intent.putExtra("index", index);

            // Set the activity result and return to the previous activity.
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_players);

        // Get the request code to see if we are doing an edit
        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("requestCode", 1);
        players = (PlayerCollection) intent.getExtras().getSerializable("players");

        // For now disable the nick name stuff.
        TextView nickNameHeader = findViewById(R.id.playerNickNameHeaderTextId);
        EditText nickNameText = findViewById(R.id.playerNickNameTextId);
        nickNameHeader.setVisibility(View.INVISIBLE);
        nickNameText.setVisibility(View.INVISIBLE);

        EditText nameText = findViewById(R.id.playerNameTextId);
        nameText.requestFocus();
        nameText.setSelection(0);

        if (requestCode == Constants.EDIT_PLAYER) {
            intentName = intent.getStringExtra("name");
            String nickName = intent.getStringExtra("nickName");

            nameText.setText(intentName);
            nickNameText.setText(nickName);
            nameText.setSelection(0, intentName.length());
        }
    }
}