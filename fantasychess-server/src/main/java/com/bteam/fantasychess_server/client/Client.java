package com.bteam.fantasychess_server.client;

import com.bteam.fantasychess_server.utils.Event;
import com.bteam.fantasychess_server.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class Client {
    private final String id;
    private final WebSocketSession session;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Event<String> onMessageReceivedEvent = new Event<>();
    private final Event<CloseStatus> onClientDisconnected = new Event<>();

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

    public Event<String> getOnMessageReceivedEvent() {
        return onMessageReceivedEvent;
    }

    public Event<CloseStatus> getOnClientDisconnected() {
        return onClientDisconnected;
    }

    public <T> Result<Boolean> sendMessage(T payload) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
            return Result.asSuccess(true);
        } catch (Exception e) {
            return Result.asFailure(e);
        }
    }
}
