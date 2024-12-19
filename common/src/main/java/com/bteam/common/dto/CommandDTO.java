package com.bteam.common.dto;

import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.utils.JsonWriter;

public class CommandDTO implements JsonDTO {
    int x;
    int y;
    String characterId;
    CommandType commandType;

    public CommandDTO() {
        this.x = 0;
        this.y = 0;
        this.characterId = "";
        commandType = CommandType.MOVEMENT;
    }

    public CommandDTO(Vector2D position, String characterId, CommandType commandType) {
        this.x = position.getX();
        this.y = position.getY();
        this.characterId = characterId;
        this.commandType = commandType;
    }

    public CommandDTO(AttackDataModel attack) {
        this.x = attack.getAttackPosition().getX();
        this.y = attack.getAttackPosition().getY();
        characterId = attack.getAttacker();
        commandType = CommandType.ATTACK;
    }

    public CommandDTO(MovementDataModel movement) {
        this.x = movement.getMovementVector().getX();
        this.y = movement.getMovementVector().getY();
        characterId = movement.getCharacterId();
        commandType = CommandType.MOVEMENT;
    }

    public Vector2D getPosition() {
        return new Vector2D(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCharacterId() {
        return characterId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("characterId", characterId)
                .and().writeKeyValue("x", getX())
                .and().writeKeyValue("y", getY())
                .and().writeKeyValue("commandType", getCommandType().name())
                .toString();
    }

    public enum CommandType {
        ATTACK,
        MOVEMENT
    }
}
