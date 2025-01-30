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

    public JoinLobbyResultDTO() {
        result = SUCCESS;
    }

    public JoinLobbyResultDTO(String result) {
        this.result = result;
    }

    public static JoinLobbyResultDTO success() {
        JoinLobbyResultDTO dto = new JoinLobbyResultDTO();
        dto.result = SUCCESS;
        return dto;
    }

    public static JoinLobbyResultDTO error() {
        JoinLobbyResultDTO dto = new JoinLobbyResultDTO();
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
        return new JsonWriter().writeKeyValue("result", result).toString();
    }
}
