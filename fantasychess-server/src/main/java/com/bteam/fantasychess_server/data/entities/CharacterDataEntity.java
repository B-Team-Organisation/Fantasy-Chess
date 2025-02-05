package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CharacterDataEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    private int health;
    private int stamina;

    @ManyToOne
    private PatternDataEntity attackPattern;

    @ManyToOne
    private PatternDataEntity movement;

    public String getId() {
        return id;
    }

    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public PatternDataEntity getAttackPattern() {
        return attackPattern;
    }

    public PatternDataEntity getMovement() {
        return movement;
    }
}
