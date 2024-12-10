package com.bteam.common.dto;

import java.util.List;

public class GameInitDTO implements JsonDTO {
    List<CharacterEntityDTO> characters;
    String gameId;

    public GameInitDTO() {
        characters = List.of();
        gameId = "";
    }

    public GameInitDTO(List<CharacterEntityDTO> characters, String gameId) {
        this.characters = characters;
        this.gameId = gameId;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"characters\": [");
        for (CharacterEntityDTO character : characters) {
            sb.append(character.toJson());
            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],");
        sb.append("\"gameId\": \"").append(gameId).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
