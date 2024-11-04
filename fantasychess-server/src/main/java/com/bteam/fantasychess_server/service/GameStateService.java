package com.bteam.fantasychess_server.service;

import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.data.Game;
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

    public Game getGame(UUID id) {
        return gameRepository.getReferenceById(id);
    }

    public Game getNewGame(Client host/*, GameSettings settings, Lobby lobby*/) {
        var game = new Game(host);
        return gameRepository.save(game);
    }
}
