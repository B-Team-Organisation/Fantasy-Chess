package com.bteam.fantasychess_server.service;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.enums.GameStatus;
import com.bteam.common.exceptions.DestinationAlreadyOccupiedException;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.services.TurnResult;
import com.bteam.common.stores.CharacterStore;
import com.bteam.common.utils.Pair;
import com.bteam.common.utils.PairNoOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to manage the state of the currently played Games
 */
@Service
public class GameStateService {
    private static final int DEFAULT_GRID_SIZE = 9;

    HashMap<UUID, GameModel> games = new HashMap<>();
    LobbyService lobbyService;
    PlayerService playerService;

    public GameStateService(@Autowired LobbyService lobbyService,
                            @Autowired PlayerService playerService) {
        this.lobbyService = lobbyService;
        this.playerService = playerService;
    }

    public void setPlayerCommands(UUID playerId, UUID gameId, List<MovementDataModel> moves, List<AttackDataModel> attacks) {
        var pair = new Pair<>(attacks, moves);
        var game = games.get(gameId);
        game.getCommands().put(playerId.toString(), pair);
    }


    public Map<UUID, GameModel> getGames() {
        return games;
    }

    public GameModel getGame(UUID gameId) {
        return games.get(gameId);
    }

    public GameModel startNewGame(GameSettingsModel settings, String lobbyId, List<UUID> playerIds) {
        var entities = generateInitialCharacters(playerIds);
        var id = UUID.randomUUID();
        var grid = new GridModel(DEFAULT_GRID_SIZE, DEFAULT_GRID_SIZE);
        var service = new GridService(grid);
        for (CharacterEntity entity : entities) {
            try {
                service.setCharacterTo(entity.getPosition(), entity);
            } catch (DestinationInvalidException e) {
                System.out.println(e);
            } catch (DestinationAlreadyOccupiedException e) {
                System.out.println(e);
            }
        }
        var model = new GameModel(grid, id.toString(), 0,
                settings.getMaxTurnSeconds(), GameStatus.Running, entities, lobbyId);
        games.put(id, model);
        return model;
    }

    private List<CharacterEntity> generateInitialCharacters(List<UUID> playerIds) {
        AtomicInteger x = new AtomicInteger(0);
        List<CharacterEntity> characters = new ArrayList<>();
        var host = playerIds.get(0);
        for (var p : playerIds) {
            x.set(0);
            var entities = CharacterStore.characters.values().stream().map(
                    model -> new CharacterEntity(
                            model, UUID.randomUUID().toString(), model.getHealth(),
                            new Vector2D(x.incrementAndGet(), p.equals(host) ? 0 : 8), p.toString())).toList();
            characters.addAll(entities);
        }
        return characters;
    }

    public void cancelGame(UUID id) {
        games.get(id).setStatus(GameStatus.Ended);
    }

    public Pair<TurnResult, GridModel>
    processMoves(UUID gameId, Map<String, Pair<List<AttackDataModel>, List<MovementDataModel>>> commands) {
        var game = games.get(gameId);
        GridService gridService = new GridService(game.getGrid());
        var movements = new ArrayList<MovementDataModel>();
        var attacks = new ArrayList<AttackDataModel>();

        var playerIds = commands.keySet().toArray();
        var lobbies = lobbyService.getAllLobbies().stream()
                .filter(l -> l.getPlayers().stream().anyMatch(
                        p -> p.getPlayerId().equals(playerIds[0])));
        var lobbyOptional = lobbies.findFirst();
        if (lobbyOptional.isEmpty())
            throw new RuntimeException("Lobby not found");
        var host = lobbyOptional.get().getHost();

        for (var k : commands.entrySet()) {
            if (host.getPlayerId().equals(k.getKey())) {
                var invertedAttacks = invertAttacks(k.getValue().getFirst());
                var invertedMovement = invertMovements(k.getValue().getSecond());
                attacks.addAll(invertedAttacks);
                movements.addAll(invertedMovement);
            } else {
                movements.addAll(k.getValue().getSecond());
                attacks.addAll(k.getValue().getFirst());
            }
        }

        if (game.getTurn() == 0) {
            TurnLogicService.applyMovement(movements, game.getEntities(), gridService);
            var result = new TurnResult(game.getEntities(), List.of(), movements, List.of());
            game.setTurn(game.getTurn() + 1);
            return new Pair<>(result, gridService.getGridModel());
        }

        var result = TurnLogicService.applyCommands(movements, game.getEntities(), attacks, gridService, host.getPlayerId());
        game.getCommands().clear();
        game.setTurn(game.getTurn() + 1);
        return new Pair<>(result, gridService.getGridModel());
    }

    public TurnResult invertResult(TurnResult result) {
        var validMovement = result.getValidMoves().stream().map(this::movementInverter).toList();
        var validAttacks = result.getValidAttacks().stream().map(this::attackInverter).toList();
        var movementConflicts = result.getMovementConflicts() != null ? result.getMovementConflicts()
                .stream().map(pair -> {
                    var first = movementInverter(pair.getFirst());
                    var second = movementInverter(pair.getSecond());
                    return new PairNoOrder<>(first, second);
                }).toList() : null;
        return new TurnResult(result.getUpdatedCharacters(), movementConflicts, validMovement, validAttacks);
    }

    public boolean checkForOwnership(GameModel model, String characterId, Player owner) {
        var characterEntityOptional = model.getEntities().stream().filter(c -> c.getId().equals(characterId)).findFirst();
        if (characterEntityOptional.isEmpty())
            throw new RuntimeException("Character not found");
        var characterEntity = characterEntityOptional.get();
        return checkForOwnership(characterEntity, owner);
    }

    public boolean checkForOwnership(CharacterEntity character, Player owner) {
        return character.getPlayerId().equals(owner.getPlayerId());
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
        var x = DEFAULT_GRID_SIZE - model.getMovementVector().getX() - 1;
        var y = DEFAULT_GRID_SIZE - model.getMovementVector().getY() - 1;
        return new MovementDataModel(model.getCharacterId(), new Vector2D(x, y));
    }
}
