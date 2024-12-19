package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * Data Transfer Object to let the server know all details about
 * the lobby that is to be created.
 *
 * @author Marc
 */
public class CreateLobbyDTO implements JsonDTO {
    private final String lobbyName;

    public CreateLobbyDTO() {
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
        return new JsonWriter().writeKeyValue("lobbyName", getLobbyName()).toString();
    }
}
