package models;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a player in the game, holding their associated name, playerId and status.
 */
public class Player {
    private String username;
    private UUID playerId;
    private Status status;

    enum Status{
        NOT_READY,
        READY,
    }


    /**
     * Constructor for a Player.
     * username: name of the players
     * playerId: id of the player
     * isReady: status of the player(ready, not ready)
     *
     */
    public Player(String username, UUID playerId) {
        this.username = username;
        this.playerId = playerId;
        this.status = Status.NOT_READY;
    }


    public String getUsername() {
        return username;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "Username: " + username + "PlayerId: " + playerId + "Status: "+ status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, username, status);
    }

    public boolean equals(Object o){
        return o instanceof Player && ((Player)o).playerId.equals(playerId);
    }
}