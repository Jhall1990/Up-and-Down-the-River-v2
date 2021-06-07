package com.example.upanddowntheriver.Backend;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Game implements java.io.Serializable {
    private final ArrayList<GamePlayer> players;
    private final ArrayList<Round> rounds;
    private Constants.TRUMP_MODE trumpMode;
    private Constants.SUIT curTrump;
    private GamePlayer curDealer;
    private int numRounds;
    private int curRound;
    private int handSize;
    private int maxCards;
    private ArrayList<Constants.SUIT> trumpArray;
    private boolean goingUp;
    private long endTime;

    public Game(PlayerCollection players) {
        this.players = new ArrayList<>();
        this.rounds = new ArrayList<>();

        for (Player p: players.getPlayers()) {
            this.players.add(new GamePlayer(p));
        }
    }

    // Getters...
    public ArrayList<GamePlayer> getPlayers() {
        return players;
    }

    public Constants.SUIT getCurTrump() {
        return curTrump;
    }

    public int getCurRound() {
        return curRound;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public GamePlayer getCurDealer() {
        return curDealer;
    }

    public int getHandSize() {
        return handSize;
    }

    public GamePlayer getPlayer(int index) {
        return players.get(index);
    }

    public GamePlayer getPlayer(String name) {
        for (GamePlayer p: players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return players.get(0);
    }

    public int getPlayerStreak(GamePlayer player) {
        int streak = 0;

        for (Round r: rounds) {
            if (!r.getFinished()) {
                break;
            }

            if (r.madeBid(player.getName())) {
                streak++;
            } else {
                streak = 0;
            }
        }
        return streak;
    }

    public int getPlayerBidsMade(GamePlayer player) {
        int made = 0;

        for (Round r: rounds) {
            if (r.getFinished() && r.madeBid(player.getName())) {
                made++;
            }
        }
        return made;
    }

    public int getPlayerScore(GamePlayer player) {
        return player.getScore();
    }

    public int getScoreAtRound(GamePlayer player, int round) {
        int score = 0;
        for (int i = 0; i <= round; i++) {
            score += rounds.get(i).playerScore(player.getName());
        }
        return score;
    }

    public Round getRound() {
        return rounds.get(rounds.size() - 1);
    }

    public Round getRound(int idx) {
        return rounds.get(idx);
    }

    public int getRoundBids() {
        return getRound().getBidTotal();
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public boolean getGoingUp() {
        return goingUp;
    }

    public Constants.TRUMP_MODE getTrumpMode() {
        return trumpMode;
    }

    // Setters...
    public void setDealer(GamePlayer p) {
        curDealer.setDealer(false);
        curDealer = p;
        curDealer.setDealer(true);
    }

    public void setCurTrump(Constants.SUIT suit) {
        curTrump = suit;
        getRound().setTrump(suit);
    }

    // Other methods...
    public boolean isOver() {
        return this.curRound > this.numRounds;
    }

    public int getMaxHandSize(int numDecks) {
        int totalCards = numDecks * Constants.DECK_SIZE;
        return totalCards / players.size();
    }

    public void getNextHandSize() {
        if (goingUp) {
            handSize++;
            if (handSize == maxCards) {
                goingUp = false;
            }
        } else {
            handSize--;
            if (handSize == 1) {
                goingUp = true;
            }
        }
    }

    public void finishRound() {
        this.curRound++;

        // Update the dealer in the players array, then move the dealer to
        // the end of the array as they bid last.
        GamePlayer nextDealer = this.players.remove(0);
        this.setDealer(nextDealer);
        this.players.add(nextDealer);

        // Update the trump.
        this.curTrump = nextTrump();

        // Update each players score.
        for (GamePlayer p: players) {
            p.setScore(p.getScore() + getRound().playerScore(p.getName()));
        }

        // Update the current hand size.
        getNextHandSize();

        // Mark the current round finished.
        getRound().setFinished();

        // Start a new round
        if (!isOver()) {
            rounds.add(new Round(curTrump));
        }
    }

    public void finalizeSettings(Constants.TRUMP_MODE trumpMode, Constants.UP_AND_DOWN_MODE upAndDownMode,
                                 int maxCards, GamePlayer dealer) {
        this.trumpMode = trumpMode;
        this.maxCards = maxCards;
        this.curRound = 1;
        this.curDealer = dealer;
        dealer.setDealer(true);

        switch (upAndDownMode) {
            case UP_AND_DOWN:
                numRounds = maxCards * 2 - 1;
                handSize = 1;
                goingUp = true;
                break;
            case UP:
                numRounds = maxCards;
                handSize = 1;
                goingUp = true;
                break;
            case DOWN_AND_UP:
                numRounds = maxCards * 2 - 1;
                handSize = maxCards;
                goingUp = false;
                break;
            case DOWN:
                numRounds = maxCards;
                handSize = maxCards;
                goingUp = false;
                break;
        }

        if (trumpMode == Constants.TRUMP_MODE.CLASSIC) {
            generateTrump();
        }

        curTrump = nextTrump();
        orderByDealer();
        rounds.add(new Round(curTrump));
    }

    private void generateTrump() {
        trumpArray = new ArrayList<>();
        trumpArray.addAll(Arrays.asList(Constants.SUIT.values()));
        Collections.shuffle(trumpArray);
    }

    private Constants.SUIT nextTrump() {
        switch (trumpMode) {
            case CLASSIC:
                Constants.SUIT suit = trumpArray.remove(0);
                trumpArray.add(suit);
                return suit;
            case RANDOM:
                int i = new Random().nextInt(Constants.SUIT.values().length);
                return trumpArray.get(i);
            case DEALER_CHOICE:
                return null;
        }

        return Constants.SUIT.NONE;
    }

    private ArrayList<GamePlayer> copyPlayers() {
        return new ArrayList<>(players);
    }

    public ArrayList<GamePlayer> getPlayersInScoreOrder() {
        ArrayList<GamePlayer> copy = copyPlayers();

        // Sort the new collection by score.
        Collections.sort(copy, (o1, o2) -> o1.getScore().compareTo(o2.getScore()));
        Collections.reverse(copy);
        return copy;
    }

    private void orderByDealer() {
        GamePlayer curPlayer = players.get(0);
        GamePlayer move;

        while (!curPlayer.getName().equals(curDealer.getName())) {
            move = players.remove(0);
            players.add(move);
            curPlayer = players.get(0);
        }
        move = players.remove(0);
        players.add(move);
    }

    /*
    This saves a completed game so the results can be looked at later.
     */
    public void save(Context c) {
        endTime = System.currentTimeMillis();
        String fileName = String.format("game_%d", endTime);
        File gameDir = new File(Constants.previousGamePath(c));

        if (!gameDir.exists()) {
            gameDir.mkdir();
        }

        try {
            FileOutputStream gameFile = new FileOutputStream(Constants.previousGamePath(c, fileName));
            ObjectOutputStream oos = new ObjectOutputStream(gameFile);
            oos.writeObject(this);
            oos.close();
            Log.i("gameSave", String.format("%s saved", fileName));
        } catch (FileNotFoundException e) {
            Log.i("gameSave", "Could not save game file.");
        } catch (IOException e) {
            Log.i("gameSave", "Couldn't write to the game file");
        }
    }

    public String getGameName() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(endTime);
    }
}