package com.bteam.fantasychess_server.service;

import com.bteam.common.dto.LobbyClosedDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.StatusDTO;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.client.interceptors.LobbyPacketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static com.bteam.common.constants.PacketConstants.CONNECTED_STATUS;

/**
 * {@link Service} Singleton that provides a Map of all connected clients and methods
 * to send Messages to said clients. As well as handling routing received {@link TextMessage}s
 * to the appropriate clients
 *
 * @author Marc
 */
@Service
public class WebSocketService {
    private final Map<String, Client> clients = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<PacketHandler> packetHandlers = new ArrayList<>();
    private final LobbyService lobbyService;

    public WebSocketService(@Autowired LobbyService lobbyService) {
        addPacketHandler(new LobbyPacketHandler(lobbyService));
        this.lobbyService = lobbyService;
    }

    public WebSocketService(@Autowired LobbyService lobbyService) {
        addPacketHandler(new LobbyPacketHandler(lobbyService));
    }

    public ImmutableMap<String, Client> getClients() {
        return ImmutableMap.copyOf(clients);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public WebSocketService addPacketHandler(PacketHandler packetHandler) {
        packetHandlers.add(packetHandler);
        return this;
    }

    public Client registerSession(WebSocketSession session, Player player) {
        var sessionID = session.getId();
        var client = new Client(sessionID, session, player);
        clients.put(sessionID, client);
        var packet = new Packet(new StatusDTO("CONNECTED"), CONNECTED_STATUS);
        client.getOnClientDisconnected().addListener(status -> onClientDisconnect(client));
        sendToClient(sessionID, packet);
        return client;
    }

    public Client removeSession(String sessionID, CloseStatus status) {
        clients.get(sessionID).getSession().isOpen();
        clients.get(sessionID).getOnClientDisconnected().invoke(status);
        return clients.remove(sessionID);
    }

    public void sendToClient(String id, Packet packet) {
        clients.get(id).sendPacket(packet);
    }

    private Client getClientBySession(String sessionID) {
        return clients.get(sessionID);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            var packet = mapper.readTree(payload);
            var client = getClientBySession(session.getId());
            String packetId = packet.get("id").asText();
            getHandlerForId(packetId).handle(client, packetId, message.getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PacketHandler getHandlerForId(String packetId) {
        for (PacketHandler packetInterceptor : packetHandlers) {
            if (packetId.contains(packetInterceptor.getPacketPattern().replace("*", "")))
                return packetInterceptor;
        }
        return null;
    }

    public Client getCurrentClientForPlayer(Player player) {
        return clients.values().stream().filter(
                client -> client.getPlayer().getPlayerId().equals(player.getPlayerId()))
            .findFirst().orElse(null);
    }

    public void onClientDisconnect(Client client) {
        var playerUUID = UUID.fromString(client.getPlayer().getPlayerId());
        var hostedLobbies = lobbyService.getHostedLobbies(playerUUID);
        for (var lobby : hostedLobbies) {
            var players = lobby.getPlayers();
            var packet = new Packet(new LobbyClosedDTO(lobby.getLobbyId(), "Host has Disconnected!"), "LOBBY_CLOSED");
            for (var player : players) {
                var playerClient = getCurrentClientForPlayer(player);
                if (playerClient != null) {
                    playerClient.sendPacket(packet);
                }
            }
            lobbyService.removeLobby(UUID.fromString(lobby.getLobbyId()));
        }
    }

    public void onSessionClose(WebSocketSession session, CloseStatus status) {
        clients.get(session.getId()).getOnClientDisconnected().invoke(status);
    }

}
