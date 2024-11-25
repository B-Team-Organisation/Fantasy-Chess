package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.CreateLobbyDTO;
import com.bteam.common.dto.LobbyDTO;
import com.bteam.common.dto.LobbyListDTO;
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
    public void handle(Client client, String id, String packet) {
        switch (id){
            case "LOBBY_ALL":
                try{
                    var lobbies = lobbyService.getAllLobbies();
                    var dtos = lobbies.stream().map(LobbyDTO::new).toList();
                    var lobbyListDTO = new LobbyListDTO(dtos);
                    client.sendPacket(new Packet(lobbyListDTO,"LOBBY_INFO"));
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "LOBBY_CREATE":
                try{
                    ObjectMapper mapper = new ObjectMapper();
                    var tree = mapper.readTree(packet);
                    var data = tree.get("data").asText();
                    var dto = mapper.readValue(data, CreateLobbyDTO.class);
                    var lobby = lobbyService.createNewLobby(client.getPlayer(),dto.getLobbyName(),2);
                    var lobbyDTO = new LobbyDTO(lobby);
                    client.sendPacket(new Packet(lobbyDTO, "LOBBY_CREATED"));
                } catch (Exception e){
                    e.printStackTrace();
                }

                break;
            default:
                System.out.println("Unhandled packet: " + id);
        }

    }

    @Override
    public String getPacketPattern() {
        return packetPattern;
    }
}
