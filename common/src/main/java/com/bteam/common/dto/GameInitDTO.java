package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

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

    public List<CharacterEntityDTO> getCharacters() {
        return characters;
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeList("characters", characters)
            .and().writeKeyValue("gameId", gameId).toString();
    }
}
