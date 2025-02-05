package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.*;
import com.bteam.common.models.TurnResult;
import com.bteam.common.utils.PairNoOrder;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.data.mapper.CommandMapper;
import com.bteam.fantasychess_server.service.GameStateService;
import com.bteam.fantasychess_server.service.LobbyService;
import com.bteam.fantasychess_server.service.PlayerService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.UUID;

import static com.bteam.common.constants.PacketConstants.GAME_COMMANDS;
import static com.bteam.common.constants.PacketConstants.GAME_TURN_RESULT;

public class GamePacketHandler implements PacketHandler {
    private final String packetPattern = "GAME_";
    private final GameStateService gameStateService;
    private final WebSocketService webSocketService;
    private final PlayerService playerService;
    private final LobbyService lobbyService;


    public GamePacketHandler(GameStateService gameStateService,
                             WebSocketService webSocketService,
                             PlayerService playerService,
                             LobbyService lobbyService) {
        this.gameStateService = gameStateService;
        this.webSocketService = webSocketService;
        this.playerService = playerService;
        this.lobbyService = lobbyService;
    }

    @Override
    public void handle(Client client, String id, String packet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var tree = mapper.readTree(packet);
        var data = tree.get("data");

        switch (id) {
            case GAME_COMMANDS:
                var commands = mapper.convertValue(data, CommandListDTO.class);
                var attacks = CommandMapper.attacksFromDTO(commands);
                var movements = CommandMapper.movementsFromDTO(commands);
                var uuid = UUID.fromString(client.getPlayer().getPlayerId());
                var gameId = UUID.fromString(commands.getGameId());

                gameStateService.setPlayerCommands(uuid, gameId, movements, attacks);

                var game = gameStateService.getGame(gameId);
                if (game.getCommands().size() < 2) break;

                var playerUUID = UUID.fromString(client.getPlayer().getPlayerId());
                var players = lobbyService.getLobbyWithPlayer(playerUUID).getPlayers();
                var result = gameStateService.processMoves(gameId, game.getCommands());

                for (int i = 0; i < players.size(); i++) {
                    var p = players.get(i);
                    Packet packetToSend;
                    TurnResult turnResult = result.getFirst();
                    if (lobbyService.getLobby(UUID.fromString(game.getLobbyId())).isHost(p))
                        turnResult = gameStateService.invertResult(result.getFirst());

                    var updatedCharactersDTO = turnResult.getUpdatedCharacters().stream().map(CharacterEntityDTO::new).toList();

                    var validCommands = new ArrayList<CommandDTO>();
                    validCommands.addAll(turnResult.getValidAttacks().stream().map(CommandDTO::new).toList());
                    validCommands.addAll(turnResult.getValidMoves().stream().map(CommandDTO::new).toList());
                    var validCommandsDto = new CommandListDTO(validCommands, gameId.toString());

                    var rejectedCommands = new ArrayList<PairNoOrder<CommandDTO, CommandDTO>>();
                    if (turnResult.getMovementConflicts() != null) {
                        var mappedCommands = turnResult
                                .getMovementConflicts()
                                .stream()
                                .map(pair ->
                                        new PairNoOrder<>(new CommandDTO(pair.getFirst()),
                                                new CommandDTO(pair.getSecond())))
                                .toList();
                        rejectedCommands.addAll(mappedCommands);
                    }

                    var dto = new TurnResultDTO(updatedCharactersDTO, rejectedCommands,
                            validCommandsDto, turnResult.getWinner());
                    packetToSend = new Packet(dto, GAME_TURN_RESULT);
                    System.out.println(dto.toJson());
                    WebSocketService.getCurrentClientForPlayer(p).sendPacket(packetToSend);
                }

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
