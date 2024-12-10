package com.bteam.common.dto;

import java.util.List;

public class GameInitDTO implements JsonDTO {
    List<CharacterEntityDTO> characters;

    public GameInitDTO(List<CharacterEntityDTO> characters) {
        this.characters = characters;
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
        sb.append("]");
        sb.append("}");


        return sb.toString();
    }
}
