package com.bteam.common.services;

import java.util.ArrayList;
import java.util.List;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Player;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.utils.Pair;

/**
 * Service for turn-based logic checks.
 * <p>
 * A service class providing services to make checks which are
 * necessary for each turn, namely movement, attacks, character deaths
 * and winning the game.
 *
 * @author Jacinto, Albano
 * @version 1.0
 */
public class TurnLogicService {

    private TurnLogicService() {}

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
     * @param List of characters and their intended movement or {@code null}, if they don't move
     * @return Array of illegal movers
     */
    public static CharacterEntity[] checkForIllegalMovement(MovementDataModel[] intendedMovements) {
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

    /**
     * Check if a Character has preformed an illegal attack. <br/>
     * - Out of Bounds (Grid map) <br />
     * - Forbidden Attack Pattern
     *
     * @param character The character to be tested
     * @return True if attack is legal, or False if not
     */
    public static CharacterEntity[] checkForIllegalAttacks(CharacterEntity character, AttackDataModel attacks) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        // Try out attack character, perhaps catch exceptions and if so, return false
        // Same for movement patterns etc.
        return illegalAttackers.toArray(new CharacterEntity[illegalAttackers.size()]);
    }

    /**
     * Apply all movement to the characters
     *
     * @return Array of dead characters as {@link CharacterEntity} or empty list
     */
    public static CharacterEntity[] applyMovement(MovementDataModel[] intendedMovements) {

    }

    /**
     * Check if characters died from attacks.
     *
     * @return Array of dead characters as {@link CharacterEntity} or empty list
     */
    public static CharacterEntity[] checkForDeaths(MovementDataModel[] movements, AttackDataModel[] attacks) {
        List<CharacterEntity> deadCharacters = new ArrayList<>();
        // Apply all movements, check if any attack will kill
        return deadCharacters.toArray(new CharacterEntity[deadCharacters.size()]);
    }

    /**
     * Checks if a player (or both) have died.
     * <p>
     * If one player dies, the other is returned as the winner
     * This method won't check for a draw, since that means no
     * {@link CharacterEntity}'s left to pass.
     *
     * @param players The two players competing
     * @return The id of the winning player or {@code null} if no result.
     */
    public static String[] checkForWinner(Pair<Player, Player> players) {
        List<CharacterEntity> deadCharacters = new ArrayList<>();
        // Check if there are no Entities left
        return deadCharacters.toArray(new CharacterEntity[deadCharacters.size()]);
    }
    /**
     * List all characters that are moving to the same position
     *
     * @param characterMovements characters and their respective movement to check, {@code null} for no movement
     * @return Array of characters that are moving to the same position
     */

    private static CharacterEntity[] movingToSamePosition(Pair<CharacterEntity, MovementDataModel>[] characterMovements) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks with methods below
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**
     * List all characters that are moving to a position where a non-moving character is.
     *
     * @param characterEntities characters to check
     * @param movement Movement of the character
     * @return Array of characters that are moving to an occupied position
     */
    private static CharacterEntity[] movingToOccupiedPosition(
            CharacterEntity[] characterEntities,
            MovementDataModel movement
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**
     * Check if a character is moving out of bounds
     *
     * @param character The character to be tested
     * @param movement movement of the character
     * @return True, if character tries moving out of bounds
     */
    private static boolean isOutOfBounds(CharacterEntity character, MovementDataModel movement) {
        // Check if character is allowed to move
        return true;
    }

    /**
     * Check if a character is moving outside its defined movement patterns
     * @param character
     * @param movement
     * @return True, if movement is out of bounds
     */
    private static boolean movingOutsideMovementPatterns(CharacterEntity character, MovementDataModel movement) {
        // Check if the pattern is available in characters moveset
        return true;
    }

}