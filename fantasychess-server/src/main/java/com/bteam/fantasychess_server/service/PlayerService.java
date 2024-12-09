package com.bteam.fantasychess_server.service;

import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.data.entities.PlayerEntity;
import com.bteam.fantasychess_server.data.mapper.PlayerMapper;
import com.bteam.fantasychess_server.data.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerService {
    final PlayerRepository playerRepository;

    public PlayerService(@Autowired PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayer(UUID id) {
        var optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isEmpty()) return null;
        var player = optionalPlayer.get();
        return PlayerMapper.fromEntity(player);
    }

    public Player createPlayer(String name) {
        var savedPlayer = playerRepository.save(new PlayerEntity(name));
        return PlayerMapper.fromEntity(savedPlayer);
    }

    public void setPlayerStatus(UUID uuid, Player.Status status) {
        var player = playerRepository.findById(uuid);
        if (player.isEmpty()) return;
        var updatedPlayer = player.get();
        updatedPlayer.setStatus(status);
        playerRepository.save(updatedPlayer);
    }
}