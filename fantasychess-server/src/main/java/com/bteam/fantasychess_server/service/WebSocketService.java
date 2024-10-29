package com.bteam.fantasychess_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {
    private final Map<String,WebSocketSession> sessions = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void registerSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    public <T> void sendToClient(String id, T payload) throws JsonProcessingException {
        String payloadString = mapper.writeValueAsString(payload);
        try{
            sessions.get(id).sendMessage(new TextMessage(payloadString));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
