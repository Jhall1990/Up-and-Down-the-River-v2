package com.example.upanddowntheriver.Backend;

import android.content.Context;
import android.text.TextUtils;

import com.example.upanddowntheriver.R;

import java.io.File;


public class Constants {
    public static final String PLAYERS_FILE = "players.yaml";
    public static final String PREVIOUS_GAME_FOLDER = "previousGames";
    public static final int ADD_PLAYER = 1;
    public static final int EDIT_PLAYER = 2;
    public static final int DECK_SIZE = 52;

    public static String previousGamePath(Context c, String gameFile) {
        String[] paths = {c.getFilesDir().toString(), PREVIOUS_GAME_FOLDER, gameFile};
        return TextUtils.join(File.separator, paths);
    }

    public static String previousGamePath(Context c) {
        String[] paths = {c.getFilesDir().toString(), PREVIOUS_GAME_FOLDER};
        return TextUtils.join(File.separator, paths);
    }

    // Trump mode enum.
    public enum TRUMP_MODE {
        CLASSIC,      // Unique suit first 5 hands, repeat pattern
        RANDOM,       // Choose a random suit each round.
        DEALER_CHOICE,  // User gets to choose the suit at the beginning of the round.
    }

    // Up or down mode enum
    public enum UP_AND_DOWN_MODE {
        UP_AND_DOWN,  // 1 -> N -> 1
        DOWN_AND_UP,  // N -> 1 -> N
        UP,           // 1 -> N
        DOWN,         // N -> 1
    }

    // Trump suits
    public enum SUIT {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES,
        NONE,
    }

    public enum PLACE {
        FIRST,
        SECOND,
        THIRD,
        OTHER,
    }

    public static int getTrumpImage(SUIT suit) {
        if (suit == null) {
            return R.drawable.ic_dealer_choice;
        }

        switch (suit) {
            case HEARTS:
                return R.drawable.ic_heart;
            case DIAMONDS:
                return R.drawable.ic_diamond;
            case CLUBS:
                return R.drawable.ic_club;
            case SPADES:
                return R.drawable.ic_spade;
            case NONE:
                return R.drawable.ic_no_trump;
        }
        return R.drawable.ic_no_trump;
    }

    public static String formatTrumpMode(TRUMP_MODE mode) {
        return format(mode.name());
    }

    public static String formatUpAndDownMode(UP_AND_DOWN_MODE mode) {
        return format(mode.name());
    }

    private static String format(String s) {
        String lower = s.toLowerCase().replace("_", " ");
        return Character.toString(lower.charAt(0)).toUpperCase() + lower.substring(1);
    }

    public static TRUMP_MODE trumpModeFromString(String s) {
        return TRUMP_MODE.valueOf(s.replace(" ", "_").toUpperCase());
    }

    public static UP_AND_DOWN_MODE upAndDownModeFromString(String s) {
        return UP_AND_DOWN_MODE.valueOf(s.replace(" ", "_").toUpperCase());
    }
}

