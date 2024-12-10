package com.bteam.common.dto;

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
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"updatedCharacters\":[");
        for (CharacterEntityDTO character : getUpdatedCharacters()) {
            sb.append(character.toJson());
            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"conflictCommands\":");
        for (PairNoOrder<CommandDTO, CommandDTO> conflictCommand : getConflictCommands()) {
            sb.append("{\"first\":").append(conflictCommand.getFirst().toJson());
            sb.append(",\"second\":").append(conflictCommand.getSecond().toJson());
            sb.append("},");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("],\"validCommands\":").append(getValidCommands().toJson());
        sb.append("}");
        return "";
    }
}
