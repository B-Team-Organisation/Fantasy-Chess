package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.*;
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

import static com.bteam.common.constants.PacketConstants.*;

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
            case PLAYER_READY:
                var dto = mapper.convertValue(data, PlayerStatusDTO.class);
                var playerId = UUID.fromString(client.getPlayer().getPlayerId());
                var isReady = Objects.equals(dto.getStatus(), PlayerStatusDTO.PLAYER_READY);
                var lobby = lobbyService.getLobbyWithPlayer(playerId);

                playerService.setPlayerStatus(playerId, isReady ?
                        Player.Status.READY : Player.Status.NOT_READY);

                lobby = lobbyService.getLobby(UUID.fromString(lobby.getLobbyId()));
                var playersInLobby = lobby.getPlayers();

                for (var player : playersInLobby) {
                    var readyPlayerId = player.getPlayerId();
                    var statusPacket = new Packet(isReady ?
                            PlayerStatusDTO.ready(readyPlayerId) :
                            PlayerStatusDTO.notReady(readyPlayerId), PLAYER_READY);
                    WebSocketService.getCurrentClientForPlayer(player).sendPacket(statusPacket);
                }

                if (playersInLobby.size() == 2 && playersInLobby.stream()
                        .allMatch(player -> player.getStatus().equals(Player.Status.READY))) {
                    var players = playersInLobby.stream().map(p -> UUID.fromString(p.getPlayerId())).toList();
                    var model = gameStateService.startNewGame(new GameSettingsModel(-1), lobby.getLobbyId(), players);
                    var dtos = model.getEntities().stream().map(CharacterEntityDTO::new).toList();

                    for (var p : playersInLobby) {
                        var playerUUID = UUID.fromString(p.getPlayerId());
                        var charactersToSend = lobbyService.getLobbyWithPlayer(playerUUID).isHost(p) ?
                                dtos.stream().map(this::invertEntityPosition).toList() : dtos;

                        var dataToSend = new GameInitDTO(charactersToSend, model.getId());
                        var packetToSend = new Packet(dataToSend, GAME_INIT);
                        WebSocketService.getCurrentClientForPlayer(p).sendPacket(packetToSend);
                    }
                }
                break;
            case PLAYER_ABANDONED:
                try {
                    var abandonPlayerId = UUID.fromString(client.getPlayer().getPlayerId());
                    var abandonedLobby = lobbyService.getLobbyWithPlayer(abandonPlayerId);

                    var game = gameStateService.getGameModelForLobby(abandonedLobby.getLobbyId());
                    gameStateService.cancelGame(UUID.fromString(game.getId()));

                    for (var player : abandonedLobby.getPlayers()) {
                        player.setStatus(Player.Status.NOT_READY);
                    }

                    abandonedLobby.removePlayer(client.getPlayer());
                    lobbyService.closeLobby(UUID.fromString(abandonedLobby.getLobbyId()), "Opponent has abandoned the game");
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;

            case PLAYER_LEAVE:
                try {
                    var player = client.getPlayer();
                    var playerID = UUID.fromString(player.getPlayerId());
                    var abandonedLobby = lobbyService.getLobbyWithPlayer(playerID);
                    abandonedLobby.removePlayer(player);

                    if (abandonedLobby.getPlayers().isEmpty()) {
                        lobbyService.closeLobby(UUID.fromString(abandonedLobby.getLobbyId()), "Noone is in the lobby");
                        break;
                    }

                    for (var p : abandonedLobby.getPlayers()) {
                        var dataToSend = new PlayerInfoDTO(player.getPlayerId(), player.getUsername());
                        var packetToSend = new Packet(dataToSend, PLAYER_LEAVE);
                        WebSocketService.getCurrentClientForPlayer(p).sendPacket(packetToSend);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case PLAYER_INFO:
                try {
                    var playerInfoRequest = mapper.convertValue(data, PlayerInfoDTO.class);
                    var requestedInfoId = UUID.fromString(playerInfoRequest.getPlayerId());
                    var player = playerService.getPlayer(requestedInfoId);
                    if (player == null) return;
                    var playerInfoDto = new PlayerInfoDTO(requestedInfoId.toString(), player.getUsername());
                    var playerInfoResult = new Packet(playerInfoDto, PLAYER_INFO);
                    client.sendPacket(playerInfoResult);
                } catch (Exception e) {
                    System.out.println(e);
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
