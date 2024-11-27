package com.bteam.common.models;

import java.util.Objects;
import com.bteam.common.entities.CharacterEntity;

/**
 * Represents the position of an attack as a model.
 * <p>
 * An attack model that has the position of the attack as {@link Vector2D}
 * and the attacker as {@link String}
 *
 * @author Jacinto
 * @version 1.0
 */
public class AttackDataModel {
    private Vector2D attackPosition;
    private String attackerId;

    /**
     * Create a new AttackDataModel.
     *
     * @param attackPosition a 2DVector pointing to the absolut Position of attack
     * @param attackerId reference to the attacking character entity
     */
    public AttackDataModel(Vector2D attackPosition, String attackerId) {
        this.attackPosition = attackPosition;
        this.attackerId = attackerId;
    }

    public Vector2D getAttackPosition(){
        return attackPosition;
    }

    public void setAttackPosition(Vector2D attackPosition) {
        this.attackPosition = attackPosition;
    }

    public String getAttacker(){
        return attackerId;
    }

    public void setAttacker(String attackerId) {
        this.attackerId = attackerId;
    }

    @Override
    public String toString() {
        return "Attack position: " + attackPosition.toString() + "\nAttacker:\n" + attackerId.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AttackDataModel
                && attackPosition == ((AttackDataModel) o).attackPosition
                && attackerId == ((AttackDataModel) o).attackerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attackPosition, attackerId);
    }
}
