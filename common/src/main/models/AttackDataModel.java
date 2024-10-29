package main.models;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents an attack pattern for a character.
 */
public class AttackDataModel {
    private int damage;
    private Vector2D[] areaOfAttack;

    /**
     * Create a new AttackDataModel
     *
     * @param damage - the damage dealt by the attack
     * @param areaOfAttack - a 2DVector array containing arrays to all
     */
    public AttackDataModel(int damage, Vector2D[] areaOfAttack) {
        this.damage = damage;
        this.areaOfAttack = areaOfAttack;
    }

    public int getDamage() {
        return damage;
    }

    public Vector2D[] getAreaOfAttack(){
        return areaOfAttack;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAreaOfAttack(Vector2D[] areaOfAttack) {
        this.areaOfAttack = areaOfAttack;
    }

    /**
     * String representation of an attack data model in the format:
     * Damage: [damage]
     * AttackVector: Vector (one line per vector)
     */
    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder("Damage: " + damage + "\n");
        for (Vector2D attackVector : areaOfAttack) {
            representation.append("AttackVector: ");
            representation.append(attackVector);
            representation.append("\n");
        }
        return representation.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AttackDataModel
                && damage == ((AttackDataModel) o).damage
                && Arrays.equals(areaOfAttack, ((AttackDataModel) o).areaOfAttack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, Arrays.hashCode(areaOfAttack));
    }
}
