package com.bteam.common.models;

import com.bteam.common.entities.CharacterEntity;

import java.util.Objects;

/**
 * Data model for a tile of the {@link GridModel}
 * <p>
 * Can hold the following information for each tile:
 *  {@link CharacterEntity}, which might occupy this tile
 *  {@code isStartTile}, if this tile is a start tile
 *
 * @author Lukas, Albano
 * @version 1.0
 */
public class TileModel {
    private CharacterEntity character = null;
    private boolean isStartTile = false;

    public void setStartTile(boolean isStartTile) {
        this.isStartTile = isStartTile;
    }

    public boolean isStartTile() {
        return isStartTile;
    }

    public void setCharacter(CharacterEntity character) {
        this.character = character;
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return "TileModel [character=" + character.toString() + ", isStartTile=" + isStartTile + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TileModel && Objects.equals(character, ((TileModel) o).character) && isStartTile == ((TileModel) o).isStartTile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character,isStartTile);
    }
}
