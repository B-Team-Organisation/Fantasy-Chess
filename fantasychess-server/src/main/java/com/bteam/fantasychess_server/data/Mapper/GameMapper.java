package com.bteam.fantasychess_server.data.Mapper;

import Exceptions.NotImplementedException;
import com.bteam.fantasychess_server.data.entities.GameEntity;
import com.bteam.fantasychess_server.data.repositories.GameRepository;
import models.GameModel;
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
