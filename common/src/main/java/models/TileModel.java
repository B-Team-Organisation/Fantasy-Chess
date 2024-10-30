package models;

import entities.CharacterEntity;

public class TileModel {
    private CharacterEntity character = null;

    public void setCharacter(CharacterEntity character) {
        this.character = character;
    }

    public CharacterEntity getCharacter() {
        return character;
    }
}
