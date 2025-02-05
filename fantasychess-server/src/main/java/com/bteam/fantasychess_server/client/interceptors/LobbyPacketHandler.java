package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.*;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.client.PacketHandler;
import com.bteam.fantasychess_server.service.LobbyService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.bteam.common.constants.PacketConstants.*;

@Component
public class LobbyPacketHandler implements PacketHandler {
    private final String packetPattern = "LOBBY_";
    private final LobbyService lobbyService;
    private final WebSocketService webSocketService;

    public LobbyPacketHandler(LobbyService lobbyService, WebSocketService webSocketService) {
        this.lobbyService = lobbyService;
        this.webSocketService = webSocketService;
    }

    @Override
    public void handle(Client client, String id, String packet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var tree = mapper.readTree(packet);
        var data = tree.get("data");

        switch (id) {
            case LOBBY_ALL:
                try {
                    var lobbies = lobbyService.getAllLobbies();
                    var dtos = lobbies.stream().map(LobbyDTO::new).toList();
                    var lobbyListDTO = new LobbyListDTO(dtos);
                    client.sendPacket(new Packet(lobbyListDTO, LOBBY_INFO));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LOBBY_CREATE:
                try {
                    var dto = mapper.convertValue(data, CreateLobbyDTO.class);
                    var lobby = lobbyService.createNewLobby(client.getPlayer(), dto.getLobbyName(), 2);
                    var lobbyDTO = new LobbyDTO(lobby);
                    client.sendPacket(new Packet(lobbyDTO, LOBBY_CREATED));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case LOBBY_JOIN:
                try {
                    var dto = mapper.convertValue(data, JoinLobbyDTO.class);
                    var lobbyID = UUID.fromString(dto.getId());
                    var playerId = UUID.fromString(client.getPlayer().getPlayerId());
                    var result = lobbyService.joinLobby(lobbyID, playerId);
                    var lobby = lobbyService.getLobby(lobbyID);

                    var resultPacket = new Packet(
                            result ? JoinLobbyResultDTO.success(new LobbyDTO(lobby))
                                    : JoinLobbyResultDTO.error(new LobbyDTO(lobby)),
                            LOBBY_JOINED);

                    client.sendPacket(resultPacket);
                    var players = lobby.getPlayers();

                    if (result) players.forEach(player -> {
                        var playerClient = WebSocketService.getCurrentClientForPlayer(player);
                        var playerPacket = new Packet(new PlayerDTO(client.getPlayer()), PLAYER_JOINED);
                        playerClient.sendPacket(playerPacket);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LOBBY_CLOSE:
                try {
                    var dto = mapper.convertValue(data, LobbyClosedDTO.class);
                    var lobbyId = UUID.fromString(dto.getLobbyId());
                    lobbyService.removeLobby(lobbyId);
                    var confirmDto = new LobbyClosedDTO(lobbyId.toString(), "closed by host");
                    var confirmationPacket = new Packet(confirmDto, LOBBY_CLOSED);
                    client.sendPacket(confirmationPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "LOBBY_UPDATE":
                try {
                    //
                } catch (Exception e) {
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
