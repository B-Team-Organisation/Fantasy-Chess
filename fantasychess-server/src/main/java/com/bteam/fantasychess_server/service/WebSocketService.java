package com.bteam.fantasychess_server.service;

import com.bteam.common.dto.Packet;
import com.bteam.common.dto.StatusDTO;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.client.interceptors.GamePacketHandler;
import com.bteam.fantasychess_server.client.interceptors.LobbyPacketHandler;
import com.bteam.fantasychess_server.client.interceptors.PlayerPacketHandler;
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
    private static final Map<String, Client> clients = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<PacketHandler> packetHandlers = new ArrayList<>();
    private final LobbyService lobbyService;

    public WebSocketService(@Autowired LobbyService lobbyService,
                            @Autowired PlayerService playerService,
                            @Autowired GameStateService gameStateService) {
        addPacketHandler(new LobbyPacketHandler(lobbyService, this));
        addPacketHandler(new PlayerPacketHandler(playerService, lobbyService, this, gameStateService));
        addPacketHandler(new GamePacketHandler(gameStateService, this, playerService, lobbyService));
        this.lobbyService = lobbyService;
    }

    /**
     * Retrieves the currently connected client for a specific player.
     *
     * @param player the player to search for
     * @return the Client object associated with the player, or null if not found
     */
    public static Client getCurrentClientForPlayer(Player player) {
        return clients.values().stream().filter(
                        client -> client.getPlayer().getPlayerId().equals(player.getPlayerId()))
                .findFirst().orElse(null);
    }

    /**
     * Retrieves an immutable map of currently connected clients.
     *
     * @return an immutable map of session IDs to Client objects
     */
    public ImmutableMap<String, Client> getClients() {
        return ImmutableMap.copyOf(clients);
    }

    /**
     * Retrieves the ObjectMapper used for JSON operations.
     *
     * @return the ObjectMapper instance
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Adds a packet handler to the list of handlers.
     *
     * @param packetHandler the packet handler to add
     */
    public void addPacketHandler(PacketHandler packetHandler) {
        packetHandlers.add(packetHandler);
    }

    /**
     * Registers a WebSocket session and associates it with a player.
     *
     * @param session the WebSocket session to register
     * @param player  the player associated with the session
     * @return the newly created Client object
     */
    public Client registerSession(WebSocketSession session, Player player) {
        var sessionID = session.getId();
        var client = new Client(sessionID, session, player);
        clients.put(sessionID, client);
        var packet = new Packet(new StatusDTO("CONNECTED"), CONNECTED_STATUS);
        client.getOnClientDisconnected().addListener(status -> onClientDisconnect(client));
        sendToClient(sessionID, packet);
        return client;
    }

    /**
     * Removes a WebSocket session and triggers client disconnection logic.
     *
     * @param sessionID the ID of the session to remove
     * @param status    the close status of the session
     * @return the removed Client object, or null if no client was found
     */
    public Client removeSession(String sessionID, CloseStatus status) {
        clients.get(sessionID).getOnClientDisconnected().invoke(status);
        return clients.remove(sessionID);
    }

    /**
     * Sends a packet to a specific client by session ID.
     *
     * @param id     the session ID of the target client
     * @param packet the packet to send
     */
    public void sendToClient(String id, Packet packet) {
        clients.get(id).sendPacket(packet);
    }

    /**
     * Retrieves a client by their WebSocket session ID.
     *
     * @param sessionID the session ID to search for
     * @return the Client object, or null if not found
     */
    private Client getClientBySession(String sessionID) {
        return clients.get(sessionID);
    }

    /**
     * Handles incoming text messages from WebSocket clients.
     *
     * @param session the WebSocket session that sent the message
     * @param message the text message received
     */
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        System.out.println("Recieved Message:\n" + payload);
        try {
            var packet = mapper.readTree(payload);
            var client = getClientBySession(session.getId());
            String packetId = packet.get("id").asText();
            getHandlerForId(packetId).handle(client, packetId, message.getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the appropriate packet handler for a given packet ID.
     *
     * @param packetId the packet ID to match
     * @return the corresponding PacketHandler, or null if none matches
     */
    private PacketHandler getHandlerForId(String packetId) {
        for (PacketHandler packetInterceptor : packetHandlers) {
            if (packetId.contains(packetInterceptor.getPacketPattern().replace("*", "")))
                return packetInterceptor;
        }
        return null;
    }

    /**
     * Handles the disconnection logic for a client.
     *
     * @param client the client that disconnected
     */
    public void onClientDisconnect(Client client) {
        var playerUUID = UUID.fromString(client.getPlayer().getPlayerId());
        var hostedLobbies = lobbyService.getHostedLobbies(playerUUID);

        for (var hostedLobby : hostedLobbies) {
            lobbyService.closeLobby(UUID.fromString(hostedLobby.getLobbyId()), "Host has disconnected");
        }
    }

    /**
     * Handles session closure events.
     *
     * @param session the WebSocket session being closed
     * @param status  the status of the closure
     */
    public void onSessionClose(WebSocketSession session, CloseStatus status) {
        clients.get(session.getId()).getOnClientDisconnected().invoke(status);
    }

}

