package com.bteam.common.utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.PatternService;
import com.bteam.common.models.Vector2D;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class containing helper methods for working with the relationships between classes.
 *
 * @author Jacinto
 * @version 1.2
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

    public static Map<String, CharacterEntity> getIdCharacterMap(List<CharacterEntity> characterEntities) {
        return characterEntities.stream().collect(Collectors.toMap(
                CharacterEntity::getId, characterEntity -> characterEntity
        ));
    }

    /**
     * Get moves by playerId
     * @return HashMap with {@code playerId : List<CharacterEntities>}
     */
    public static Map<String, ArrayList<MovementDataModel>> groupMovesByPlayerId(
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

    /**
     * Revert an array of {@link PatternService}s, creating a new List where the Patterns are reversed.
     *
     * @return A new array with new PatternService Objects which have reversed patterns,
     * using the {@link PatternService#reversePattern() method}
     */
    public static PatternService[] reversePatternServiceArray(PatternService[] patternServices) {
        PatternService[] reversedPatternServices = new PatternService[patternServices.length];
        for (int i = 0; i < patternServices.length; i++) {
                reversedPatternServices[i] = patternServices[i].reversePattern();
        }
        return reversedPatternServices;
    }

    /**
     * Get a string visualization of a vector array representing a pattern.
     * <p />
     * The pattern will be displayed around the middle position in all directions,
     * with x marking all vertices from the array and P for the middle.
     * If middle is also part of the array, it'll be marked as + instead of 0.
     * Example: [x: 1, y: 1, x: 1, 0] ->
     * "xx \n 0 \n   "
     * @param vectorArray The array to represent as a string
     * @param middle the player position / middle of the pattern
     * @return the string representation
     */
    public static String vectorArrayToPatternString(Vector2D[] vectorArray, Vector2D middle) {
        if (vectorArray == null) return "";

        StringBuilder result = new StringBuilder();

        Vector2D[] adjustedForMiddle = Arrays.stream(vectorArray)
                .map(vector -> vector.subtract(middle))
                .toArray(Vector2D[]::new);

        // size of the pattern in x and y direction
        int size = Arrays.stream(adjustedForMiddle)
                .flatMapToInt(vector -> Arrays.stream(
                        new int[]{Math.abs(vector.getX()), Math.abs(vector.getY())})
                )
                .max()
                .orElse(0);

        List<Vector2D> vectorList = Arrays.stream(adjustedForMiddle).toList();

        for (int y = size; y>=-size; y--) {
            result.append("\n");
            for (int x = size; x>=-size; x--) {
                Vector2D position = new Vector2D(x, y);
                if (x==0 && y==0) {
                    if (vectorList.contains(position)) {
                        result.append("+");
                    } else {
                        result.append("P");
                    }
                } else if (vectorList.contains(position)) {
                    result.append("x");
                } else {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

}
