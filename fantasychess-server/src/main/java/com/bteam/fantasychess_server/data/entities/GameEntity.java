package com.bteam.fantasychess_server.data.entities;

import enums.GameStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "turn")
    private int Turn;

    private int maxTurnSeconds;
    private GameStatus status;

    @OneToMany
    private List<CharacterEntity> entities;

    private int rows;
    private int cols;

    public UUID getId() {
        return id;
    }

    public int getTurn() {
        return Turn;
    }

    public void setTurn(int turn) {
        Turn = turn;
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

    public List<CharacterEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<CharacterEntity> entities) {
        this.entities = entities;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}