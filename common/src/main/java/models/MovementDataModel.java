package models;

import java.util.Objects;
import main.java.entities.CharacterEntity;

/**
 * Represents the targeted movement of a character on the playing field.
 */
public class MovementDataModel {
    CharacterEntity characterEntity;
    Vector2D movementVector;

    /**
     * Create a new movement model from the current position and the movement vector.
     *
     * @param characterEntity - reference to the character that is supposed to be moved
     * @param movementVector - the movement vector to be added to the position
     */
    public MovementDataModel(CharacterEntity characterEntity, Vector2D movementVector) {
        this.characterEntity = characterEntity;
        this.movementVector = movementVector;
    }

    public CharacterEntity getCharacterEntity() {
        return characterEntity;
    }

    public Vector2D getMovementVector() {
        return movementVector;
    }

    public void setCharacterEntity(CharacterEntity characterEntity) {
        this.characterEntity = characterEntity;
    }

    public void setMovementVector(Vector2D movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public String toString() {
        return "MovementVector: " + movementVector + "\nCharacterEntity:\n" + characterEntity;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MovementDataModel
                && this.characterEntity.equals(((MovementDataModel) o).characterEntity)
                && movementVector.equals(((MovementDataModel) o).movementVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterEntity, movementVector);
    }

}