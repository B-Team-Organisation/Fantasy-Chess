package com.bteam.common.dto;

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

    @Override
    public String toJson() {
        return "{" +
            "\"status\":\"" + status + "\"," +
            ", \"clientId\":\"" + clientId + "\"" +
            "}";
    }
}
