package com.bteam.common.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class CommandValidator {

    /**
     * Check if a Character has preformed an illegal attack. <br/>
     * - Out of Bounds (Grid map) <br />
     * - Forbidden Attack Pattern
     *
     */
    public static List<CharacterEntity> validateMovements(CharacterEntity character, AttackDataModel attacks) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        // Try out attack character, perhaps catch exceptions and if so, return false
        // Same for movement patterns etc.
        return illegalAttackers.toArray(new CharacterEntity[illegalAttackers.size()]);
    }

    /**
     * Check {@link CharacterEntity}'s for illegal movement.
     * <p>
     * Check for any of the following illegal movements:
     * <p><ul>
     * <li>Trying to move multiple characters to the same position
     * <li>Trying to move to where another character already is
     * <li>Out of bounds regarding grid map
     * <li>Forbidden movement pattern based on an entities {@link CharacterDataModel}
     * </ul>
     *
     */
    public static List<CharacterEntity> validateAttack(MovementDataModel[] intendedMovements) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        for (Pair<CharacterEntity, MovementDataModel> characterMovement : characterMovements) {
            if (movingOutsideMovementPatterns(characterMovement.getFirst(), characterMovement.getSecond())
                    || isOutOfBounds(characterMovement.getFirst(), characterMovement.getSecond())) {
                illegalMovers.add(characterMovement.getFirst());
            }
        }
        illegalMovers.addAll(movingToSamePosition(characterMovements));
        illegalMovers.addAll(movingToOccupiedPosition(characterMovements));
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    private List<CharacterEntity> moving


    private static List<CharacterEntity> movingToOccupiedPosition(
            CharacterEntity[] characterEntities,
            MovementDataModel movement
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    private static boolean movingOutOfBounds(CharacterEntity character, MovementDataModel movement) {
        // Check if character is allowed to move
        return true;
    }

    private static boolean movingOutsideMovementPatterns(CharacterEntity character, MovementDataModel movement) {
        // Check if the pattern is available in characters moveset
        return true;
    }

    private static
}
