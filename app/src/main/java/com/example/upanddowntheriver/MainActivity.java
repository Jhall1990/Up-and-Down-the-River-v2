package com.example.upanddowntheriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.upanddowntheriver.Activities.AddPlayer;
import com.example.upanddowntheriver.Activities.SelectPlayers;
import com.example.upanddowntheriver.Activities.PreviousGames;
import com.example.upanddowntheriver.Activities.Stats;
import com.example.upanddowntheriver.Backend.Speak;
import com.example.upanddowntheriver.Backend.Utils;


public class MainActivity extends AppCompatActivity {

    public void newGame(View view) {
        Log.d("main","new game button pressed");

        // Load the select player activity
        Intent intent = new Intent(this, SelectPlayers.class);
        startActivity(intent);
    }

    public void previousGames(View view) {
        Log.d("main", "load game button pressed");

        // Load the previous game activity
        Intent intent = new Intent(this, PreviousGames.class);
        startActivity(intent);
    }

    public void players(View view) {
        Log.d("main", "players button pressed");

        // Load the new player activity.
        Intent intent = new Intent(this, AddPlayer.class);
        startActivity(intent);
    }

    public void stats(View view) {
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Speak speak = Speak.getInstance();
        speak.InitTTS(getApplicationContext());
    }
}