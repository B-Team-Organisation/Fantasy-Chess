package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.CommandDTO;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.models.TurnResult;
import com.bteam.common.utils.PairNoOrder;
import com.bteam.fantasychess_client.Main;

import java.util.ArrayList;
import java.util.logging.Level;

public final class TurnResultMapper {

    private TurnResultMapper() {}

    public static TurnResult fromDTO(String str) {
        JsonValue value = new JsonReader().parse(str);
        var data = value.get("data");
        var conflicts = new ArrayList<PairNoOrder<MovementDataModel, MovementDataModel>>();
        Main.getLogger().log(Level.SEVERE, data.toString());
        data.get("conflictCommands").forEach(j -> conflicts.add(movementPairFromJson(j)));
        Main.getLogger().log(Level.SEVERE, data.get("conflictCommands").toString());
        var characters = CharacterEntityMapper.fromListJson(data.get("updatedCharacters"));
        var validMoves = new ArrayList<MovementDataModel>();
        var validAttacks = new ArrayList<AttackDataModel>();

        Main.getLogger().log(Level.SEVERE, data.get("validCommands").toString());
        data.get("validCommands").get("commands").forEach(j -> {
            Main.getLogger().log(Level.SEVERE, j.toString());
            if (j.getString("commandType").equals(CommandDTO.CommandType.MOVEMENT.toString())) {
                validMoves.add(movementFromJson(j));
            } else {
                validAttacks.add(attackFromJson(j));
            }
        });

        var winner = data.getString("winner");
        return new TurnResult(characters, conflicts, validMoves, validAttacks,winner);
    }

    public static PairNoOrder<MovementDataModel, MovementDataModel> movementPairFromJson(JsonValue value) {
        var first = movementFromJson(value.get("first"));
        var second = movementFromJson(value.get("second"));
        return new PairNoOrder<>(first, second);
    }

    public static MovementDataModel movementFromJson(JsonValue json) {
        var x = json.getInt("x");
        var y = json.getInt("y");
        var id = json.getString("characterId");
        return new MovementDataModel(id, new Vector2D(x, y));
    }

    public static AttackDataModel attackFromJson(JsonValue json) {
        var x = json.getInt("x");
        var y = json.getInt("y");
        var id = json.getString("characterId");
        return new AttackDataModel(new Vector2D(x, y), id);
    }
}
