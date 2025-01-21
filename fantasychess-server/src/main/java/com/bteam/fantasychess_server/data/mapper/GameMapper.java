package com.bteam.fantasychess_server.data.mapper;

import com.bteam.common.exceptions.NotImplementedException;
import com.bteam.common.models.GameModel;
import com.bteam.fantasychess_server.data.entities.GameEntity;
import com.bteam.fantasychess_server.data.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    @Autowired
    GameRepository gameRepository;

    public static GameModel EntityToModel(GameEntity gameEntity) {
        throw new NotImplementedException("EntityToModel");
    }
}
