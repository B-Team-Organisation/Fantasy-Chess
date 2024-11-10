package entities;

import models.CharacterDataModel;
import models.Vector2D;

import java.util.Objects;
import java.util.UUID;

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
    private final UUID playerId;
    private CharacterDataModel characterBaseModel;
    private int health;
    private Vector2D position;

    /**
     * Creates a new Character
     *
     * @param characterBaseModel Reference to the character in question
     * @param health             Current health of the character
     * @param position           Current position of the character
     * @param playerId           PlayerId of the Player, which the entity belongs to
     */
    public CharacterEntity(CharacterDataModel characterBaseModel, int health, Vector2D position, UUID playerId) {
        this.characterBaseModel = characterBaseModel;
        this.health = health;
        this.position = position;
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public CharacterDataModel getCharacterBaseModel() {
        return this.characterBaseModel;
    }

    public void setCharacterBaseModel(CharacterDataModel characterBaseModel) {
        this.characterBaseModel = characterBaseModel;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "CharacterEntity [characterBaseModel=" + characterBaseModel.toString()
                + ", health=" + health
                + ", position =" + position + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterEntity
                && ((CharacterEntity) o).characterBaseModel.equals(this.characterBaseModel)
                && ((CharacterEntity) o).health == this.health
                && ((CharacterEntity) o).position.equals(this.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterBaseModel, health, position);
    }

}
