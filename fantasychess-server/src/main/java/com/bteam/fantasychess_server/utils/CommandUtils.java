package com.bteam.fantasychess_server.utils;

import com.bteam.common.dto.CommandDTO;
import com.bteam.common.models.Vector2D;

public final class CommandUtils {
    public static CommandDTO invertCommandDto(CommandDTO commandDTO) {
        return new CommandDTO(
            invertVector(commandDTO.getPosition()),
            commandDTO.getCharacterId(),
            commandDTO.getCommandType());
    }

    public static Vector2D invertVector(Vector2D vector) {
        return new Vector2D(8 - vector.getX(), 8 - vector.getY());
    }
}
