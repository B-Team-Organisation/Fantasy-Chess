package com.bteam.common.dto;

import java.util.List;

public class LobbyListDTO implements JsonDTO{
    List<LobbyDTO> lobbies;

    public LobbyListDTO(List<LobbyDTO> lobbies) {
        this.lobbies = lobbies;
    }

    @Override
    public String toJson() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"lobbies\": [");
        for (LobbyDTO lobby : lobbies) {
            sb.append(lobby.toJson());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]}");
        return sb.toString();
    }


}
