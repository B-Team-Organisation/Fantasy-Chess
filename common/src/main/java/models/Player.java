package models;

import java.util.ArrayList;
import entities.CharacterEntity;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a player.
 * <p>
 * Represents a player in the game, holding their associated name, playerId , list of characters and status.
 *
 * @author Adnan,Albano
 * @version 1.0
 */
public class Player {
    private String username;
    private UUID playerId;
    private Status status;
    private List<CharacterEntity> characters;

    enum Status{
        NOT_READY,
        READY,
    }


    /**
     * Constructor for a Player.
     *
     * @param username name of the players
     * @param playerId id of the player
     * @param characters list of characters associated with the player
     */
    public Player(String username, UUID playerId,List<CharacterEntity> characters) {
        this.username = username;
        this.playerId = playerId;
        this.status = Status.NOT_READY;
        this.characters = new ArrayList<>();
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

    public List<CharacterEntity> getCharacters(){
        return characters;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public void setCharacters(List<CharacterEntity> characters) {
        this.characters = characters != null ? characters : new ArrayList<>();
    }


    /**
     * Adds a character to the player's character list.
     *
     * @param character The character to add
     */
    public void addCharacter(CharacterEntity character) {
        if (character != null && !characters.contains(character)) {
            characters.add(character);
        }
    }

    /**
     * Removes a character from the player's character list.
     *
     * @param character The character to remove
     */
    public void removeCharacter(CharacterEntity character) {
        characters.remove(character);
    }

    @Override
    public String toString() {
        return "Username: " + username + "PlayerId: " + playerId + "Status: "+ status+ "Characters: " + characters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, username, status, characters);
    }

    public boolean equals(Object o){
        return o instanceof Player && ((Player)o).playerId.equals(playerId);
    }
}