package com.bteam.fantasychess_server.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CharacterEntity {
    @Id
    private Long id;
    public Long getId() {
        return id;
    }

    private int positionX;
    private int positionY;
    private int currentHealth;


}
