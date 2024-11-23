package com.bteam.fantasychess_client.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.CreateLobbyDTO;
import com.bteam.common.dto.Packet;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.data.errors.UnhandledPacketException;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketState;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.bteam.common.constants.PacketConstants.CONNECTED_STATUS;

/**
 * Websocket Service to establish communication with the Server,
 * features assignable packet handlers which will automatically
 * be called upon package arrival
 * @author Marc
 */
public class WebSocketService {
    private final Map<String, PacketHandler> listeners = new HashMap<>();
    private final Json json = new Json();

    WebSocket webSocket;
    WebSocketClient client;
    String userid;
    String baseAddress;

    public WebSocketService(String address, WebSocketClient listener) {
        this.baseAddress = address;
        client = listener;
        client.onTextEvent.addListener(this::handlePacket);

        client.onOpenEvent.addListener(payload ->
            Main.getLogger().log(Level.SEVERE, "Client has connected"));

        addPacketHandler(CONNECTED_STATUS, packet -> {
            Main.getLogger().log(Level.SEVERE, "It is now forwarded: " + packet);
        });

        addPacketHandler("LOBBY_CREATED", packet -> {
            Main.getLogger().log(Level.SEVERE, "Created Lobby: " + packet);
        });
    }

    public WebSocketState getState() {
        return webSocket.getState();
    }

    private void connect(String token) {
        Main.getLogger().log(Level.SEVERE, "Connecting to server...");
        String address = baseAddress + "?token=" + token;
        webSocket = WebSockets.newSocket(address);
        webSocket.setSendGracefully(true);
        webSocket.addListener(client);
        try{
            webSocket.connect();
            send(new Packet(new CreateLobbyDTO("EXAMPLE"),"LOBBY_CREATE"));
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

    /**
     * Takes the arrived packet in, reads it's ID and calls it's corresponding handler function
     * @param packet - Packet to handle
     */
    public void handlePacket(String packet){
        Main.getLogger().log(Level.SEVERE, "Received packet: " + packet);
        JsonValue fromJson = new JsonReader().parse(packet);
        String id = fromJson.getString("id");
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

    /**
     * Sends the given packet to the server
     * @param packet - Packet to handle
     */
    public void send(Packet packet){
        String string = json.toJson(packet, Packet.class);
        webSocket.send(string);
    }

    public WebSocketClient getClient() {
        return client;
    }

    public void registerAndConnect(String username){
        registerClient(username);
    }

    private void registerClient(String username){
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url("http://127.0.0.1:5050/api/v1/register")
            .content(username)
            .build();

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseCallbackListener(this::onRegisterResult));
    }

    private void onRegisterResult(Net.HttpResponse response){
        userid = response.getResultAsString();
        getToken(userid);
    }

    private void getToken(String userid){
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url("http://127.0.0.1:5050/api/v1/token")
            .header("X-USER-ID", userid)
            .build();
        Gdx.net.sendHttpRequest(httpRequest,new HttpResponseCallbackListener(this::onTokenResult));
    }

    private void onTokenResult(Net.HttpResponse response){
        JsonReader reader = new JsonReader();
        String token = reader.parse(response.getResultAsString()).getString("token");
        connect(token);
    }
}
