package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.JsonReader;
import com.bteam.common.models.Player;

import java.util.List;

public final class PlayerInfoMapper {
    private PlayerInfoMapper() {
    }

    public static Player fromDTO(String dto) {
        var json = new JsonReader().parse(dto).get("data");
        String username = json.getString("username");
        String playerId = json.getString("playerId");
        Player.Status status = Player.Status.valueOf(json.getString("status"));
        var player = new Player(username, playerId, List.of());
        player.setStatus(status);
        return player;
    }
}
