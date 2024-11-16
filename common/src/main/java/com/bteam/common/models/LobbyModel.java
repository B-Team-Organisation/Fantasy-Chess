package com.bteam.common.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * This class represents the game lobby.
 * <p>
 * A lobby, consisting of its name, ID, Players, Player Limit and the current
 * {@link GameState}
 *
 * @author Adnan
 * @link 1.0
 */
public class LobbyModel {
    private String lobbyId;
    private int maxPlayers = 2;
    private List<Player> players;
    private Player host;
    private GameState gameState;
    private String lobbyName;

    public enum GameState {
        CLOSED,
        OPEN,
        FULL,
        RUNNING
    }

    /**
     * Constructor for a LobbyModel.
     *
     * @param lobbyId unique identifier for the lobby.
     * @param players List of the players that can enter the lobby
     * @param host player who is the host of the lobby and is added at index 0 in the players list
     * @param lobbyName name of the lobby
     */
    public LobbyModel(String lobbyId, List<Player> players, Player host, String lobbyName) {
        this.lobbyId = lobbyId;
        this.gameState = GameState.OPEN;
        this.players = new ArrayList<>();
        this.host = host;
        this.lobbyName = lobbyName;

        this.players.add(host);
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getHost() {
        return host;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Checks if the specified player is the host of the lobby.
     *
     * @param player the player to check
     * @return true if the player is the host, false otherwise
     */
    public boolean isHost(Player player){
        return host.equals(player);
    }

    /**
     * Adds a player to the lobby if there is space available.
     *
     * @param player - the player to add
     */
    public void addPlayer(Player player){
        if (this.players.size() < maxPlayers){
            this.players.add(player);
            if (this.maxPlayers == players.size()){
                gameState = GameState.FULL;
            } else {
                gameState = GameState.OPEN;
            }
        } else {
            throw new IllegalStateException("Lobby is already full.");
        }
    }

    /**
     * Removes a player from the lobby and updates the game state if necessary.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player){
        this.players.remove(player);

        if (this.players.size() < maxPlayers){
            gameState = GameState.OPEN;
        }
    }

    public String toString() {
        return "lobbyId: " + lobbyId + ", players: " + players + ", host: " + host + "lobbyName: " + lobbyName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof LobbyModel && lobbyId.equals(((LobbyModel) o).lobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, players, host, lobbyName);
    }
}