package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;
import com.bteam.common.utils.PairNoOrder;

import java.util.List;

public class TurnResultDTO implements JsonDTO {
    private final List<CharacterEntityDTO> updatedCharacters;
    private final List<PairNoOrder<CommandDTO, CommandDTO>> conflictCommands;
    private final CommandListDTO validCommands;
    private final String winner;

    public TurnResultDTO(List<CharacterEntityDTO> updatedCharacters, List<PairNoOrder<CommandDTO, CommandDTO>> conflictCommands, CommandListDTO validCommands, String winner) {
        this.updatedCharacters = updatedCharacters;
        this.conflictCommands = conflictCommands;
        this.validCommands = validCommands;
        this.winner = winner;
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

    public String getWinner() {
        return winner;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeList("updatedCharacters", getUpdatedCharacters())
            .and().writeList("conflictCommands", getConflictCommands())
            .and().writeKeyValue("validCommands", getValidCommands())
            .and().writeKeyValue("winner",getWinner())
            .toString();
    }
}
