package com.bteam.common.dto;

import java.util.ArrayList;
import java.util.List;
/**
 * Data Transfer Object for a list of Lobbies
 *
 * @author Marc
 */
public class LobbyListDTO implements JsonDTO{
    List<LobbyDTO> lobbies;

    public LobbyListDTO() {
        lobbies = new ArrayList<LobbyDTO>();
    }

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
