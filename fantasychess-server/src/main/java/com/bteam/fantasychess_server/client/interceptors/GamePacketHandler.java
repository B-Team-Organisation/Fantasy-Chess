package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.CommandListDTO;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.data.mapper.CommandMapper;
import com.bteam.fantasychess_server.service.GameStateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.UUID;

import static java.lang.System.out;

public class GamePacketHandler implements PacketHandler {
    private final String packetPattern = "GAME_";

    GameStateService gameStateService;

    public GamePacketHandler(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

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
                var gameId = UUID.fromString(commands.getGameId());
                gameStateService.setPlayerMoves(client.getPlayerId(), gameId, movements, attacks);
                var game = gameStateService.getGame(gameId);
                if (game.getCommands().size() == 2) {
                    var combinedMoves = new ArrayList<MovementDataModel>();
                    var combinedAttacks = new ArrayList<AttackDataModel>();
                    game.getCommands().forEach((s, v) -> {
                        combinedMoves.addAll(v.getSecond());
                        combinedAttacks.addAll(v.getFirst());
                    });
                    var result = gameStateService.processMoves(gameId, combinedMoves, combinedAttacks);
                    
                }

                break;
            case "GAME_INITIAL_PLACEMENTS":

                break;
            default:
                out.println("Unknown packet: " + packet);
                break;
        }
    }

    @Override
    public String getPacketPattern() {
        return packetPattern;
    }
}
