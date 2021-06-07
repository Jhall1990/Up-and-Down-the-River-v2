package com.example.upanddowntheriver.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.upanddowntheriver.Backend.Constants;
import com.example.upanddowntheriver.Backend.Game;
import com.example.upanddowntheriver.Backend.PreviousGamesRecyclerAdapter;
import com.example.upanddowntheriver.Backend.SimpleDividerItemDecoration;
import com.example.upanddowntheriver.Backend.Utils;
import com.example.upanddowntheriver.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;


public class PreviousGames extends AppCompatActivity {
    private RecyclerView previousGames;
    private PreviousGamesRecyclerAdapter previousGamesAdapter;
    private ArrayList<Game> games;

    public void clearGames(View view) {
        File[] files = new File(Constants.previousGamePath(this)).listFiles();

        if (files == null) {
            return;
        }

        for (File f: files) {
            f.delete();
        }

        games.clear();
        previousGamesAdapter.notifyDataSetChanged();
    }

    public Game loadGame(String fileName) {
        try {
            FileInputStream gameFile = new FileInputStream(Constants.previousGamePath(this, fileName));
            ObjectInputStream ois = new ObjectInputStream(gameFile);
            return (Game) ois.readObject();
        } catch (FileNotFoundException e) {
            Log.i("fileNotFound", "Could not find game file");
        } catch (IOException e) {
            Log.i("ioError", "Could not read game file");
        } catch (ClassNotFoundException e ) {
            Log.i("classError", "Game file not formatted correctly");
        }

        return null;
    }

    private ArrayList<Game> getGameFiles() {
        ArrayList<Game> games = new ArrayList<>();
        File[] files = new File(Constants.previousGamePath(this)).listFiles();

        if (files == null) {
            return games;
        }

        for (File f: files) {
            Game game = loadGame(f.getName());
            if (game != null) {
                games.add(game);
            }
        }

        Collections.reverse(games);
        return games;
    }

    private PreviousGamesRecyclerAdapter.OnClickListener createClickListener() {
        Context ctx = this;
        return game -> {
            Intent intent = Utils.createIntentWithData(ctx, Scoreboard.class, "game", game);
            startActivity(intent);
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_games);

        games = getGameFiles();

        previousGames = findViewById(R.id.previousGamesRecyclerId);
        previousGames.setLayoutManager(new LinearLayoutManager(this));
        PreviousGamesRecyclerAdapter.OnClickListener listener = createClickListener();
        previousGamesAdapter = new PreviousGamesRecyclerAdapter(games, listener);
        previousGames.setAdapter(previousGamesAdapter);

        // Add a line between each item in the recycler.
        SimpleDividerItemDecoration divider = new SimpleDividerItemDecoration(previousGames.getContext());
        previousGames.addItemDecoration(divider);
    }
}