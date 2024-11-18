package com.bteam.fantasychess_client.networking;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;

import java.util.function.Consumer;

public class WebSocketService {
    public WebSocketService(String address, int port) {
        WebSocket socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
        socket.setSendGracefully(true);
        socket.addListener(new WebSocketClient());
        socket.connect();
    }

    public <T> void AddPacketHandler(Consumer<T> packetHandler){

        //packetHandler.accept(a);
    }
}
