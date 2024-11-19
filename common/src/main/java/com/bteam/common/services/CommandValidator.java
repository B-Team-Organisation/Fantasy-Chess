package com.bteam.common.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;

import java.util.ArrayList;
import java.util.List;

public class CommandValidator {

    /**
     * Test all commands for their legality.
     * <p>
     * Check for any illegal movements using {@link #validateMovements},
     * {@link #validateAttacks} and {@link #validateSingleCommandsOnly}
     *
     * @param characters all characters
     * @param intendedMovements all intended movements
     * @param intendedAttacks all intended attacks
     * @param grid the playing field
     * @return list of all characters that are using illegal commands
     */
    public List<CharacterEntity> validateCommands(
            List<CharacterEntity> characters,
            List<MovementDataModel> intendedMovements,
            List<AttackDataModel> intendedAttacks,
            GridModel grid
    ) {
        ArrayList<CharacterEntity> charactersWithIllegalCommands = new ArrayList<>();

        charactersWithIllegalCommands.addAll(validateMovements(intendedMovements, characters, grid));
        charactersWithIllegalCommands.addAll(validateAttacks(intendedAttacks, grid));
        charactersWithIllegalCommands.addAll(validateSingleCommandsOnly(intendedMovements, intendedAttacks));

        return charactersWithIllegalCommands;
    }

    /**
     * Check a List of {@link CharacterEntity}'s for illegal movement.
     * <p>
     * Check for any of the following illegal movements:
     * <p><ul>
     * <li>Trying to move multiple characters to the same position
     * <li>Trying to move to where another character already is
     * <li>Out of bounds regarding grid map
     * <li>Forbidden movement pattern based on an entities {@link CharacterDataModel}
     * and their position based on the {@code characters} argument
     * </ul>
     *
     * @param intendedMovements list of movements
     * @param characters list of all characters, including those that aren't moved
     * @param grid the playing field
     * @return a list of illegal movers
     */
    public static List<CharacterEntity> validateMovements(
            List<MovementDataModel> intendedMovements,
            List<CharacterEntity> characters,
            GridModel grid
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();

        for (MovementDataModel intendedMovement : intendedMovements) {
            // ToDo: character for movingOutsideMovementPattern
            if (movementOutOfBounds(intendedMovement, grid) || movingOutsideMovementPattern(intendedMovement, null)) {
                illegalMovers.add(intendedMovement.getCharacterEntity());
            }
        }

        illegalMovers.addAll(movingToSamePosition(intendedMovements));
        illegalMovers.addAll(movingToOccupiedPosition(intendedMovements, characters));

        return illegalMovers;
    }

    /**
     * Check if a Character has preformed an illegal attack. <br/>
     * <p>
     * Check for any of the following illegal attacks:
     * <p><ul>
     * <li>Out of Bounds (Grid map)
     * <li>Forbidden Attack Pattern
     * </ul>
     *
     * @param intendedAttacks list of attacks
     * @param grid the playing field
     * @return a list of illegal movers
     */
    public static List<CharacterEntity> validateAttacks(
            List<AttackDataModel> intendedAttacks,
            GridModel grid
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();

        for (AttackDataModel intendedAttack : intendedAttacks) {
            // ToDo replace null with actual character
            if (attackOutOfBounds(intendedAttack, grid) || attackingOutsideAttackPattern(intendedAttack, null)) {
                illegalAttackers.add(intendedAttack.getAttacker());
            }
        }


        return illegalAttackers;
    }

    /**
     * Tests if there are multiple commands towards the same entity.
     *
     * @param intendedMovements the intended movements
     * @param intendedAttacks the intended attacks
     * @return a list of all entities that have the multiple commands directed towards them.
     */
    private static List<CharacterEntity> validateSingleCommandsOnly(
        List<MovementDataModel> intendedMovements,
        List<AttackDataModel> intendedAttacks
    ) {
        ArrayList<CharacterEntity> multipleCommandEntities = new ArrayList<>();
        return multipleCommandEntities;
    }

    /**
     * Test if character is moving outside their allowed movement patterns
     *
     * @param intendedMovement the intended movement
     * @param character the character to apply the movement to
     * @return true if moving outside allowed patterns, false otherwise
     */
    private static boolean movingOutsideMovementPattern(
            MovementDataModel intendedMovement,
            CharacterEntity character
    ) {
        return false;
    }

    /**
     * Test if character is moving outside the grid
     *
     * @param intendedMovement the movement
     * @param grid the grid
     * @return the movement
     */
    private static boolean movementOutOfBounds(
            MovementDataModel intendedMovement,
            GridModel grid
    ) {
        return false;
    }

    /**
     * Test if multiple characters are moving to the same position
     *
     * @param intendedMovements all movements
     * @return a list of characters that are moving to the same spot
     */
    private static List<CharacterEntity> movingToSamePosition(List<MovementDataModel> intendedMovements) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if the characters are moving to a position that is already occupied
     *
     * @param movements all movements
     * @param characters all characters
     * @return a list of characters that are moving to an occupied position
     */
    private static List<CharacterEntity> movingToOccupiedPosition(
            List<MovementDataModel> movements,
            List<CharacterEntity> characters
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if an attack is applied outside the grid
     *
     * @param intendedAttack intended attack
     * @param grid the playing field
     * @return true, if out of bounds, false otherwise
     */
    private static boolean attackOutOfBounds(
            AttackDataModel intendedAttack,
            GridModel grid
    ) {
        return false;
    }

    /**
     * Test if character is attacking outside their allowed attack patterns
     *
     * @param attack the attack
     * @param characters the available characterEntities
     * @return true, if attacking outside allowed pattern, false otherwise
     */
    private static boolean attackingOutsideAttackPattern(
        AttackDataModel attack,
        CharacterEntity characters
    ) {
        return false;
    }

}
