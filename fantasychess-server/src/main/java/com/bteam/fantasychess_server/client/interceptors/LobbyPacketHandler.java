package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.Packet;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.service.LobbyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LobbyPacketHandler implements PacketHandler {
    @Autowired
    private LobbyService lobbyService;
    private final String packetPattern = "LOBBY_";

    @Override
    public void handle(Client client, Packet packet) {
        switch (packet.getId()){
            case "LOBBY_ALL":
                try{
                    var lobbies = lobbyService.getAllLobbies();
                    ObjectMapper mapper = new ObjectMapper();
                    client.sendPacket(new Packet("LOBBY_INFO", mapper.writeValueAsString(lobbies)));
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Unhandled packet: " + packet.getId());
        }

    }

    @Override
    public String getPacketPattern() {
        return packetPattern;
    }
}
