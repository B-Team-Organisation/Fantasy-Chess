package com.bteam.common.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.utils.Pair;

import java.util.List;

public class TurnResult {
    private  List<CharacterEntity> updatedCharacters;
    private List<Pair<CharacterEntity, CharacterEntity>> movementConflicts;

    public TurnResult(List<CharacterEntity> updatedCharacters, List<Pair<CharacterEntity, CharacterEntity>> movementConflicts) {
        this.updatedCharacters = updatedCharacters;
        this.movementConflicts = movementConflicts;
    }

    public List<CharacterEntity> getUpdatedCharacters() {
        return updatedCharacters;
    }

    public List<Pair<CharacterEntity, CharacterEntity>> getMovementConflicts() {
        return movementConflicts;
    }
}
