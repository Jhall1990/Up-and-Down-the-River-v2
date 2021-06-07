package com.example.upanddowntheriver.Backend;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Player implements java.io.Serializable {
    private String name;
    private String nickName;
    private final HashMap<Constants.PLACE, Integer> medals;
    private int gamesPlayed;

    /*
    Player constructors
     */
    public Player(String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
        this.medals = new HashMap<>();
        this.medals.put(Constants.PLACE.FIRST, 0);
        this.medals.put(Constants.PLACE.SECOND, 0);
        this.medals.put(Constants.PLACE.THIRD, 0);
    }

    public Player(JSONObject player) {
        medals = new HashMap<>();
        this.medals.put(Constants.PLACE.FIRST, 0);
        this.medals.put(Constants.PLACE.SECOND, 0);
        this.medals.put(Constants.PLACE.THIRD, 0);

        try {
            name = player.getString("name");
            nickName = player.getString("nickName");
            gamesPlayed = player.getInt("gamesPlayed");
            medals.put(Constants.PLACE.FIRST, player.getInt(Constants.PLACE.FIRST.toString()));
            medals.put(Constants.PLACE.SECOND, player.getInt(Constants.PLACE.SECOND.toString()));
            medals.put(Constants.PLACE.THIRD, player.getInt(Constants.PLACE.THIRD.toString()));
        } catch (JSONException e) {
            Log.i("jsonError", "Could not create player from json");
        }
    }

    /*
    Getter/setter methods because fucking java
     */
    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /*
    toString so the ListView works correctly.
     */
    @NonNull
    public String toString() {
        return name;
    }

    /*
    Convert a player object into json.
     */
    public JSONObject writeJson() {
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("name", name);
            jsonData.put("nickName", nickName);
            jsonData.put(Constants.PLACE.FIRST.toString(), medals.get(Constants.PLACE.FIRST));
            jsonData.put(Constants.PLACE.SECOND.toString(), medals.get(Constants.PLACE.SECOND));
            jsonData.put(Constants.PLACE.THIRD.toString(), medals.get(Constants.PLACE.THIRD));
            jsonData.put("gamesPlayed", gamesPlayed);
        } catch (JSONException e) {
            Log.i("jsonError", "Could not convert to json.");
        }

        return jsonData;
    }

    public void addGamePlayed() {
        gamesPlayed++;
    }

    public void updateMedals(Constants.PLACE place) {
        int count = medals.get(place);
        medals.put(place, count + 1);
    }

    public Integer getMedalCount(Constants.PLACE place) {
        return medals.get(place);
    }

    public void resetStats() {
        gamesPlayed = 0;
        medals.put(Constants.PLACE.FIRST, 0);
        medals.put(Constants.PLACE.SECOND, 0);
        medals.put(Constants.PLACE.THIRD, 0);
    }
}
