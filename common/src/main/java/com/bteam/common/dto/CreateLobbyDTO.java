package com.bteam.common.dto;

public class CreateLobbyDTO implements JsonDTO{
    private String lobbyName;

    public CreateLobbyDTO(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    @Override
    public String toJson() {
        return "{\"lobbyName\":\""+lobbyName+"\"}";
    }
}
