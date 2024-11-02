package models;

import entities.CharacterEntity;

import java.util.Objects;

/**
 * Data model for character entities
 * <p>
 * This class represents a Data Transferable Object for static character data.
 * It contains static attributes about a character.
 * <p>
 * A {@link CharacterDataModel} can only have one of {@link AttackDataModel} and {@link MovementDataModel}.
 * This is done to adhere to the game logic.
 *
 * @author Jacinto
 * @version 1.0
 */
public class CharacterDataModel {
    private CharacterEntity character;
    private String name;
    private int health;
    private int damage;
    private AttackDataModel attackData;
    private MovementDataModel movementData;


    /**
     * Creates a new CharacterDataModel with attack data.
     *
     * @param character - reference to the {@link CharacterEntity}
     * @param name - name of the character
     * @param health - the health of the character
     * @param damage - the damage from the character
     * @param attackData - the attack pattern that the character is set to
     */
    public CharacterDataModel(CharacterEntity character, String name, int health, int damage, AttackDataModel attackData) {
        this.character = character;
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.attackData = attackData;
    }

    /**
     * Creates a new CharacterDataModel with movement data.
     *
     * @param character - reference to the {@link CharacterEntity}
     * @param name - name of the character
     * @param health - the health of the character
     * @param damage - the damage from the character
     * @param movementData - the movement pattern that the character is set to
     */
    public CharacterDataModel(CharacterEntity character, String name, int health, int damage, MovementDataModel movementData) {
        this.character = character;
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.movementData = movementData;
    }

    /**
     * Creates a new CharacterDataModel without movement or attack data.
     *
     * @param character - reference to the {@link CharacterEntity}
     * @param name - name of the character
     * @param health - the health of the character
     * @param damage - the damage from the character
     */
    public CharacterDataModel(CharacterEntity character, String name, int health, int damage) {
        this.character = character;
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public CharacterEntity getCharacter() {
        return this.character;
    }

    public String getName() {
        return this.name;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    public AttackDataModel getAttackData() {
        return this.attackData;
    }

    public MovementDataModel getMovementData() {
        return this.movementData;
    }

    public void setCharacter(CharacterEntity character) {
        this.character = character;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setAttackData(AttackDataModel attackData) {
        this.attackData = attackData;
    }

    public void setMovementData(MovementDataModel movementData) {
        this.movementData = movementData;
    }

    @Override
    public String toString() {
        return "CharacterDataModel "
                + "[character=" + character.toString()
                + ", name=" + name
                + ", health=" + health
                + ", damage=" + damage
                + ", movementData=" + movementData
                + ", attackData=" + attackData
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterDataModel
                && ((CharacterDataModel) o).character.equals(this.character)
                && ((CharacterDataModel) o).name.equals(this.name)
                && ((CharacterDataModel) o).health == this.health
                && ((CharacterDataModel) o).damage == this.damage
                && ((CharacterDataModel) o).attackData == this.attackData
                && ((CharacterDataModel) o).movementData == this.movementData;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, name, health, damage, attackData, movementData);
    }

}
