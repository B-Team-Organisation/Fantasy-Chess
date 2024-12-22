package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

public class PlayerReadyDTO implements JsonDTO {
    public static final String PLAYER_READY = "playerReady";
    public static final String PLAYER_NOT_READY = "playerNotReady";
    String status;
    String clientId;

    public PlayerReadyDTO() {
        status = PLAYER_READY;
        clientId = "";
    }

    public PlayerReadyDTO(String status, String clientId) {
        this.status = status;
        this.clientId = clientId;
    }

    public static PlayerReadyDTO ready(String clientId) {
        return new PlayerReadyDTO(PLAYER_READY, clientId);
    }

    public static PlayerReadyDTO notReady(String clientId) {
        return new PlayerReadyDTO(PLAYER_NOT_READY, clientId);
    }

    public String getStatus() {
        return status;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("status", status)
            .and().writeKeyValue("clientId", clientId).toString();
    }
}
