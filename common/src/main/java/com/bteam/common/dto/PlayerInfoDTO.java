package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

public class PlayerInfoDTO implements JsonDTO{
    private final String username;
    private final String playerId;

    public PlayerInfoDTO(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
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
