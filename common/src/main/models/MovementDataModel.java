package main.models;

import java.util.Objects;

/**
 * Represents the movement of a character on the playing field.
 * Main purpose is the movement calculation through the calculateMovement method.
 */
public class MovementDataModel {
    Vector2D currentPosition;
    Vector2D movementVector;

    /**
     * Create a new movement model from the current position and the movement vector.
     *
     * @param currentPosition - the current position of a character
     * @param movementVector - the movement vector to be added to the position
     */
    public MovementDataModel(Vector2D currentPosition, Vector2D movementVector) {
        this.currentPosition = currentPosition;
        this.movementVector = movementVector;
    }

    /**
     * Calculate the position after movement.
     *
     * @return the position after movement.
     */
    public Vector2D calculateMovement() {
        return new Vector2D(
                currentPosition.getX() +movementVector.getX(),
                currentPosition.getY() +movementVector.getY()
                );
    }

    public Vector2D getCurrentPosition() {
        return currentPosition;
    }

    public Vector2D getMovementVector() {
        return movementVector;
    }

    public void setCurrentPosition(Vector2D currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setMovementVector(Vector2D movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public String toString() {
        return "CurrentPosition: " + currentPosition + ", MovementVector: " + movementVector;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MovementDataModel && this.currentPosition.equals(((MovementDataModel) o).currentPosition)
                && movementVector.equals(((MovementDataModel) o).movementVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPosition, movementVector);
    }

}