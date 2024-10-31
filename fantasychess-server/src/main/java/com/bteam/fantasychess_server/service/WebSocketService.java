package com.bteam.fantasychess_server.service;

import com.bteam.fantasychess_server.client.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {
    private final Map<String, Client> clients = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void registerSession(WebSocketSession session) {
        System.out.println("registering session");
        var sessionID = session.getId();
        clients.put(sessionID, new Client(sessionID, session));
    }

    public <T> void sendToClient(String id, T payload) {
        clients.get(id).sendMessage(payload);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        clients.get(session.getId()).getOnMessageRecievedEvent().Invoke(payload);
    }
}
