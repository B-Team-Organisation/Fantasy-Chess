package models;

import entities.CharacterEntity;

import java.util.Objects;

/**
 * Data model for character entities
 * <p>
 * This class represents a Data Transferable Object for static character data.
 * It contains static attributes about a character.
 *
 * @author Jacinto
 * @version 1.0
 */
public class CharacterDataModel {
    private CharacterEntity character;
    private String name;
    private int health;
    private int damage;

    /**
     * Creates a new CharacterDataModel.
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

    @Override
    public String toString() {
        return "CharacterDataModel "
                + "[character=" + character.toString()
                + ", name=" + name
                + ", health=" + health
                + ", damage=" + damage
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CharacterDataModel
                && ((CharacterDataModel) o).character.equals(this.character)
                && ((CharacterDataModel) o).name.equals(this.name)
                && ((CharacterDataModel) o).health == this.health
                && ((CharacterDataModel) o).damage == this.damage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, name, health, damage);
    }

}
