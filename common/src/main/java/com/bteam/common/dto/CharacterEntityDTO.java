package com.bteam.common.dto;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.utils.JsonWriter;

public class CharacterEntityDTO implements JsonDTO {
    CharacterEntity character;

    public CharacterEntityDTO(CharacterEntity characterEntity) {
        this.character = characterEntity;
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    @Override
    public String toJson() {
        return new JsonWriter()
            .writeKeyValue("modelId", character.getCharacterBaseModel().getName())
            .and().writeKeyValue("health", character.getHealth())
            .and().writeKeyValue("playerId", character.getPlayerId())
            .and().writeKeyValue("id", character.getId())
            .and().writeKeyValue("x", character.getPosition().getX())
            .and().writeKeyValue("y", character.getPosition().getY())
            .toString();
    }
}
