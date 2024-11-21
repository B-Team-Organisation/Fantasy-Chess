package com.bteam.fantasychess_client.networking;

import com.bteam.common.utils.Event;
import com.bteam.fantasychess_client.Main;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;

import java.util.logging.Level;

public class WebSocketClient implements WebSocketListener {
    public final Event<WebSocket> onOpenEvent = new Event<>();
    public final Event<String> onCloseEvent = new Event<>();
    public final Event<Throwable> onErrorEvent = new Event<>();
    public final Event<String> onTextEvent = new Event<>();

    @Override
    public boolean onOpen(WebSocket webSocket) {
        onOpenEvent.invoke(webSocket);
        return true;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        onCloseEvent.invoke(closeCode + ":" + reason);
        Main.getLogger().log(Level.SEVERE, reason + " | " + closeCode);
        return true;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        onTextEvent.invoke(packet);
        return true;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        onTextEvent.invoke(new String(packet));
        return true;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        onErrorEvent.invoke(error);
        return true;
    }
}
