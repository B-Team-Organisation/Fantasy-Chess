package com.bteam.fantasychess_client.networking;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WebSocketService {
    Map<String,Consumer> handlers = new HashMap<>();
    WebSocket socket;

    public WebSocketService(String address, int port) {
        WebSocket socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
        socket.setSendGracefully(true);
        socket.addListener(new WebSocketClient());
        this.socket = socket;
    }

    public WebSocketState GetState() {
        return socket.getState();
    }

    public void Connect() {
        socket.connect();
    }

    public <T> void AddPacketHandler(String id, Consumer<T> packetHandler){
        handlers.put(id, packetHandler);
    }

    public <T> void HandlePacket(String id, T packet){
        handlers.get(id).accept(packet);
    }
}
