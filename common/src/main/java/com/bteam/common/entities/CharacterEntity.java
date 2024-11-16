package com.bteam.common.entities;


import com.bteam.common.models.Vector2D;
import com.bteam.common.models.CharacterDataModel;

import java.util.Objects;

/**
 * Entity representing the state of a character.
 * <p>
 * Contains a reference to the {@link CharacterDataModel} and
 * live attributes of the character like current health and
 * position.
 *
 * @author Jacinto, Adnan
 * @version 1.0
 */
public class CharacterEntity {
    private CharacterDataModel characterBaseModel;
    private int health;
    private Vector2D position;
    private String playerId;

    /**
     * Creates a new Character
     *
     * @param characterBaseModel Reference to the character in question
     * @param health Current health of the character
     * @param position Current position of the character
     */
    public CharacterEntity(CharacterDataModel characterBaseModel, int health, Vector2D position, String playerId) {
        this.characterBaseModel = characterBaseModel;
        this.health = health;
        this.position = position;
        this.playerId = playerId;
    }

    public CharacterDataModel getCharacterBaseModel() {
        return this.characterBaseModel;
    }

    public int getHealth() {
        return this.health;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public String getPlayerId() {return this.playerId;}

    public void setCharacterBaseModel(CharacterDataModel characterBaseModel) {
        this.characterBaseModel = characterBaseModel;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "CharacterEntity [characterBaseModel=" + characterBaseModel.toString()
                + ", health=" + health
                + ", position =" + position
                + ", playerId=" + playerId + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterEntity
                && ((CharacterEntity) o).characterBaseModel.equals(this.characterBaseModel)
                && ((CharacterEntity) o).health == this.health
                && ((CharacterEntity) o).position.equals(this.position)
                && ((CharacterEntity) o).playerId.equals(this.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterBaseModel, health, position, playerId);
    }

}
