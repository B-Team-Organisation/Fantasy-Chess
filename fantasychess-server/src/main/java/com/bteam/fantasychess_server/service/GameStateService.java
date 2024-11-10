package com.bteam.fantasychess_server.service;

import Exceptions.DestinationAlreadyOccupiedException;
import Exceptions.DestinationInvalidException;
import Exceptions.GameNotRunningException;
import Exceptions.NotAStartPositionException;
import com.bteam.fantasychess_server.utils.Pair;
import entities.CharacterEntity;
import enums.GameStatus;
import models.*;
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
    Map<UUID, GameModel> games = new HashMap<>();

    public GameModel getGame(UUID id) {
        return games.get(id);
    }

    public GameModel createGame(GameSettingsModel settings) {
        UUID id = UUID.randomUUID();
        var model = new GameModel(
                new GridModel(9, 9),
                id,
                0,
                settings.getMaxTurnSeconds(),
                GameStatus.Setup,
                new HashMap<>());
        games.put(id, model);
        return model;
    }

    public void setCharacters(UUID gameId, Map<UUID, List<Pair<Vector2D, CharacterDataModel>>> characterMap)
            throws DestinationInvalidException, DestinationAlreadyOccupiedException, NotAStartPositionException {
        var gameModel = getGame(gameId);
        var gridService = new GridService(gameModel.getGrid());
        for (var playerCharactersEntry : characterMap.entrySet()) {
            for (var model : playerCharactersEntry.getValue()) {
                var entity = new CharacterEntity(
                        model.getSecond(),
                        model.getSecond().getHealth(),
                        model.getFirst(),
                        playerCharactersEntry.getKey());
                gridService.setCharacterTo(entity.getPosition(), entity);
            }
        }
    }

    public GameModel processCommands(UUID gameId,
                                     Map<UUID, List<MovementDataModel>> moveCommandMap,
                                     Map<UUID, AttackDataModel> attackCommandMap) {
        var gameModel = getGame(gameId);
        if (gameModel.getStatus() != GameStatus.Running)
            throw new GameNotRunningException(gameId);

        //TODO: Call turn Logic on the Board and process the specified command
        return gameModel;
    }

    public void removeGame(UUID gameId) {
        games.remove(gameId);
    }

    public void StartGame(UUID gameId) {
        getGame(gameId).setStatus(GameStatus.Running);
    }

    public void PauseGame(UUID gameId) {
        getGame(gameId).setStatus(GameStatus.Paused);
    }

    public void EndGame(UUID gameId, UUID winningPlayer) {
        getGame(gameId).setStatus(GameStatus.Ended);
    }
}
