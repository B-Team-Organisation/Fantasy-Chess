package com.bteam.common.dto;

import com.bteam.common.models.Player;
import com.bteam.common.utils.JsonWriter;

public class PlayerDTO implements JsonDTO {
    private final String username;
    private final String playerId;
    private final String status;

    public PlayerDTO(Player player) {
        this.username = player.getUsername();
        this.playerId = player.getPlayerId();
        this.status = player.getStatus().toString();
    }

    public PlayerDTO(String username, String playerId, String status) {
        this.username = username;
        this.playerId = playerId;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("username", getUsername())
                .and().writeKeyValue("playerId", getPlayerId())
                .and().writeKeyValue("status", getStatus()).toString();
    }
}
