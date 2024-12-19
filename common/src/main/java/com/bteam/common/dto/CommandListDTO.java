package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

public class CommandListDTO implements JsonDTO {
    List<CommandDTO> commands;
    String gameId;

    public CommandListDTO(List<CommandDTO> commands, String gameId) {
        this.commands = commands;
        this.gameId = gameId;
    }

    public CommandListDTO() {
        commands = new ArrayList<>();
        gameId = "";
    }

    public String getGameId() {
        return gameId;
    }

    public List<CommandDTO> getCommands() {
        return commands;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeList("commands", getCommands())
            .and().writeKeyValue("gameId", getGameId()).toString();


        /*StringBuilder sb = new StringBuilder();
        sb.append("{\"commands\": [");
        for (CommandDTO commandDTO : getCommands()) sb.append(commandDTO.toJson()).append(",");
        if (!getCommands().isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("],");
        sb.append("\"gameId\": \"").append(getGameId()).append("\"");
        sb.append("}");
        return sb.toString();*/
    }
}
