package com.bteam.common.dto;

public class JoinLobbyResultDTO implements JsonDTO{
    private String result;

    public JoinLobbyResultDTO() {
        result = "SUCCESS";
    }

    public JoinLobbyResultDTO(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }


    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"result\":\"").append(result).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
