package com.bteam.fantasychess_server.service;

import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.data.entities.GameEntity;
import com.bteam.fantasychess_server.data.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    @Autowired
    private GameRepository gameRepository;

    public GameEntity getGame(UUID id) {
        return gameRepository.getReferenceById(id);
    }

    public GameEntity getNewGame(Client host/*, GameSettings settings, Lobby lobby*/) {
        var game = new GameEntity();
        return gameRepository.save(game);
    }
}
