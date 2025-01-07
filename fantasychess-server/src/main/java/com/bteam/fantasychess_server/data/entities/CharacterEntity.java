package com.bteam.fantasychess_server.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class CharacterEntity {
    @Id
    private final Long id;
    private int positionX;
    private int positionY;
    private int currentHealth;

    @ManyToOne
    private CharacterDataEntity characterDataModel;

    @JsonIgnore
    private UUID playerID;

    public CharacterEntity(Long id, int positionX, int positionY, int currentHealth, UUID playerID) {
        this.id = id;
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentHealth = currentHealth;
        this.playerID = playerID;
    }

    public CharacterEntity() {
        this.id = 0L;
    }

    public Long getId() {
        return id;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public CharacterDataEntity getCharacterDataModel() {
        return characterDataModel;
    }

    public void setCharacterDataModel(CharacterDataEntity characterDataModel) {
        this.characterDataModel = characterDataModel;
    }
}
