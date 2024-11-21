package com.bteam.common.dto;

public class CreateLobbyDTO {
    private String lobbyName;

    public CreateLobbyDTO(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
