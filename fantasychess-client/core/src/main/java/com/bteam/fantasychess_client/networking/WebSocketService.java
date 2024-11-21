package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.StatusDTO;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.data.errors.UnhandledPacketException;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketState;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.bteam.common.constants.PacketConstants.CONNECTED_STATUS;

public class WebSocketService {
    private final Map<String, PacketHandler> listeners = new HashMap<>();
    private final Json json = new Json();

    WebSocket webSocket;
    WebSocketClient client;

    public WebSocketService(String address, WebSocketClient listener) {
        webSocket = WebSockets.newSocket(address);
        webSocket.setSendGracefully(true);
        client = listener;
        webSocket.addListener(client);
        client.onTextEvent.addListener(this::handlePacket);

        client.onOpenEvent.addListener(payload ->
            Main.getLogger().log(Level.SEVERE, "Client has connected"));

        addPacketHandler(CONNECTED_STATUS, packet -> {
            Main.getLogger().log(Level.SEVERE, "It is now forwarded: " + packet);
        });
    }

    public WebSocketState getState() {
        return webSocket.getState();
    }

    public void connect() {
        Main.getLogger().log(Level.SEVERE, "Connecting to server...");
        try{
            webSocket.connect();
        } catch (Exception e){
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public void addPacketHandler(String id, PacketHandler packetHandler){
        listeners.put(id, packetHandler);
    }

    public void removePacketHandler(String id){
        listeners.remove(id);
    }

    public void handlePacket(String packet){
        Main.getLogger().log(Level.SEVERE, "Received packet: " + packet);
        JsonValue fromJson = new JsonReader().parse(packet);
        var id = fromJson.getString("id");
        try{
            Main.getLogger().log(Level.SEVERE, "Deserialized with id: " +id);
            if (!listeners.containsKey(id))
                throw new UnhandledPacketException("Packet with id: " + id +
                    " has no registered packet Handlers!\nIs your client version out of sync?");
            listeners.get(id).handle(packet);
        } catch (Exception e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public void send(Packet packet){
        var string = json.toJson(packet, Packet.class);
        webSocket.send(string);
    }

    public WebSocketClient getClient() {
        return client;
    }
}
