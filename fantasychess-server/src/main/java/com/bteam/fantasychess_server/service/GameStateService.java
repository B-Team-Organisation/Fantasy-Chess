package com.bteam.fantasychess_server.service;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.enums.GameStatus;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.services.TurnResult;
import com.bteam.common.utils.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    private static final int DEFAULT_GRID_SIZE = 9;

    HashMap<UUID, GameModel> games = new HashMap<>();

    public void setPlayerMoves(UUID playerId, UUID gameId, List<MovementDataModel> moves, List<AttackDataModel> attacks) {
        var pair = new Pair<>(attacks, moves);
        var game = games.get(playerId);
        game.getCommands().put(playerId.toString(), pair);
    }

    public Map<UUID, GameModel> getGames() {
        return games;
    }

    public GameModel getGame(UUID gameId) {
        return games.get(gameId);
    }

    public GridModel startNewGame(List<CharacterEntity> characters,
                                  GameSettingsModel settings, String lobbyId) {
        var id = UUID.randomUUID();
        var model = new GameModel(
                new GridModel(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE), id.toString(), 0,
                settings.getMaxTurnSeconds(), GameStatus.Running, characters, lobbyId);
        games.put(id, model);
        return model.getGrid();
    }

    public void cancelGame(UUID id) {
        games.get(id).setStatus(GameStatus.Ended);
    }

    public Pair<TurnResult, GridModel> processMoves(UUID gameId, List<MovementDataModel> movements, List<AttackDataModel> attacks) {
        var game = games.get(gameId);
        GridService service = new GridService(game.getGrid());
        var result = TurnLogicService.applyCommands(movements, game.getEntities(), attacks, service);
        game.getCommands().clear();
        return new Pair<>(result, service.getGridModel());
    }
}
