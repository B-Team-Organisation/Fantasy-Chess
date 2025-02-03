package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;

import java.util.ArrayList;
import java.util.List;

public final class LobbyMapper {

    private LobbyMapper() {
    }

    public static LobbyModel lobbyFromJson(JsonValue lobbyJson) {
        String lobbyName = lobbyJson.get("name").asString();
        String lobbyId = lobbyJson.getString("lobbyId");
        LobbyModel.GameState gameState = LobbyModel.GameState.valueOf(lobbyJson.getString("gameState"));
        int maxPlayers = lobbyJson.getInt("maxPlayers");
        var lobby = new LobbyModel(lobbyId, lobbyName, gameState, maxPlayers);
        lobbyJson.get("players").forEach(p -> {
            var playerId = p.getString("playerId");
            var username = p.getString("username");
            var status = Player.Status.valueOf(p.getString("status"));
            var player = new Player(username, playerId, List.of());
            player.setStatus(status);
            lobby.addPlayer(player);
        });
        return lobby;
    }

    public static List<LobbyModel> lobbiesFromJson(JsonValue lobbiesJson) {
        List<LobbyModel> lobbies = new ArrayList<>();
        lobbiesJson.forEach((l) -> lobbies.add(lobbyFromJson(l)));
        return lobbies;
    }
}
