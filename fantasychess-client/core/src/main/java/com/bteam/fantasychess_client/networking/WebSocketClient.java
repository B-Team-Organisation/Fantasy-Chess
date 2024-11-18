package com.bteam.fantasychess_client.networking;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;

public class WebSocketClient implements WebSocketListener {
    @Override
    public boolean onOpen(WebSocket webSocket) {
        return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        return false;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        return false;
    }
}
