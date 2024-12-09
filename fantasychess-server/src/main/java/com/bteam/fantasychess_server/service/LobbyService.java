package com.bteam.fantasychess_server.service;

import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LobbyService {
    final PlayerService playerService;
    Map<UUID, LobbyModel> lobbyModels = new HashMap<>();
    Map<UUID, UUID> gameLobbyReference = new HashMap<>();

    public LobbyService(@Autowired PlayerService playerService) {
        this.playerService = playerService;
    }

    public LobbyModel createNewLobby(Player host, String name, int maxPlayers) {
        UUID uuid = UUID.randomUUID();
        var lobby = new LobbyModel(uuid.toString(), new ArrayList<>(), host, name, maxPlayers);
        lobbyModels.put(uuid, lobby);
        return lobby;
    }

    public LobbyModel getLobby(UUID uuid) {
        return lobbyModels.get(uuid);
    }

    public List<LobbyModel> getAllLobbies() {
        return new ArrayList<>(lobbyModels.values());
    }

    public boolean joinLobby(UUID uuid, UUID playerId) {
        var lobby = getLobby(uuid);
        try {
            lobby.addPlayer(playerService.getPlayer(playerId));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean leaveLobby(UUID uuid, UUID playerId) {
        var lobby = getLobby(uuid);
        lobby.removePlayer(playerService.getPlayer(playerId));
        return true;
    }

    public void removeLobby(UUID uuid) {
        lobbyModels.remove(uuid);
    }

    public LobbyModel lobbyWithPlayer(UUID uuid) {
        return lobbyModels.values().stream().filter(
                        lobby -> lobby.getPlayers().stream().anyMatch(
                                p -> Objects.equals(p.getPlayerId(), uuid.toString())))
                .findFirst().orElse(null);
    }

    public Collection<LobbyModel> getHostedLobbies(UUID uuid) {
        var player = playerService.getPlayer(uuid);
        return lobbyModels.values().stream().filter(l -> l.isHost(player)).toList();
    }
}