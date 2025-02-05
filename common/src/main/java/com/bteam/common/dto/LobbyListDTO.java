package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a list of Lobbies
 *
 * @author Marc
 */
public class LobbyListDTO implements JsonDTO {
    List<LobbyDTO> lobbies;

    public LobbyListDTO() {
        lobbies = new ArrayList<>();
    }

    public LobbyListDTO(List<LobbyDTO> lobbies) {
        this.lobbies = lobbies;
    }

    public List<LobbyDTO> getLobbies() {
        return lobbies;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeList("lobbies", lobbies).toString();
    }
}
