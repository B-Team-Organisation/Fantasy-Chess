package com.bteam.common.dto;

/**
 * Data Transfer Object to let the server know all details about
 * the lobby that is to be created.
 * @author Marc
 */
public class CreateLobbyDTO implements JsonDTO{
    private String lobbyName;

    public CreateLobbyDTO(){
        this.lobbyName = "";
    }

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
