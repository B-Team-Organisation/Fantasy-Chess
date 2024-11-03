package models;

import java.util.Objects;

/**
 * Represents a player in the game, holding their associated name, playerId and status.
 */
public class Player {
    private String username;
    private int playerId;
    private boolean isReady;



    /**
     * Constructor for a Player.
     *
     * username: name of the players
     * playerId: Id of the player
     * isReady: status of the player(ready, not ready)
     *
     */
    public Player(String username, int playerId, boolean isReady) {
        this.username = username;
        this.playerId = playerId;
        this.isReady = false;
    }


    public String getUsername() {
        return username;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    @Override
    public String toString() {
        return "Username: " + username + "PlayerId: " + playerId + "Status: "+ isReady;
    }


}