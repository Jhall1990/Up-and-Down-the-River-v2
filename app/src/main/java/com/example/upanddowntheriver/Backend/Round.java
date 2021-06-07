package com.example.upanddowntheriver.Backend;

import java.util.HashMap;

public class Round implements java.io.Serializable {
    private final HashMap<String, Integer> bids;
    private final HashMap<String, Integer> tricks;
    private boolean finished;
    private Constants.SUIT trump;

    public Round(Constants.SUIT trump) {
        bids = new HashMap<>();
        tricks = new HashMap<>();
        finished = false;
        this.trump = trump;
    }

    public void setTrump(Constants.SUIT trump) {
        this.trump = trump;
    }

    public Constants.SUIT getTrump() {
        return trump;
    }

    public void addBid(String name, int bid) {
        bids.put(name, bid);
    }

    public int getBid(String name) {
        return bids.get(name);
    }

    public int removeBid(String name) {
        return bids.remove(name);
    }

    public void setFinished() {
        finished = true;
    }

    public boolean getFinished() {
        return finished;
    }

    public void addTaken(String name, int taken) {
        tricks.put(name, taken);
    }

    public int getTaken(String name) {
        return tricks.get(name);
    }

    public int getBidTotal() {
        int total = 0;

        for (int bid: bids.values()) {
            total += bid;
        }

        return total;
    }

    public boolean madeBid(String name) {
        int numTaken;
        int bid;

        if (tricks.containsKey(name)) {
            numTaken = tricks.get(name);
        } else {
            return false;
        }

        if (bids.containsKey(name)) {
            bid = bids.get(name);
        } else {
            return false;
        }

        return numTaken == bid;
    }

    public int playerScore(String name) {
        int taken = tricks.get(name);
        if (madeBid(name)) {
            return taken + 10;
        }
        return taken;
    }
}
