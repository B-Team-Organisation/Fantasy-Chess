package com.bteam.fantasychess_server.client;

import com.bteam.fantasychess_server.config.TextWebSocketHandlerExt;
import com.bteam.fantasychess_server.utils.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class Client {
    private final String id;
    private final WebSocketSession session;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Event<String> onMessageRecievedEvent = new Event<>();

    public Client(String id, WebSocketSession session) {
        this.id = id;
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public Event<String> getOnMessageRecievedEvent() {
        return onMessageRecievedEvent;
    }

    public <T> void sendMessage(T payload) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
