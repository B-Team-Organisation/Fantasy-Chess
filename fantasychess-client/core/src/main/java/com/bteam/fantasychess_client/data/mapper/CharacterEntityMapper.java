package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.CharacterEntityDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.stores.CharacterStore;

import java.util.ArrayList;
import java.util.List;

public final class CharacterEntityMapper {
    public static CharacterEntity fromDTO(String data) {
        return fromJson(new JsonReader().parse(data).get("data"));
    }

    public static CharacterEntity fromJson(JsonValue json) {
        Vector2D position = new Vector2D(json.getInt("x"), json.getInt("y"));
        int health = json.getInt("health");
        String playerId = json.getString("playerId");
        String id = json.getString("id");
        CharacterDataModel model = CharacterStore.getCharacter(json.getString("modelId"));
        return new CharacterEntity(model, id, health, position, playerId);
    }

    public static String toDTO(CharacterEntity entity) {
        return new CharacterEntityDTO(entity).toJson();
    }

    public static List<CharacterEntity> fromListDTO(String data) {
        JsonValue json = new JsonReader().parse(data);
        List<CharacterEntity> entities = new ArrayList<>();
        json.get("data").get("characters").forEach(j -> entities.add(fromJson(j)));
        return entities;
    }
}
