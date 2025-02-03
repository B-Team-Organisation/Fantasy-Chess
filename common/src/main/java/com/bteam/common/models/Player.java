package com.bteam.common.models;

import com.bteam.common.entities.CharacterEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a player.
 * <p>
 * Represents a player in the game, holding their associated name, playerId, list of characters and status.
 *
 * @author Adnan, Albano
 * @version 1.1
 */
public class Player {
    private final String playerId;
    private String username;
    private Status status;
    private List<CharacterEntity> characters;

    /**
     * Constructor for a Player.
     *
     * @param username   name of the players
     * @param playerId   id of the player as stringified UUID
     * @param characters list of characters associated with the player
     */
    public Player(String username, String playerId, List<CharacterEntity> characters) {
        this.username = username;
        this.playerId = playerId;
        this.status = Status.NOT_READY;
        this.characters = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return playerId as stringified UUID
     */
    public String getPlayerId() {
        return playerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CharacterEntity> getCharacters() {
        return characters;
    }

    public void setCharacters(List<CharacterEntity> characters) {
        this.characters = characters != null ? characters : new ArrayList<>();
    }

    /**
     * Adds a character to the player's character list.
     *
     * @param character The character to add
     */
    public boolean addCharacter(CharacterEntity character) {
        if (character != null && !characters.contains(character)) {
            return characters.add(character);
        }
        return false;
    }

    /**
     * Removes a character from the player's character list.
     *
     * @param character The character to remove
     */
    public boolean removeCharacter(CharacterEntity character) {
        if (character != null && characters.contains(character)) {
            return characters.remove(character);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[Username=" + username + ", PlayerId=" + playerId + ", Status=" + status + ", Characters=" + characters + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, username, status, characters);
    }

    public boolean equals(Object o) {
        return o instanceof Player
                && ((Player) o).playerId.equals(playerId)
                && ((Player) o).username.equals(username)
                && ((Player) o).status.equals(status)
                && ((Player) o).characters.equals(characters);

    }

    public enum Status {
        NOT_READY {
            public static final String DISPLAY_TEXT = "NOT READY";

            @Override
            public String getDisplay() {
                return DISPLAY_TEXT;
            }

            @Override
            public Status toggle() {
                return READY;
            }
        },
        READY {
            public static final String DISPLAY_TEXT = "NOT READY";

            @Override
            public String getDisplay() {
                return DISPLAY_TEXT;
            }

            @Override
            public Status toggle() {
                return NOT_READY;
            }
        };

        public abstract Status toggle();

        public abstract String getDisplay();
    }
}
