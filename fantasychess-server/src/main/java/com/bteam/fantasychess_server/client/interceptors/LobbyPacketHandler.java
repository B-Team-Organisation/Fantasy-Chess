package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.*;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LobbyPacketHandler implements PacketHandler {
    @Autowired
    private LobbyService lobbyService;
    private final String packetPattern = "LOBBY_";

    public LobbyPacketHandler(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @Override
    public void handle(Client client, String id, String packet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var tree = mapper.readTree(packet);
        var data = tree.get("data");

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
                    var dto = mapper.convertValue(data, CreateLobbyDTO.class);
                    var lobby = lobbyService.createNewLobby(client.getPlayer(),dto.getLobbyName(),2);
                    var lobbyDTO = new LobbyDTO(lobby);
                    client.sendPacket(new Packet(lobbyDTO, "LOBBY_CREATED"));
                } catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case "LOBBY_JOIN":
                try{
                    var dto = mapper.convertValue(data, JoinLobbyDTO.class);
                    var lobbyID = UUID.fromString(dto.getId());
                    var playerId = UUID.fromString(client.getPlayer().getPlayerId());
                    var result = lobbyService.joinLobby(lobbyID,playerId);
                    var resultPacket = new Packet(
                            new JoinLobbyResultDTO(result ? "SUCCESS" : "FAILED"),
                            "LOBBY_JOINED");
                    client.sendPacket(resultPacket);
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
