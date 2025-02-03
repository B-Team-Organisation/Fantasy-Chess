package com.bteam.common.dto;

import com.bteam.common.models.Player;
import com.bteam.common.utils.JsonWriter;

public class PlayerInfoDTO implements JsonDTO {
    private final String username;
    private final String playerId;
    private final Player.Status status;

    public PlayerInfoDTO(String playerId, String username, Player.Status status) {
        this.playerId = playerId;
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Player.Status getStatus() {
        return status;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("username", getUsername())
                .and().writeKeyValue("playerId", getPlayerId())
                .and().writeKeyValue("status", status.name()).toString();
    }
}
