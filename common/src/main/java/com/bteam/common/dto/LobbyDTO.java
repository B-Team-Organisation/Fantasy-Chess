package com.bteam.common.dto;

import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;
import com.bteam.common.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for all infos about a Lobby
 *
 * @author Marc
 */
public class LobbyDTO implements JsonDTO {
    private final String lobbyId;
    private final int maxPlayers;
    private final List<PlayerDTO> players;
    private final String hostId;
    private final LobbyModel.GameState gameState;
    private final String lobbyName;

    public LobbyDTO() {
        lobbyId = "";
        maxPlayers = 0;
        players = new ArrayList<>();
        hostId = "";
        gameState = LobbyModel.GameState.OPEN;
        lobbyName = "";
    }

    public LobbyDTO(LobbyModel lobbyModel) {
        this.lobbyId = lobbyModel.getLobbyId();
        this.maxPlayers = lobbyModel.getMaxPlayers();
        this.players = lobbyModel.getPlayers().stream().map(PlayerDTO::new).collect(Collectors.toList());
        this.hostId = lobbyModel.getHost().getPlayerId();
        this.gameState = lobbyModel.getGameState();
        this.lobbyName = lobbyModel.getLobbyName();
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<PlayerDTO> getPlayerIds() {
        return players;
    }

    public String getHostId() {
        return hostId;
    }

    public LobbyModel.GameState getGameState() {
        return gameState;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    @Override
    public String toJson() {

        return new JsonWriter().writeKeyValue("lobbyId", lobbyId)
            .and().writeKeyValue("name", lobbyName)
            .and().writeKeyValue("maxPlayers", maxPlayers)
            .and().writeList("players", players)
            .and().writeKeyValue("hostId", hostId)
            .and().writeKeyValue("gameState", gameState.name())
            .toString();
    }
}
