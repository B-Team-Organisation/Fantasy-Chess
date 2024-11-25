package com.bteam.common.dto;

import com.bteam.common.models.LobbyModel;

import java.util.List;

public class LobbyDTO implements JsonDTO{
    LobbyModel lobby;

    public LobbyDTO(LobbyModel lobby) {
        this.lobby = lobby;
    }

    @Override
    public String toJson() {
        return "{\"lobbyId\":\"" + lobby.getLobbyId() + "\"}";
    }
}
