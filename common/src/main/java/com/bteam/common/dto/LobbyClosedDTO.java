package com.bteam.common.dto;

public class LobbyClosedDTO implements JsonDTO {
    private final String lobbyId;
    private final String reason;

    public LobbyClosedDTO(String lobbyId, String reason) {
        this.lobbyId = lobbyId;
        this.reason = reason;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toJson() {
        return "{" +
            "\"lobbyId\":\"" + lobbyId + "\"," +
            "\"reason\":\"" + reason + "\"" +
            "}";
    }
}
