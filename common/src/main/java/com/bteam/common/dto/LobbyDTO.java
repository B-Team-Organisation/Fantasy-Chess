package com.bteam.common.dto;

import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LobbyDTO implements JsonDTO{
    private String lobbyId;
    private int maxPlayers;
    private List<String> playerIds;
    private String hostId;
    private LobbyModel.GameState gameState;
    private String lobbyName;

    public LobbyDTO(LobbyModel lobbyModel) {
        this.lobbyId = lobbyModel.getLobbyId();
        this.maxPlayers = lobbyModel.getPlayers().size();
        this.playerIds = lobbyModel.getPlayers().stream().map(Player::getPlayerId).collect(Collectors.toList());
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

    public List<String> getPlayerIds() {
        return playerIds;
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
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("lobbyId\":\"");
        sb.append(lobbyId);
        sb.append("\",");
        sb.append("name\":\"");
        sb.append(lobbyName);
        sb.append("\",");
        sb.append("maxPlayers\":");
        sb.append(maxPlayers);
        sb.append(",");
        sb.append("playerIds\":[");
        sb.append(playerIds.stream().collect(Collectors.joining(",")));
        sb.append("],");
        sb.append("hostId\":\"");
        sb.append(hostId);
        sb.append("\",");
        sb.append("gameState\":\"");
        sb.append(gameState.name());
        sb.append("\"");
        sb.append("}");
        return sb.toString();
    }
}
