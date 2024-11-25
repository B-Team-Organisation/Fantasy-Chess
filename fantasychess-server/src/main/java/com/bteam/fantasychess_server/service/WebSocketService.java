package com.bteam.fantasychess_server.service;

import com.bteam.common.dto.Packet;
import com.bteam.common.dto.StatusDTO;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
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
        var client = clients.put(sessionID, new Client(sessionID, session,player));
        var packet = new Packet(new StatusDTO("CONNECTED"), CONNECTED_STATUS);
        sendToClient(sessionID, packet);
        return client;
    }

    public Client removeSession(String sessionID, CloseStatus status) {
        clients.get(sessionID).getSession().isOpen();
        clients.get(sessionID).getOnClientDisconnected().invoke(status);
        return clients.remove(sessionID);
    }

    public <T> void sendToClient(String id, T payload) {
        clients.get(id).sendMessage(payload);
    }

    private Client getClientBySession(String sessionID) {
        return clients.get(sessionID);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try{
            var packet = mapper.readValue(payload, Packet.class);
            var client = getClientBySession(session.getId());
            getHandlerForId(packet.getId()).handle(client,packet.getId(),message.getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PacketHandler getHandlerForId(String packetId){
        for (PacketHandler packetInterceptor : packetHandlers) {
            if (packetId.contains(packetInterceptor.getPacketPattern().replace("*", "")))
                return packetInterceptor;
        }
        return null;
    }
}
