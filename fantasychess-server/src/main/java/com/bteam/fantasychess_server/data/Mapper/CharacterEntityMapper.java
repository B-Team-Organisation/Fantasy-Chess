package com.bteam.fantasychess_server.data.Mapper;

import com.bteam.fantasychess_server.data.entities.CharacterEntity;
import models.Vector2D;

public class CharacterEntityMapper {

    public static entities.CharacterEntity EntityToModel(CharacterEntity characterEntity) {
        return new entities.CharacterEntity(
                characterEntity.getCharacterDataModel(),
                characterEntity.getCurrentHealth(),
                new Vector2D(characterEntity.getPositionX(), characterEntity.getPositionY()));
    }

}
