package com.bteam.fantasychess_server.data.mapper;

import com.bteam.common.dto.CommandDTO;
import com.bteam.common.dto.CommandListDTO;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;

import java.util.List;

public final class CommandMapper {
    public static List<AttackDataModel> attacksFromDTO(CommandListDTO commandListDTO) {
        return commandListDTO.getCommands().stream()
            .filter(commandDTO -> commandDTO.getCommandType() == CommandDTO.CommandType.ATTACK)
            .map(commandDTO -> new AttackDataModel(commandDTO.getPosition(), commandDTO.getCharacterId()))
            .toList();
    }

    public static List<MovementDataModel> movementsFromDTO(CommandListDTO commandListDTO) {
        return commandListDTO.getCommands().stream()
            .filter(commandDTO -> commandDTO.getCommandType() == CommandDTO.CommandType.MOVEMENT)
            .map(commandDTO -> new MovementDataModel(commandDTO.getCharacterId(), commandDTO.getPosition()))
            .toList();
    }
}
