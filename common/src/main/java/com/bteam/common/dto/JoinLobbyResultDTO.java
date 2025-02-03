package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * Data Transfer Object sent to let the client know all details about
 * the result of joining a lobby, weather it failed or succeded
 *
 * @author Marc
 */
public class JoinLobbyResultDTO implements JsonDTO {
    public static String SUCCESS = "success";
    public static String ERROR = "error";
    private String result;
    private LobbyDTO lobby;

    public JoinLobbyResultDTO() {
        result = SUCCESS;
    }

    public JoinLobbyResultDTO(String result, LobbyDTO lobby) {
        this.result = result;
        this.lobby = lobby;
    }

    public static JoinLobbyResultDTO success(LobbyDTO lobby) {
        JoinLobbyResultDTO dto = new JoinLobbyResultDTO();
        dto.lobby = lobby;
        return dto;
    }

    public static JoinLobbyResultDTO error(LobbyDTO lobby) {
        JoinLobbyResultDTO dto = new JoinLobbyResultDTO();
        dto.lobby = lobby;
        dto.result = ERROR;
        return dto;
    }

    public boolean isSuccess() {
        return result.equals(SUCCESS);
    }

    public String getResult() {
        return result;
    }


    @Override
    public String toJson() {
        return new JsonWriter()
                .writeKeyValue("result", result).and()
                .writeKeyValue("lobby", lobby).toString();
    }
}
