package com.bteam.fantasychess_server.service;

import com.bteam.fantasychess_server.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

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

    public ImmutableMap<String, Client> getClients() {
        return ImmutableMap.copyOf(clients);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public Client registerSession(WebSocketSession session) {
        var sessionID = session.getId();
        return clients.put(sessionID, new Client(sessionID, session));
    }

    public Client removeSession(String sessionID, CloseStatus status) {
        clients.get(sessionID).getSession().isOpen();
        clients.get(sessionID).getOnClientDisconnected().invoke(status);
        return clients.remove(sessionID);
    }

    public <T> void sendToClient(String id, T payload) {
        clients.get(id).sendMessage(payload);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        clients.get(session.getId()).getOnMessageReceivedEvent().invoke(payload);
    }
}
