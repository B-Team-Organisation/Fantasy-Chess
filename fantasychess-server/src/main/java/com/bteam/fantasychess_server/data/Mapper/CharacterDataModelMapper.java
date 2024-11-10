package com.bteam.fantasychess_server.data.Mapper;

import com.bteam.fantasychess_server.data.entities.CharacterDataEntity;
import models.CharacterDataModel;

public class CharacterDataModelMapper {
    public CharacterDataModel EntityToModel(CharacterDataEntity characterEntity) {
        return new CharacterDataModel(
                characterEntity.getId(),
                characterEntity.getName(),
                characterEntity.getHealth(),
                characterEntity.getAttackPatterns(),
                characterEntity.getMovementPatterns());
    }
}
