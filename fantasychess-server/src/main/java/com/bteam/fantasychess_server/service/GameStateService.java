package com.bteam.fantasychess_server.service;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.enums.GameStatus;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.services.TurnResult;
import com.bteam.common.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    private static final int DEFAULT_GRID_SIZE = 9;

    HashMap<UUID, GameModel> games = new HashMap<>();
    LobbyService lobbyService;
    PlayerService playerService;


    public GameStateService(@Autowired LobbyService lobbyService, @Autowired PlayerService playerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
    }

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

    public Pair<TurnResult, GridModel>
    processMoves(UUID gameId, HashMap<UUID, Pair<List<AttackDataModel>, List<MovementDataModel>>> commands) {
        var game = games.get(gameId);
        GridService service = new GridService(game.getGrid());
        var movements = new ArrayList<MovementDataModel>();
        var attacks = new ArrayList<AttackDataModel>();

        for (var k : commands.keySet()) {
            var lobbies = lobbyService.getAllLobbies().stream()
                    .filter(l -> l.getPlayers().stream().anyMatch(
                            p -> p.getPlayerId().equals(k.toString())));
            var optionalLobby = lobbies.findFirst();
            if (optionalLobby.isPresent() && optionalLobby.get().isHost(playerService.getPlayer(k))) {
                var invertedAttacks = invertAttacks(commands.get(k).getFirst());
                var invertedMovement = invertMovements(commands.get(k).getSecond());
                commands.put(k, new Pair<>(invertedAttacks, invertedMovement));
            }
        }

        var result = TurnLogicService.applyCommands(movements, game.getEntities(), attacks, service);
        game.getCommands().clear();
        return new Pair<>(result, service.getGridModel());
    }

    public List<AttackDataModel> invertAttacks(List<AttackDataModel> attacks) {
        return attacks.stream().map(this::attackInverter).toList();
    }

    private AttackDataModel attackInverter(AttackDataModel model) {
        // Everything relies on the default grid size now, TOO BAD
        var x = DEFAULT_GRID_SIZE - model.getAttackPosition().getX() - 1;
        var y = DEFAULT_GRID_SIZE - model.getAttackPosition().getY() - 1;
        return new AttackDataModel(new Vector2D(x, y), model.getAttacker());
    }

    public List<MovementDataModel> invertMovements(List<MovementDataModel> movements) {
        return movements.stream().map(this::movementInverter).toList();
    }

    private MovementDataModel movementInverter(MovementDataModel model) {
        var x = DEFAULT_GRID_SIZE - model.getMovementVector().getX();
        var y = DEFAULT_GRID_SIZE - model.getMovementVector().getY();
        return new MovementDataModel(model.getCharacterId(), new Vector2D(x, y));
    }
}
