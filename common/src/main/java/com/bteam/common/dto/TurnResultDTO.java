package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;
import com.bteam.common.utils.PairNoOrder;

import java.util.List;

public class TurnResultDTO implements JsonDTO {
    private final List<CharacterEntityDTO> updatedCharacters;
    private final List<PairNoOrder<CommandDTO, CommandDTO>> conflictCommands;
    private final CommandListDTO validCommands;

    public TurnResultDTO(List<CharacterEntityDTO> updatedCharacters, List<PairNoOrder<CommandDTO, CommandDTO>> conflictCommands, CommandListDTO validCommands) {
        this.updatedCharacters = updatedCharacters;
        this.conflictCommands = conflictCommands;
        this.validCommands = validCommands;
    }


    public List<CharacterEntityDTO> getUpdatedCharacters() {
        return updatedCharacters;
    }

    public List<PairNoOrder<CommandDTO, CommandDTO>> getConflictCommands() {
        return conflictCommands;
    }

    public CommandListDTO getValidCommands() {
        return validCommands;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeList("updatedCharacters", getUpdatedCharacters())
            .and().writeList("conflictComamnds", getConflictCommands())
            .and().writeKeyValue("validCommands", getValidCommands()).toString();
    }
}
