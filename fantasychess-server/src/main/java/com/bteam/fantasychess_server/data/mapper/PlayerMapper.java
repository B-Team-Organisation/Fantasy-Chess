package com.bteam.fantasychess_server.data.mapper;

import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.data.entities.PlayerEntity;

import java.util.ArrayList;

public final class PlayerMapper {
    public static Player fromEntity(PlayerEntity playerEntity) {
        var player = new Player(
            playerEntity.getName(),
            playerEntity.getId().toString(),
            new ArrayList<>());
        player.setStatus(playerEntity.getStatus());
        return player;
    }
}
