package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * Data Transfer Object to let the server know all details about
 * the lobby the client wants to join.
 *
 * @author Marc
 */
public class JoinLobbyDTO implements JsonDTO {
    String id;

    public JoinLobbyDTO() {
        this.id = "";
    }

    public JoinLobbyDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("id", id).toString();
    }
}
