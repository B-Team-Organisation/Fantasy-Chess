package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.utils.Json;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketState;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WebSocketService {
    Map<String, PacketHandler> listeners = new HashMap<>();
    WebSocket webSocket;
    private final Json json = new Json();

    public WebSocketService(String address, int port, WebSocketListener listener) {
        WebSocket socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
        socket.setSendGracefully(true);
        socket.addListener(listener);
        this.webSocket = socket;
    }

    public WebSocketState getState() {
        return webSocket.getState();
    }

    public void connect() {
        webSocket.connect();
    }

    public <T> void addPacketHandler(String id, PacketHandler packetHandler){
        listeners.put(id, packetHandler);
    }

    public void removePacketHandler(String id){
        listeners.remove(id);
    }

    public void handlePacket(String packet){
        handlePacket(json.fromJson(Packet.class, packet));
    }

    public void handlePacket(Packet packet){
        listeners.get(packet.id).handle(packet);
    }
}
