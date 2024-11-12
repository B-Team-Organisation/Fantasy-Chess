package services;

import com.bteam.fantasychess_server.utils.Pair;
import entities.CharacterEntity;
import models.MovementDataModel;

/**
 * Service for turn-based logic checks.
 * <p>
 * A service class providing services to make checks which are
 * necessary for each turn, namely movement, attacks and winning
 * the game.
 *
 * @author Jacinto, Albano
 * @version 1.0
 */
public class TurnLogicService {

    public enum Winner {
        NONE, PLAYER1, PLAYER2, DRAW;
    }

    /**
     * Check Array of {@link CharacterEntity} for illegal movement.
     *
     * @return Array of illegal movers
     */
    public CharacterEntity[] checkForIllegalMovement() {
        // Simple loop with ArrayList
        return new CharacterEntity[]{};
    }

    /**
     * Check if a Character has preformed illegal movement: <br/>
     * - Out of Bounds (Grid map) <br />
     * - Forbidden Movement Pattern <br />
     * - Trying to move to where another character already is
     * - Trying to move multiple characters to the same position
     *
     * @param character The character to be tested
     * @return True if movement is legal, or False if movement is illegal
     */
    public boolean isMovementLegal(CharacterEntity character, MovementDataModel movement) {
        // Try to move character, catch exceptions and if so, return false
        // Same for movement patterns etc.
        return true;
    }

    /**
     * Checks if two character are trying to move to the same location.
     * <p>
     * If two characters try to move to the same position, they will eventually be sent back
     * to the start position.
     *
     * @return Array of character pairs that are moving to the same location, or null if not applicable
     */
    public Pair<CharacterEntity, CharacterEntity>[] movingToSameLocation() {
        return null;
    }

    /**
     * Check if a Character has preformed an illegal attack: <br/>
     * - Out of Bounds (Grid map) <br />
     * - Forbidden Attack Pattern
     *
     * @param character The character to be tested
     * @return True if attack is legal, or False if not
     */
    public boolean isAttackLegal(CharacterEntity character) {
        // Try out attack character, catch exceptions and if so, return false
        // Same for movement patterns etc.
        return true;
    }

    /**
     * Check if characters died from attacks.
     *
     * @return Array of dead characters as {@link CharacterEntity} or empty list
     */
    public CharacterEntity[] checkForDeaths() {
        return new CharacterEntity[0]; // Implementation
    }

    /**
     * Checks if a player (or both) have died.
     * <p>
     * If one player dies, the other is returned as the winner
     * If both players die, a draw is declared
     *
     * @return The winning player or DRAW from the {@link Winner} enum,
     * or NONE if no winner
     */
    public Winner checkForWinner() {
        // Check if
        return Winner.NONE; // Logic
    }

}
