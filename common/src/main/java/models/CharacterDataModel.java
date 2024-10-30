package models;

import entities.CharacterEntity;

/**
 * Represents character data stored as a model.
 */
public class CharacterDataModel {
    private CharacterEntity character;
    private String name;
    private int health;
    private int damage;
    // private AttackMovementPatter;
    // private MovementPattern;

    /**
     * Creates a new CharacterDataModel.
     *
     * @param character - reference to the character entity
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

}
