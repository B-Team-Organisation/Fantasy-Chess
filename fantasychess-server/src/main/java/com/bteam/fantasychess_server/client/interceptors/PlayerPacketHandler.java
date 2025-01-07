package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.CharacterEntityDTO;
import com.bteam.common.dto.GameInitDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerReadyDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.GameSettingsModel;
import com.bteam.common.models.Player;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.service.GameStateService;
import com.bteam.fantasychess_server.service.LobbyService;
import com.bteam.fantasychess_server.service.PlayerService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

/**
 * Packet handler for any player related data, such as setting the player's status
 * (or receiving player details)
 *
 * @author Marc
 */

public class PlayerPacketHandler implements PacketHandler {
    private final PlayerService playerService;
    private final LobbyService lobbyService;
    private final WebSocketService webSocketService;
    private final GameStateService gameStateService;

    public PlayerPacketHandler(PlayerService playerService,
                               LobbyService lobbyService,
                               WebSocketService webSocketService,
                               GameStateService gameStateService) {
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.webSocketService = webSocketService;
        this.gameStateService = gameStateService;
    }

    @Override
    public void handle(Client client, String id, String packet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var tree = mapper.readTree(packet);
        var data = tree.get("data");

        switch (id) {
            case "PLAYER_READY":
                var dto = mapper.convertValue(data, PlayerReadyDTO.class);
                var playerId = UUID.fromString(client.getPlayer().getPlayerId());
                var isReady = Objects.equals(dto.getStatus(), PlayerReadyDTO.PLAYER_READY);
                var lobby = lobbyService.lobbyWithPlayer(playerId);
                playerService.setPlayerStatus(playerId, isReady ?
                    Player.Status.READY : Player.Status.NOT_READY);
                var playersToNotify = lobby.getPlayers();
                for (var player : playersToNotify) {
                    var readyPlayerId = player.getPlayerId();
                    var statusPacket = new Packet(isReady ?
                        PlayerReadyDTO.ready(readyPlayerId) :
                        PlayerReadyDTO.notReady(readyPlayerId), "PLAYER_READY");
                    webSocketService.getCurrentClientForPlayer(player).sendPacket(statusPacket);
                }
                if (lobby.getPlayers().size() == 2) {
                    var players = lobby.getPlayers().stream().map(p -> UUID.fromString(p.getPlayerId())).toList();
                    var model = gameStateService.startNewGame(new GameSettingsModel(-1), lobby.getLobbyId(), players);
                    var dtos = model.getEntities().stream().map(CharacterEntityDTO::new).toList();

                    for (var p : lobby.getPlayers()) {
                        var playerUUID = UUID.fromString(p.getPlayerId());
                        var charactersToSend = lobbyService.lobbyWithPlayer(playerUUID).isHost(p) ?
                            dtos.stream().map(this::invertEntityPosition).toList() : dtos;

                        var dataToSend = new GameInitDTO(charactersToSend, model.getId());
                        var packetToSend = new Packet(dataToSend, "GAME_INIT");
                        webSocketService
                            .getCurrentClientForPlayer(p)
                            .sendPacket(packetToSend);
                    }
                }
                break;
            default:
                break;
        }
    }

    private CharacterEntityDTO invertEntityPosition(CharacterEntityDTO entityDto) {
        var character = entityDto.getCharacter();
        var invertedPosition = new Vector2D(
            8 - character.getPosition().getX(),
            8 - character.getPosition().getY());
        var invertedEntity = new CharacterEntity(
            character.getCharacterBaseModel(),
            character.getId(),
            character.getHealth(),
            invertedPosition,
            character.getPlayerId());
        return new CharacterEntityDTO(invertedEntity);
    }

    @Override
    public String getPacketPattern() {
        return "PLAYER_";
    }
}
