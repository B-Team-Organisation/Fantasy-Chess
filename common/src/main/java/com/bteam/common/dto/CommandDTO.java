package com.bteam.common.dto;

import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;

public class CommandDTO implements JsonDTO {
    Vector2D position;
    String characterId;
    CommandType commandType;

    public CommandDTO() {
        this.position = new Vector2D(0, 0);
        this.characterId = "";
    }

    public CommandDTO(Vector2D position, String characterId, CommandType commandType) {
        this.position = position;
        this.characterId = characterId;
        this.commandType = commandType;
    }

    public CommandDTO(AttackDataModel attack) {
        position = attack.getAttackPosition();
        characterId = attack.getAttacker();
        commandType = CommandType.ATTACK;
    }

    public CommandDTO(MovementDataModel movement) {
        position = movement.getMovementVector();
        characterId = movement.getCharacterId();
        commandType = CommandType.MOVEMENT;
    }

    public Vector2D getPosition() {
        return position;
    }

    public String getCharacterId() {
        return characterId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toJson() {
        return "{" +
            "\"characterId\":\"" + characterId + "\"," +
            "\"position\":\"" +
            "{\"x\":" + position.getX() + "," +
            "\"y\":" + position.getY() + "}," +
            "\"commandType\":\"" + commandType.name() + "\"" +
            "}";
    }

    public enum CommandType {
        ATTACK,
        MOVEMENT
    }
}
