package com.bteam.common.dto;

import java.util.UUID;

public class JoinLobbyDTO implements JsonDTO{
    String id;

    public JoinLobbyDTO() {
        this.id = UUID.randomUUID().toString();
    }

    public JoinLobbyDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\": \"").append(id).append("\"");
        sb.append("}");

        return sb.toString();
    }
}
