package com.bteam.common.dto;

import com.bteam.common.models.Player;
import com.bteam.common.utils.JsonWriter;

public class PlayerDTO implements JsonDTO{
    private final String username;
    private final String playerId;

    public PlayerDTO(Player player) {
        this.username = player.getUsername();
        this.playerId = player.getPlayerId();
    }

    public PlayerDTO(String username, String playerId) {
        this.username = username;
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerId() {
        return playerId;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("username", getUsername())
            .and().writeKeyValue("playerId", getPlayerId()).toString();
    }
}
