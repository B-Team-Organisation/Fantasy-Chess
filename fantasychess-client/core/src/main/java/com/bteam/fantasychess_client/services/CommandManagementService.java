package com.bteam.fantasychess_client.services;

import com.bteam.common.dto.CommandDTO;
import com.bteam.common.dto.CommandListDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bteam.fantasychess_client.Main.getWebSocketService;

public class CommandManagementService {
    private final Map<String, AttackDataModel> attacks;
    private final Map<String, MovementDataModel> movements;

    public CommandManagementService() {
        attacks = new HashMap<>();
        movements = new HashMap<>();
    }

    public void clearAll() {
        attacks.clear();
        movements.clear();
    }

    public void setCommand(String characterEntityId, AttackDataModel attack) {
        movements.remove(characterEntityId);
        attacks.put(characterEntityId, attack);
    }

    public void setCommand(String characterEntityId, MovementDataModel movement) {
        attacks.remove(characterEntityId);
        movements.put(characterEntityId, movement);
    }

    public Map<String, AttackDataModel> getAttacksCommands() {
        return attacks;
    }

    public Map<String, MovementDataModel> getMovementsCommands() {
        return movements;
    }

    public void sendCommandsToServer() {
        List<CommandDTO> list = new ArrayList<>();
        for (var movement : movements.values()) list.add(new CommandDTO(movement));
        for (var attack : attacks.values()) list.add(new CommandDTO(attack));
        CommandListDTO commandListDTO = new CommandListDTO(list);
        Packet packet = new Packet(commandListDTO, "GAME_COMMANDS");
        getWebSocketService().send(packet);
        clearAll();
    }
}
