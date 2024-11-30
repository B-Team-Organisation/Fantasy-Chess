package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerReadyDTO;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.service.LobbyService;
import com.bteam.fantasychess_server.service.PlayerService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;
import java.util.UUID;

public class PlayerPacketHandler implements PacketHandler {
    private final PlayerService playerService;
    private final LobbyService lobbyService;
    private final WebSocketService webSocketService;

    public PlayerPacketHandler(PlayerService playerService,
                               LobbyService lobbyService,
                               WebSocketService webSocketService) {
        this.playerService = playerService;
        this.lobbyService = lobbyService;
        this.webSocketService = webSocketService;
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
                playerService.setPlayerStatus(playerId, isReady ?
                    Player.Status.READY : Player.Status.NOT_READY);
                var playersToNotify = lobbyService.lobbyWithPlayer(playerId).getPlayers();
                for (var player : playersToNotify) {
                    var readyPlayerId = player.getPlayerId();
                    var statusPacket = new Packet(isReady ?
                        PlayerReadyDTO.ready(readyPlayerId) :
                        PlayerReadyDTO.notReady(readyPlayerId), "PLAYER_READY");
                    webSocketService.getCurrentClientForPlayer(player).sendPacket(statusPacket);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public String getPacketPattern() {
        return "PLAYER_";
    }
}
