package com.bteam.fantasychess_server.client;

import com.bteam.common.dto.Packet;
import com.bteam.common.models.Player;
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
    private final Player player;
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

    public Client(String id, WebSocketSession session, Player player) {
        this.id = id;
        this.session = session;
        this.player = player;
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

    public Result<Boolean> sendPacket(Packet packet) {
        return sendMessage(packet);
    }

    /**
     * @param packet The packet sent to the client
     * @return Success Result if the message was sent without errors,
     * otherwise returns a Failure result
     */
    public Result<Boolean> sendPacket(Packet packet) {
        try {
            session.sendMessage(new TextMessage(packet.toString()));
            return Result.asSuccess(true);
        } catch (Exception e) {
            return Result.asFailure(e);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
