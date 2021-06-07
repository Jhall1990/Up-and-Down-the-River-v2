package com.example.upanddowntheriver.Backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class PlayerCollection implements java.io.Serializable {
    private final ArrayList<Player> players;

    public PlayerCollection() {
        this.players = new ArrayList<>();
    }

    public PlayerCollection(Context c, String jsonFile) {
        this.players = new ArrayList<>();
        readFromJsonFile(c, jsonFile);
    }

    public void addPlayer(Player p) {
        addPlayer(p, true);
    }

    public void addPlayer(Player p, boolean sort) {
        players.add(p);

        if (sort)
            sortPlayersByName();
    }

    public Player removePlayer(int index) {
        return players.remove(index);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public Player getPlayer(String name) {
        for (Player p: players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public int size() {
        return players.size();
    }

    /*
    Convert player collection to json.
     */
    public void savePlayers(Context c) {
        JSONObject playersJson = createJson();
        writeJsonToFile(c, playersJson);
    }

    /*
    Create JSON from the players.
     */
    private JSONObject createJson() {
        JSONObject playersJson = new JSONObject();
        JSONArray playersJsonArray = new JSONArray();

        for (Player p : players) {
            playersJsonArray.put(p.writeJson());
        }

        try {
            playersJson.put("players", playersJsonArray);
        } catch (JSONException e) {
            Log.i("jsonError", "Could not create players json.");
        }

        return playersJson;
    }

    /*
    Writes the JSON to the players file.
     */
    private void writeJsonToFile(Context c, JSONObject playersJson) {
        try {
            FileOutputStream playerFile = c.openFileOutput(Constants.PLAYERS_FILE, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(playerFile);
            osw.write(playersJson.toString());
            osw.close();
        } catch (FileNotFoundException e) {
            Log.i("fileNotFound", "Could not open players file.");
        } catch (IOException e) {
            Log.i("ioError", "Couldn't write to the players file.");
        }
    }

    /*
    Populates the players array with player objects from the json file.
     */
    private void readFromJsonFile(Context c, String jsonFile) {
        try {
            // All this to read the players json file.
            FileInputStream playersFile = c.openFileInput(jsonFile);
            InputStreamReader isr = new InputStreamReader(playersFile);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder playerJson = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                playerJson.append(line);
            }

            // Create a json object from the text read out of the players file.
            JSONObject playerData = new JSONObject(playerJson.toString());

            // Create a JSON array object from the "players" object in the players JSON.
            JSONArray playerArray = (JSONArray) playerData.get("players");

            // Iterate over each entry in the playerArray, create a player
            // and add it to the player collection.
            for (int i = 0; i < playerArray.length(); i++) {
                Player p = new Player(playerArray.getJSONObject(i));
                addPlayer(p);
            }
        } catch (JSONException e) {
            Log.i("jsonError", "Couldn't read players json");
        } catch (FileNotFoundException e) {
            Log.i("fileNotFound", "Could not find players file");
        } catch (IOException e) {
            Log.i("ioError", "Could not read players file");
        }
    }

    public void addPlayer(Intent intent) {
        String name = intent.getStringExtra("name");
        String nickName = intent.getStringExtra("nickName");
        Player p = new Player(name, nickName);
        addPlayer(p);
    }

    public void editPlayer(Intent intent) {
        int index = intent.getIntExtra("index", -1);
        String name = intent.getStringExtra("name");
        String nickName = intent.getStringExtra("nickName");

        Player p = getPlayer(index);
        p.setName(name);
        sortPlayersByName();
        p.setNickName(nickName);
    }

    public boolean playerUnique(Player p) {
        for (Player player: players) {
            if (p.getName().toLowerCase().equals(player.getName().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public void resetStats(Context ctx) {
        for (Player p: players) {
            p.resetStats();
        }
        savePlayers(ctx);
    }

    public void sortPlayersByName() {
        Collections.sort(players, (x, y) -> x.getName().compareTo(y.getName()));
    }
}