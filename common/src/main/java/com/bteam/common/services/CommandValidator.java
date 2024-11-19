package com.bteam.common.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;

import java.util.ArrayList;
import java.util.List;

public class CommandValidator {

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
     * @param intendedMovements List of movements
     * @param characters List of all characters, including those that aren't moved
     * @param grid The playing field
     * @return A list of illegal movers
     */
    public static List<CharacterEntity> validateMovements(
            List<MovementDataModel> intendedMovements,
            List<CharacterEntity> characters,
            GridModel grid
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // Try out attack character, perhaps catch exceptions and if so, return false
        // Same for movement patterns etc.
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
     * @param intendedAttacks List of attacks
     * @param characters List of all characters, including those that aren't attacking
     * @return A list of illegal movers
     */
    public static List<CharacterEntity> validateAttack(
            List<AttackDataModel> intendedAttacks,
            List<CharacterEntity> characters
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if character is moving outside their allowed movement patterns
     *
     * @param intendedMovement The intended movement
     * @param character The character to be moved
     * @return true if moving outside allowed patterns, false otherwise
     */
    private boolean movingOutsideMovementPattern(
            MovementDataModel intendedMovement,
            CharacterEntity character
    ) {
        return false;
    }

    /**
     * Test if character is moving outside the grid
     *
     * @param grid The grid
     * @param intendedMovement The movement
     * @return The movement
     */
    private boolean movementOutOfBounds(
            GridModel grid,
            MovementDataModel intendedMovement
    ) {
        return false;
    }

    /**
     * Test if multiple characters are moving to the same position
     *
     * @param intendedMovements All movements
     * @return A list of characters that are moving to the same spot
     */
    private List<CharacterEntity> movingToSamePosition(
            List<MovementDataModel> intendedMovements
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if the characters are moving to a position that is already occupied
     *
     * @param movements All movements
     * @param characters All characters
     * @return A list of characters that are moving to an occupied position
     */
    private List<CharacterEntity> movingToOccupiedPosition(
            List<MovementDataModel> movements,
            List<CharacterEntity> characters
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if multiple movement commands apply to the same entity
     *
     * @param movements All movements
     * @return List of characters that have multiple movements to them
     */
    private List<CharacterEntity> multipleMovementsToSameEntity(
            List<MovementDataModel> movements
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if an attack is applied outside the grid
     *
     * @param intendedAttack Intended attack
     * @param grid The playing field
     * @return true, if out of bounds, false otherwise
     */
    private boolean attackOutOfBounds(
            AttackDataModel intendedAttack,
            GridModel grid
    ) {
        return false;
    }

    /**
     * Test if character is attacking outside their allowed attack patterns
     *
     * @param attack The attack
     * @param character The characterEntity
     * @return True, if attacking outside allowed pattern, false otherwise
     */
    private boolean attackingOutsideAttackPattern(
        AttackDataModel attack,
        CharacterEntity character
    ) {
        return false;
    }

    /**
     * Test if multiple attack commands are applied to the same entity
     *
     * @param attacks All attacks
     * @return List of entities that have multiple attacks
     */
    private List<CharacterEntity> multipleAttacksToSameEntity(
            List<AttackDataModel> attacks
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

}
