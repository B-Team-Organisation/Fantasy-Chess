package com.bteam.fantasychess_server.client;

import com.bteam.fantasychess_server.utils.Event;
import com.bteam.fantasychess_server.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


/**
 * Wrapper class for a WebSocketSession to handle parsing
 * class data to JSON, as well as wrapping function calls
 * in {@link Event}s that can be subscribed to.
 */
public class Client {
    private final String id;
    private final WebSocketSession session;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * An {@link Event} that gets called, when a message, belonging to this client
     * was received
     */
    private final Event<String> onMessageReceivedEvent = new Event<>();
    /**
     * An {@link Event} that gets called, when the WebSocketSession has Disconnected
     */
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

    /**
     * @param payload The payload sent to the client
     * @param <T>     The type of the Payload, sent to the client
     * @return Success Result if the message was sent without errors,
     * otherwise returns a Failure result
     */
    public <T> Result<Boolean> sendMessage(T payload) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
            return Result.asSuccess(true);
        } catch (Exception e) {
            return Result.asFailure(e);
        }
    }
}
