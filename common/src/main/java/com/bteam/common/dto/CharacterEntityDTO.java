package com.bteam.common.dto;

import com.bteam.common.entities.CharacterEntity;

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
        return "{\"modelId\":\"" + character.getCharacterBaseModel().getName() + "\"," +
                "\"health\":\"" + character.getHealth() + "\"," +
                "\"playerId\":\"" + character.getPlayerId() + "\"," +
                "\"id\":" + character.getId() + "," +
                "\"x\":" + character.getPosition().getX() + "," +
                "\"y\":" + character.getPosition().getY() + "}";
    }
}
