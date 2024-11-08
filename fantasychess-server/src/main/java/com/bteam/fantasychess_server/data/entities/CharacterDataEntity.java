package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.*;

@Entity
public class CharacterDataEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    public String getId() {
        return id;
    }

    private int health;
    private int stamina;

    /* private PatternEntity attackPattern;
    private PatternEntity movementPattern; */
}
