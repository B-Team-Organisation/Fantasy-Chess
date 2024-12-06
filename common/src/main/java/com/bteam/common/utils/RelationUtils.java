package com.bteam.common.utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.MovementDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class containing helper methods for working with the relationships between classes.
 *
 * @author Jacinto
 * @version 1.0
 */
public class RelationUtils {

    private RelationUtils() {}

    /**
     * Get the matching characterEntity given an id.
     *
     * @param characterEntities The entities
     * @param characterId The id to search for
     * @return The matching character
     */
    public static CharacterEntity getCharacterWithId(
            List<CharacterEntity> characterEntities, String characterId
    ) {
        for (CharacterEntity character : characterEntities) {
            if (characterId.equals(character.getId())) {
                return character;
            }
        }
        return null;
    }

    /**
     * Get moves by playerId
     * @return HashMap with {@code playerId : List<CharacterEntities>}
     */
    public static HashMap<String, ArrayList<MovementDataModel>> groupMovesByPlayerId(
            List<MovementDataModel> intendedMovements, List<CharacterEntity> characterEntities
    ) {
        HashMap<String, ArrayList<MovementDataModel>> characterById = new HashMap<>();
        for (MovementDataModel intendedMovement : intendedMovements) {
            CharacterEntity character = getCharacterWithId(characterEntities, intendedMovement.getCharacterId());
            if (character == null) continue;
            characterById.computeIfAbsent(character.getPlayerId(), key -> new ArrayList<>()).add(intendedMovement);
        }
        return characterById;
    }
}
