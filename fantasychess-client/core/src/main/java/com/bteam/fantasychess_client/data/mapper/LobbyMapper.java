package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.models.LobbyModel;

import java.util.ArrayList;
import java.util.List;

public final class LobbyMapper {

	public static LobbyModel lobbyFromJson(JsonValue lobbyJson) {
        String lobbyName = lobbyJson.get("name").asString();
        String lobbyId = lobbyJson.getString("lobbyId");
        LobbyModel.GameState gameState = LobbyModel.GameState.valueOf(lobbyJson.getString("gameState"));
        int maxPlayers = lobbyJson.getInt("maxPlayers");

        return new LobbyModel(lobbyId,lobbyName,gameState,maxPlayers);
	}

    public static List<LobbyModel> lobbiesFromJson(JsonValue lobbiesJson) {
        List<LobbyModel> lobbies = new ArrayList<>();
        lobbiesJson.forEach((l) -> lobbies.add(lobbyFromJson(l)));
        return lobbies;
    }
}
