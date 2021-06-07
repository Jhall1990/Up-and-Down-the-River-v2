package com.example.upanddowntheriver.Backend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;

public class Utils {
    public static void enableButton(Button button, int color) {
        button.setEnabled(true);
        button.setBackgroundColor(color);
    }

    public static void disableButton(Button button, int color) {
        button.setEnabled(false);
        button.setBackgroundColor(color);
    }

    public static Intent createIntentWithData(Context ctx, Class<?> cls, String key, Serializable val) {
        Intent intent = new Intent(ctx, cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, val);
        intent.putExtras(bundle);
        return intent;
    }

    public static void updatePlayerStats(Context ctx, ArrayList<GamePlayer> gamePlayers) {
        PlayerCollection playersInPlayersFile = new PlayerCollection(ctx, Constants.PLAYERS_FILE);

        for (GamePlayer p: gamePlayers) {
            Player player = playersInPlayersFile.getPlayer(p.getName());
            player.addGamePlayed();

            if (p.topThree()) {
                player.updateMedals(p.getPlace());
            }
        }

        playersInPlayersFile.savePlayers(ctx);
    }
}
