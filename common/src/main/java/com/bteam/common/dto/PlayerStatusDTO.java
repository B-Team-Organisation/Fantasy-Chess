package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

public class PlayerStatusDTO implements JsonDTO {
    public static final String PLAYER_READY = "playerReady";
    public static final String PLAYER_NOT_READY = "playerNotReady";
    public static final String PLAYER_ABANDON = "playerAbandon";
    String status;
    String clientId;

    public PlayerStatusDTO() {
        status = PLAYER_READY;
        clientId = "";
    }

    public PlayerStatusDTO(String status, String clientId) {
        this.status = status;
        this.clientId = clientId;
    }

    public static PlayerStatusDTO ready(String clientId) {
        return new PlayerStatusDTO(PLAYER_READY, clientId);
    }

    public static PlayerStatusDTO notReady(String clientId) {
        return new PlayerStatusDTO(PLAYER_NOT_READY, clientId);
    }

    public static PlayerStatusDTO abandoned(String clientId) {
        return new PlayerStatusDTO(PLAYER_ABANDON, clientId);
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
