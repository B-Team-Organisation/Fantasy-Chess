package com.bteam.common.models;

import java.util.Arrays;
import java.util.Objects;

/**
 * Data model for character entities
 * <p>
 * This class represents a Character Model to model characters.
 * It contains static attributes about a character, including all
 * movement and attack patterns as arrays of {@link PatternModel}
 *
 * @author Jacinto
 * @version 1.0
 */
public class CharacterDataModel {
    private String name;
    private String description;
    private int health;
    private int attackPower;
    private PatternService[] attackPatterns;
    private PatternService[] movementPatterns;


    /**
     * Creates a new CharacterDataModel.
     *
     * @param name name of the character
     * @param description a description for the character
     * @param health the health of the character
     * @param attackPower the power of the characters attack
     * @param attackPatterns all attack patterns that the character can perform
     * @param movementPatterns the movement pattern that the character can perform
     */
    public CharacterDataModel(
            String name,
            String description,
            int health,
            int attackPower,
            PatternService[] attackPatterns,
            PatternService[] movementPatterns
    ) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.attackPower = attackPower;
        this.attackPatterns = Arrays.copyOf(attackPatterns, attackPatterns.length);
        this.movementPatterns = Arrays.copyOf(movementPatterns, attackPatterns.length);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getHealth() {
        return this.health;
    }

    public int getAttackPower(){return this.attackPower;}

    public PatternService[] getAttackPatterns() {
        return Arrays.copyOf(attackPatterns, attackPatterns.length);
    }

    public PatternService[] getMovementPatterns() {
        return Arrays.copyOf(movementPatterns, attackPatterns.length);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttackPower(int attackPower) {this.attackPower=attackPower;}

    public void setAttackPatterns(PatternService[] attackPatterns) {
        this.attackPatterns = attackPatterns;
    }

    public void setMovementPatterns(PatternService[] movementPatterns) {
        this.movementPatterns = movementPatterns;
    }

    @Override
    public String toString() {
        return "CharacterDataModel "
                + "[name=" + name
                + ", description=" + description
                + ", health=" + health
                + ", attackPower" + attackPower
                + ", movementData=" + Arrays.toString(movementPatterns)
                + ", attackData=" + Arrays.toString(attackPatterns)
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterDataModel
                && ((CharacterDataModel) o).name.equals(this.name)
                && ((CharacterDataModel) o).description.equals(this.description)
                && ((CharacterDataModel) o).health == this.health
                && ((CharacterDataModel) o).attackPower == this.attackPower
                && Arrays.equals(((CharacterDataModel) o).movementPatterns, this.movementPatterns)
                && Arrays.equals(((CharacterDataModel) o).attackPatterns, attackPatterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, description, health, attackPower,
                Arrays.hashCode(movementPatterns),
                Arrays.hashCode(attackPatterns));
    }

}
