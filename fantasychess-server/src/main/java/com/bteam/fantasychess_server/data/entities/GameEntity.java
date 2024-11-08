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

    @Column
    private int maxTurnSeconds;

    @Column
    private GameStatus status;


    @OneToMany
    private List<CharacterDataEntity> characters;

    public UUID getId() {
        return id;
    }
}
