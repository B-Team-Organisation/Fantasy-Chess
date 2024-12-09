package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CharacterDataEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    private int health;
    private int stamina;

    public String getId() {
        return id;
    }


}
