package com.bteam.common.models;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.enums.GameStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Model Class for a Single game, which encapsulates information such as the current turn, how long each turn should
 * take at maximum, the current entities which are alive in the given game, the state of the game and the grid being
 * played on.
 */
public class GameModel {
    private final UUID id;
    private final Map<UUID, List<CharacterEntity>> entities;
    private GridModel grid;
    private int turn;
    private int maxTurnSeconds;
    private GameStatus status;

    public GameModel(GridModel grid, UUID id, int turn, int maxTurnSeconds, GameStatus status, Map<UUID, List<CharacterEntity>> entities) {
        this.grid = grid;
        this.id = id;
        this.turn = turn;
        this.maxTurnSeconds = maxTurnSeconds;
        this.status = status;
        this.entities = entities;
    }

    public GridModel getGrid() {
        return grid;
    }

    public void setGrid(GridModel grid) {
        this.grid = grid;
    }

    public UUID getId() {
        return id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getMaxTurnSeconds() {
        return maxTurnSeconds;
    }

    public void setMaxTurnSeconds(int maxTurnSeconds) {
        this.maxTurnSeconds = maxTurnSeconds;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Map<UUID, List<CharacterEntity>> getEntities() {
        return entities;
    }
}
