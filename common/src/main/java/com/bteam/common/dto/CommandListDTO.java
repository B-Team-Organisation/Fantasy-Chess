package com.bteam.common.dto;

import java.util.ArrayList;
import java.util.List;

public class CommandListDTO implements JsonDTO {
    List<CommandDTO> commands;

    public CommandListDTO(List<CommandDTO> commands) {
        this.commands = commands;
    }

    public CommandListDTO() {
        commands = new ArrayList<>();
    }

    public List<CommandDTO> getCommands() {
        return commands;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"commands\": [");
        for (CommandDTO commandDTO : commands) sb.append(commandDTO.toJson()).append(",");
        if (!commands.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }
}
