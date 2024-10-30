package models;

import entities.CharacterEntity;

import java.util.Objects;

/**
 * Data model for a tile of the {@link GridModel}
 * <p>
 * Can hold the following information for each tile:
 *  {@link CharacterEntity}, which might occupy this tile
 *
 *
 * @author lukas albano
 * @version 1.0
 */
public class TileModel {
    private CharacterEntity character = null;

    public void setCharacter(CharacterEntity character) {
        this.character = character;
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return "TileModel [character=" + character.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TileModel && Objects.equals(character, ((TileModel) o).character);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }
}
