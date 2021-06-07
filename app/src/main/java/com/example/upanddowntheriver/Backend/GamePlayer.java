package com.example.upanddowntheriver.Backend;

public class GamePlayer extends Player implements java.io.Serializable {
    private int score;
    private int tricksTaken;
    private boolean dealer;
    private Constants.PLACE place;

    public GamePlayer(Player p) {
        super(p.getName(), p.getNickName());
        this.score = 0;
        this.tricksTaken = 0;
    }

    // Setters/getters...
    public void setScore(int score) {
        this.score = score;
    }

    public void setTricksTaken(int tricksTaken) {
        this.tricksTaken = tricksTaken;
    }

    public void setDealer(boolean isDealer) {
        this.dealer = isDealer;
    }

    public void setPlace(Constants.PLACE place) {
        this.place = place;
    }

    public Integer getScore() {
        return score;
    }

    public int getTricksTaken() {
        return tricksTaken;
    }

    public boolean getDealer() {
        return dealer;
    }

    public Constants.PLACE getPlace() {
        return place;
    }

    public boolean topThree() {
        return place != Constants.PLACE.OTHER;
    }
}
