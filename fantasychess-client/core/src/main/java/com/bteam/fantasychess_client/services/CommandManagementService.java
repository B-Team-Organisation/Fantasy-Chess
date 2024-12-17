package com.bteam.fantasychess_client.services;

import com.bteam.common.dto.CommandDTO;
import com.bteam.common.dto.CommandListDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * Command Mangement Service to store all commands the player currently wants
 * it's charcters to make
 *
 * @author Marc Lukas
 */
public class CommandManagementService {
    private final Map<String, AttackDataModel> attacks;
    private final Map<String, MovementDataModel> movements;

    private final Map<AttackDataModel,Map<Vector2D,Integer>> commandDamageMappings;

    /**
     * Constructor
     */
    public CommandManagementService() {
        attacks = new HashMap<>();
        movements = new HashMap<>();
        commandDamageMappings = new HashMap<>();
    }

    /**
     * Clears all content
     * <p>
     * Resets service to original state.
     */
    public void clearAll() {
        attacks.clear();
        movements.clear();
        commandDamageMappings.clear();
    }

    /**
     * Sets the attack command
     *
     * @param attack {@link AttackDataModel} of the attack command to set
     * @param damageValues {@link Map} containing the attacked position mapped to its damage
     */
    public void setCommand(AttackDataModel attack, Map<Vector2D, Integer> damageValues) {
        movements.remove(attack.getAttacker());

        AttackDataModel oldAttack = attacks.get(attack.getAttacker());
        commandDamageMappings.remove(oldAttack);
        commandDamageMappings.put(attack,damageValues);

        attacks.put(attack.getAttacker(), attack);
    }

    /**
     * Sets the movement command
     *
     * @param movement {@link MovementDataModel} of the movement command to set
     */
    public void setCommand(MovementDataModel movement) {
        attacks.remove(movement.getCharacterId());
        movements.put(movement.getCharacterId(), movement);
    }

    /**
     * Getter for attack commands
     *
     * @return {@link Map} that maps Character ID to {@link AttackDataModel}
     */
    public Map<String, AttackDataModel> getAttacksCommands() {
        return attacks;
    }

    /**
     * Getter for movement commands
     *
     * @return {@link Map} that maps Character ID to {@link MovementDataModel}
     */
    public Map<String, MovementDataModel> getMovementsCommands() {
        return movements;
    }

    /**
     * Getter for command damage mappings
     */
    public Map<AttackDataModel, Map<Vector2D, Integer>> getCommandDamageMappings() {
        return commandDamageMappings;
    }

    /**
     * Sends all commands to the server
     */
    public void sendCommandsToServer() {
        List<CommandDTO> list = new ArrayList<>();
        for (var movement : movements.values()) list.add(new CommandDTO(movement));
        for (var attack : attacks.values()) list.add(new CommandDTO(attack));
        CommandListDTO commandListDTO = new CommandListDTO(list);
        Packet packet = new Packet(commandListDTO, "GAME_COMMANDS");
        getWebSocketService().send(packet);
    }
}
