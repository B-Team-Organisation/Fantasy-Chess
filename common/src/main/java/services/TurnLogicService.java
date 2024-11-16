package services;

import models.Player;
import utils.Pair;
import entities.CharacterEntity;
import models.AttackDataModel;
import models.MovementDataModel;

import java.util.ArrayList;
import java.util.List;


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

    public enum Winner {
        NONE, PLAYER1, PLAYER2, DRAW;
    }

    /**
     * Check Array of {@link CharacterEntity} for illegal movement.
     * <p><ul>
     * <li>Trying to move multiple characters to the same position
     * <li>Trying to move to where another character already is
     * <li>All checks from {@link #isMovementLegal}
     * </ul>
     *
     * @param players The players for which to test problems
     * @return Array of illegal movers
     */
    public static CharacterEntity[] checkForIllegalMovement(Pair<Player, Player> players) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // test each character for isMovementLegal and add them to illegalMovers
        // check for overlaps listed below
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**
     * List all characters that are moving to the same position
     *
     * @param characterEntities characters to check
     * @return Array of characters that are moving to the same position
     */
    public static CharacterEntity[] movingToSamePosition(CharacterEntity[] characterEntities) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**
     * List all characters that are moving to a position where a non-moving character is.
     *
     * @param characterEntities characters to check
     * @param movement Movement of the character
     * @return Array of characters that are moving to an occupied position
     */
    public static CharacterEntity[] movingToOccupiedPosition(
            CharacterEntity[] characterEntities,
            MovementDataModel movement
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**
     * Check if a Character has preformed legal movement, or not from:
     * <p><ul>
     * <li>Out of Bounds (Grid map)
     * <li>Forbidden Movement Pattern
     * <p></ul>
     *
     * @param character The character to be tested
     * @param movement movement of the character
     * @return True if movement is legal, or False if movement is illegal
     */
    public static boolean isMovementLegal(CharacterEntity character, MovementDataModel movement) {
        return isOutOfBounds(character, movement) || movementOutsideMovementPatterns(character, movement);
    }

    /**
     * Check if a character is moving out of bounds
     *
     * @param character The character to be tested
     * @param movement movement of the character
     * @return True, if character tries moving out of bounds
     */
    public static boolean isOutOfBounds(CharacterEntity character, MovementDataModel movement) {
        // Check if character is allowed to move
        return true;
    }

    /**
     * Check if a character is moving outside its defined movement patterns
     * @param character
     * @param movement
     * @return True, if movement is out of bounds
     */
    public static boolean movementOutsideMovementPatterns(CharacterEntity character, MovementDataModel movement) {
        // Check if the pattern is available in characters moveset
        return true;
    }


    /**
     * Check if a Character has preformed an illegal attack: <br/>
     * - Out of Bounds (Grid map) <br />
     * - Forbidden Attack Pattern
     *
     * @param character The character to be tested
     * @return True if attack is legal, or False if not
     */
    public static boolean isAttackLegal(CharacterEntity character, AttackDataModel attacks) {
        // Try out attack character, catch exceptions and if so, return false
        // Same for movement patterns etc.
        return true;
    }

    /**
     * Check if characters died from attacks.
     *
     * @return Array of dead characters as {@link CharacterEntity} or empty list
     */
    public static CharacterEntity[] checkForDeaths() {
        List<CharacterEntity> deadCharacters = new ArrayList<>();
        // Logic
        return deadCharacters.toArray(new CharacterEntity[deadCharacters.size()]);
    }

    /**
     * Checks if a player (or both) have died.
     * <p>
     * If one player dies, the other is returned as the winner
     * If both players die, a draw is declared
     *
     * @param players The two players competing
     * @return {@link Winner} enum with the winning player, draw or none for no end of game.
     */
    public static Winner checkForWinner(Pair<Player, Player> players) {
        // Check if one player is winning, keep it simple by checking for empty arrays
        return Winner.NONE; // Logic
    }

    //TODO method getAllCharacters

}