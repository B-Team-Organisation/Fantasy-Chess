package main.java.models;

import java.util.Objects;
import main.java.entities.CharacterEntity;

/**
 * Represents the position of an attack as a model.
 */
public class AttackDataModel {
    private Vector2D attackPosition;
    private CharacterEntity attacker;

    /**
     * Create a new AttackDataModel.
     *
     * @param attackPosition - a 2DVector that points to the position of attack relative to the character
     * @param attacker - reference to the attacking character entity
     */
    public AttackDataModel(Vector2D attackPosition, CharacterEntity attacker) {
        this.attackPosition = attackPosition;
        this.attacker = attacker;
    }

    public Vector2D getAttackPosition(){
        return attackPosition;
    }

    public void setAttackPosition(Vector2D attackPosition) {
        this.attackPosition = attackPosition;
    }

    public CharacterEntity getAttacker(){
        return attacker;
    }

    public void setAttacker(CharacterEntity attacker) {
        this.attacker = attacker;
    }

    @Override
    public String toString() {
        return "Attack position: " + attackPosition.toString() + "\nAttacker:\n" + attacker.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AttackDataModel
                && attackPosition == ((AttackDataModel) o).attackPosition
                && attacker == ((AttackDataModel) o).attacker;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attackPosition, attacker);
    }
}
