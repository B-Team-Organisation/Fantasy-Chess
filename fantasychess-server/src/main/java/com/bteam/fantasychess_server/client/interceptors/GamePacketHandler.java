package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.CommandListDTO;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.data.mapper.CommandMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GamePacketHandler implements PacketHandler {
    private final String packetPattern = "GAME_";

    @Override
    public void handle(Client client, String id, String packet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var tree = mapper.readTree(packet);
        var data = tree.get("data");

        switch (id) {
            case "GAME_COMMANDS":
                var commands = mapper.convertValue(data, CommandListDTO.class);
                var attacks = CommandMapper.attacksFromDTO(commands);
                var movements = CommandMapper.movementsFromDTO(commands);
                System.out.println(attacks.size());
                System.out.println(movements.size());
                break;
            default:
                System.out.println("Unknown packet: " + packet);
                break;
        }
    }

    @Override
    public String getPacketPattern() {
        return packetPattern;
    }
}
