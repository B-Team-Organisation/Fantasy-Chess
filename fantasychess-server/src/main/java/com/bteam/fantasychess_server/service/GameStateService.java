package com.bteam.fantasychess_server.service;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.enums.GameStatus;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.services.TurnResult;
import com.bteam.fantasychess_server.utils.Pair;
import models.GameSettingsModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    private static final int DEFAULT_GRID_SIZE = 9;

    HashMap<UUID, GameModel> games = new HashMap<>();

    public HashMap<UUID, GameModel> getGames() {
        return games;
    }

    public void startNewGame(List<Player> player, List<CharacterEntity> characters, GameSettingsModel settings) {
        var id = UUID.randomUUID();
        var model = new GameModel(
                new GridModel(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE), id.toString(), 0,
                settings.getMaxTurnSeconds(), GameStatus.Running, characters);
        games.put(id, model);
    }

    public void cancelGame(UUID id) {
        games.get(id).setStatus(GameStatus.Ended);
    }

    public Pair<TurnResult, GridModel> processMoves(UUID gameId, List<MovementDataModel> movements, List<AttackDataModel> attacks) {
        var game = games.get(gameId);
        GridService service = new GridService(game.getGrid());
        var result = TurnLogicService.applyCommands(movements, game.getEntities(), attacks, service);
        return new Pair<>(result, service.getGridModel());
    }
}
