package com.bteam.common.models;

import java.util.Objects;
import com.bteam.common.entities.CharacterEntity;

/**
 * Represents the targeted movement of a character on the playing field.
 * <p>
 * A movement model contains of a {@link String} and its position as
 * a {@link Vector2D}
 *
 * @author Jacinto
 * @version 1.0
 */
public class MovementDataModel {
    private String characterId;
    Vector2D movementVector;

    /**
     * Create a new movement model from the current position and the movement vector.
     *
     * @param characterId reference to the character that is supposed to be moved
     * @param movementVector a 2DVector pointing to the absolut position the character should move to
     */
    public MovementDataModel(String characterId, Vector2D movementVector) {
        this.characterId = characterId;
        this.movementVector = movementVector;
    }

    public String getCharacterId() {
        return characterId;
    }

    public Vector2D getMovementVector() {
        return movementVector;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public void setMovementVector(Vector2D movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public String toString() {
        return "MovementVector: " + movementVector + "\nCharacterEntity:\n" + characterId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MovementDataModel
                && this.characterId.equals(((MovementDataModel) o).characterId)
                && movementVector.equals(((MovementDataModel) o).movementVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, movementVector);
    }

}