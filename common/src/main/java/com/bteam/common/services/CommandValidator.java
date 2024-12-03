package com.bteam.common.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.common.utils.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A service class providing methods for move validation
 *
 * @author Jacinto
 * @version 1.0
 */
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
     * @param gridService gridService containing the playing field
     * @return list of all characters that are using illegal commands
     */
    public static TurnResult validateCommands(
            List<CharacterEntity> characters,
            List<MovementDataModel> intendedMovements,
            List<AttackDataModel> intendedAttacks,
            GridService gridService
    ) {
        Pair<List<MovementDataModel>, List<AttackDataModel>> cleaned = validateSingleCommandsOnly(
          intendedMovements, intendedAttacks
        );
        List<MovementDataModel> cleanedMovements = cleaned.getFirst();
        List<AttackDataModel> cleanedAttacks = cleaned.getSecond();
        //List<MovementDataModel> validMovements = validateMovements(cleanedMovements, characters, gridService.getGridModel());
        List<AttackDataModel> validAttacks = validateAttacks(cleanedAttacks, characters, gridService);
        //List<MovementDataModel> movementConflicts = opposingPlayersMovingToSamePosition(validMovements));
        //remove movementConflicts from validAttacks and validMovements

        return new TurnResult(characters, null, null, validAttacks);
    }

    /**
     * Check a List of movements.
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
     * @return valid movements, extra method for collisions
     */
    public static List<CharacterEntity> validateMovements(
            List<MovementDataModel> intendedMovements,
            List<CharacterEntity> characters,
            GridModel grid
    ) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();

        for (MovementDataModel intendedMovement : intendedMovements) {
            // ToDo: character for movingOutsideMovementPattern
            if (movementOutOfBounds(intendedMovement, new GridService(grid)) || movingOutsideMovementPattern(intendedMovement, null)) {
                //illegalMovers.add(intendedMovement.getCharacterEntity());
            }
        }

        illegalMovers.addAll(movingToSamePosition(intendedMovements));
        illegalMovers.addAll(movingToOccupiedPosition(intendedMovements, characters));

        return illegalMovers;
    }

    /**
     * Check the validity of attacks and only return those that are legal <br/>
     * <p>
     * Check for any of the following illegal attacks:
     * <p><ul>
     * <li>Out of Bounds (Grid map)
     * <li>Forbidden Attack Pattern
     * </ul>
     *
     * @param intendedAttacks list of attacks
     * @param characters list of all characters
     * @param gridService grid service containing the playing field
     * @return a list of legal attacks
     */
    public static List<AttackDataModel> validateAttacks(
            List<AttackDataModel> intendedAttacks,
            List<CharacterEntity> characters,
            GridService gridService
    ) {
        ArrayList<AttackDataModel> legalAttacks = new ArrayList<>();

        for (AttackDataModel intendedAttack : intendedAttacks) {
            if (
                    attackInsideBounds(intendedAttack, gridService)
                    && attackingInsideAttackPattern(intendedAttack, characters)
            ) {
                legalAttacks.add(intendedAttack);
            }
        }

        return legalAttacks;
    }

    /**
     * Makes sure there aren't multiple commands to the same entity.
     * <p>
     * If there are multiple commands to one entity, they are all removed.
     *
     * @param intendedMovements the intended movements
     * @param intendedAttacks the intended attacks
     * @return pair of cleaned MovementDataModels and AttackDataModels
     */
    public static Pair<List<MovementDataModel>, List<AttackDataModel>> validateSingleCommandsOnly(
            List<MovementDataModel> intendedMovements,
            List<AttackDataModel> intendedAttacks
    ) {

        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> duplicates = new ArrayList<>();

        for (MovementDataModel intendedMove : intendedMovements) {
            if (ids.contains(intendedMove.getCharacterId())) {
                duplicates.add(intendedMove.getCharacterId());
            }
            ids.add(intendedMove.getCharacterId());
        }

        for (AttackDataModel intendedAttack : intendedAttacks) {
            if (ids.contains(intendedAttack.getAttacker())) {
                duplicates.add(intendedAttack.getAttacker());
            }
            ids.add(intendedAttack.getAttacker());
        }

        ArrayList<MovementDataModel> cleanedMoves = new ArrayList<>();
        for (MovementDataModel intendedMove : intendedMovements) {
            if (!duplicates.contains(intendedMove.getCharacterId())) {
                cleanedMoves.add(intendedMove);
            }
        }

        ArrayList<AttackDataModel> cleanedAttacks = new ArrayList<>();
        for (AttackDataModel intendedAttack : intendedAttacks) {
            if (!duplicates.contains(intendedAttack.getAttacker())) {
                cleanedAttacks.add(intendedAttack);
            }
        }
        return new Pair<>(cleanedMoves,cleanedAttacks);

    }

    /**
     * Test if opposing players are moving their characters to the same position
     * <p>
     * The result will only contain pairs, since the input should either be preprocessed by
     * {@link #movingToSamePosition} or be free of such problems in the first place
     *
     * @param intendedMovements All movements from both players
     * @return Movement conflicts in the form of {@link Pair}'s of {@link MovementDataModel}'s
     */
    public static List<Pair<MovementDataModel, MovementDataModel>> opposingPlayersMovingToSamePosition(
            List<MovementDataModel> intendedMovements,
            List<AttackDataModel> attackDataModel
    ) {
        return null;
    }

    /**
     * Test if a character is moving outside their allowed movement patterns
     *
     * @param intendedMovement the intended movement
     * @param character the character to apply the movement to
     * @return true if moving outside allowed patterns, false otherwise
     */
    public static boolean movingOutsideMovementPattern(
            MovementDataModel intendedMovement,
            CharacterEntity character
    ) {
        return false;
    }

    /**
     * Test if character is moving outside the grid.
     *
     * @param intendedMovement the movement
     * @param gridService grid service containing the grid
     * @return the movement
     */
    public static boolean movementOutOfBounds(
            MovementDataModel intendedMovement,
            GridService gridService
    ) {
        return false;
    }

    /**
     * Test if multiple characters are moving to the same position.
     *
     * @param intendedMovements all movements
     * @return a list of characters that are moving to the same spot
     */
    public static List<CharacterEntity> movingToSamePosition(List<MovementDataModel> intendedMovements) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if the characters are moving to a position that is already occupied.
     *
     * @param movements all movements
     * @param characters all characters
     * @return a list of characters that are moving to an occupied position
     */
    public static List<CharacterEntity> movingToOccupiedPosition(
            List<MovementDataModel> movements,
            List<CharacterEntity> characters
    ) {
        ArrayList<CharacterEntity> illegalAttackers = new ArrayList<>();
        return illegalAttackers;
    }

    /**
     * Test if an attack is applied outside the grid.
     *
     * @param intendedAttack intended attack
     * @param gridService gridService containing the playing field
     * @return true, if inside bounds, false otherwise
     */
    public static boolean attackInsideBounds(AttackDataModel intendedAttack, GridService gridService) {
        int attackX = intendedAttack.getAttackPosition().getX();
        int attackY = intendedAttack.getAttackPosition().getY();
        return !gridService.checkPositionInvalid(new Vector2D(attackX, attackY));
    }

    /**
     * Test if character is attacking inside their allowed attack patterns.
     *
     * @param attack the attack
     * @param characters the available characterEntities
     * @return true, if attacking inside allowed attack patterns, else false
     */
    public static boolean attackingInsideAttackPattern(
            AttackDataModel attack,
            List<CharacterEntity> characters
    ) {

        if (attack == null || characters == null) {
            return false;
        }

        List<CharacterEntity> availableCharacters = getCharactersWithId(characters, attack.getAttacker());
        if (availableCharacters.size() != 1) {
            return false;
        }

        CharacterEntity character = availableCharacters.getFirst();
        Vector2D attackPosition = attack.getAttackPosition();
        PatternService[] attackServices = character.getCharacterBaseModel().getAttackPatterns();

        for (PatternService attackService : attackServices) {
            for (Vector2D allowedAttack : attackService.getPossibleTargetPositions(character.getPosition())) {
                if (allowedAttack.equals(attackPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get the matching characterEntity given an id.
     *
     * @param characterEntities The entities
     * @param characterId The id to search for
     * @return List of all matching entities
     */
    private static List<CharacterEntity> getCharactersWithId(
            List<CharacterEntity> characterEntities, String characterId
    ) {
        ArrayList<CharacterEntity> charactersWithId = new ArrayList<>();
        for (CharacterEntity character : characterEntities) {
            if (characterId.equals(character.getId())) {
                charactersWithId.add(character);
            }
        }
        return charactersWithId;
    }

}
