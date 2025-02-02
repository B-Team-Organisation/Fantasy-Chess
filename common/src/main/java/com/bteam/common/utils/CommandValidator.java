package com.bteam.common.utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.common.models.TurnResult;
import com.bteam.common.models.ValidationResult;

import static com.bteam.common.utils.RelationUtils.groupMovesByPlayerId;
import static com.bteam.common.utils.RelationUtils.getIdCharacterMap;
import static com.bteam.common.utils.RelationUtils.reversePatternServiceArray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A service class providing methods for move validation
 *
 * @author Jacinto, Albano
 * @version 1.0
 */
public class CommandValidator {

    /**
     * private constructor for hiding implicit public one
     */
    private CommandValidator() {}

    /**
     * Test all commands for their legality.
     * <p>
     * Check for any illegal movements using {@link #validateMovements},
     * {@link #validateAttacks} and {@link #validateSingleCommandsOnly}
     * and collect movement conflicts ("bounces") between opposing players
     * using {@link #opposingPlayersMovingToSamePosition}
     *
     * @param characters all characters
     * @param intendedMovements all intended movements
     * @param intendedAttacks all intended attacks
     * @param gridService gridService containing the playing field
     * @param hostID the playerID of the host
     * @return {@link TurnResult} with unaltered characters but only
     * valid movements and attacks as well as a list of movement conflicts
     * between players
     */
    public static ValidationResult validateCommands(
            List<CharacterEntity> characters,
            List<MovementDataModel> intendedMovements,
            List<AttackDataModel> intendedAttacks,
            GridService gridService,
            String hostID
    ) {



        Pair<List<MovementDataModel>, List<AttackDataModel>> cleaned = validateSingleCommandsOnly(
          intendedMovements, intendedAttacks
        );
        List<MovementDataModel> cleanedMovements = cleaned.getFirst();
        List<AttackDataModel> cleanedAttacks = cleaned.getSecond();

        List<MovementDataModel> validMovements = validateMovements(cleanedMovements, characters, gridService, hostID);
        List<AttackDataModel> validAttacks = validateAttacks(cleanedAttacks, characters, gridService, hostID);

        List<PairNoOrder<MovementDataModel, MovementDataModel>> movementConflicts = opposingPlayersMovingToSamePosition(
                characters, validMovements
        );

        validMovements.removeIf( // remove movementConflicts from movements
                movement -> movementConflicts.stream()
                        .anyMatch(conflict -> conflict.getFirst().equals(movement) ||
                                conflict.getSecond().equals(movement)
                        )
        );

        return new ValidationResult(movementConflicts, validMovements, validAttacks);
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
     * @param gridService grid service containing the playing field
     * @param hostID the playerID of the host
     * @return valid movements, without collisions between opposing players' characters
     * (done separately by {@link #opposingPlayersMovingToSamePosition})
     */
    public static List<MovementDataModel> validateMovements(
            List<MovementDataModel> intendedMovements,
            List<CharacterEntity> characters,
            GridService gridService,
            String hostID
    ) {
        ArrayList<MovementDataModel> booleanChecked = new ArrayList<>();
        Map<String, CharacterEntity> idCharacterMap = getIdCharacterMap(characters);

        for (MovementDataModel intendedMove : intendedMovements) {
            CharacterEntity character = idCharacterMap.get(intendedMove.getCharacterId());
            if (
                    character != null
                    && movingInsideBounds(intendedMove, gridService)
                    && movingInsideMovementPattern(intendedMove, character, hostID)
            ) {
                booleanChecked.add(intendedMove);
            }
        }

        List<MovementDataModel> noSamePositions = (validateMovingToSamePosition(booleanChecked, characters));

        return validateMovingToOccupiedPosition(noSamePositions, characters);
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
     * @param hostID the playerID of the host
     * @return a list of legal attacks
     */
    public static List<AttackDataModel> validateAttacks(
            List<AttackDataModel> intendedAttacks,
            List<CharacterEntity> characters,
            GridService gridService,
            String hostID
    ) {
        ArrayList<AttackDataModel> legalAttacks = new ArrayList<>();

        Map<String, CharacterEntity> idCharacterMap = getIdCharacterMap(characters);

        for (AttackDataModel intendedAttack : intendedAttacks) {
            CharacterEntity attacker = idCharacterMap.get(intendedAttack.getAttacker());
            if (
                    attackInsideBounds(intendedAttack, gridService)
                    && attackingInsideAttackPattern(intendedAttack, attacker, hostID)
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
     * Test if opposing players are moving their characters to the same position.
     * <p>
     * The result will only contain pairs, since the input should either be preprocessed by
     * {@link #validateMovingToSamePosition} or be free of such problems in the first place
     *
     * @param characterEntities The character entities (for player reference)
     * @param intendedMovements All movements from two players
     * @return Movement conflicts in the form of {@link PairNoOrder}'s of {@link MovementDataModel}'s
     */
    public static List<PairNoOrder<MovementDataModel, MovementDataModel>> opposingPlayersMovingToSamePosition(
            List<CharacterEntity> characterEntities,
            List<MovementDataModel> intendedMovements
    ) {
        Map<String, ArrayList<MovementDataModel>> playerCharacters = groupMovesByPlayerId(intendedMovements,characterEntities);

        String[] playerIds = playerCharacters.keySet().toArray(new String[0]);
        if (playerIds.length != 2) return new ArrayList<>();

        List<MovementDataModel> movementsPlayer1 = playerCharacters.get(playerIds[0]);
        List<MovementDataModel> movementsPlayer2 = playerCharacters.get(playerIds[1]);

        return movementsPlayer1.stream()
                .flatMap(movementPlayer1 -> movementsPlayer2.stream()
                .filter(movementPlayer2 -> movementPlayer1.getMovementVector().equals(movementPlayer2.getMovementVector()))
                        .map(movementPlayer2 -> new PairNoOrder<>(movementPlayer1, movementPlayer2))
                )
                .collect(Collectors.toList());
    }

    /**
     * Test if a character is moving outside their allowed movement patterns
     *
     * @param intendedMovement the intended movement
     * @param character The moving character
     * @param hostID the playerID of the hosting player
     * @return true if moving outside allowed patterns, false otherwise
     */
    public static boolean movingInsideMovementPattern(
            MovementDataModel intendedMovement,
            CharacterEntity character,
            String hostID
    ) {

        if (intendedMovement == null) return false;

        Vector2D movementVector = intendedMovement.getMovementVector();
        PatternService[] movementServices = character.getCharacterBaseModel().getMovementPatterns();

        if (hostID.equals(character.getPlayerId())) {
                movementServices = reversePatternServiceArray(movementServices);
        }

        for (PatternService movementService : movementServices) {
            for (Vector2D allowedMove : movementService.getPossibleTargetPositions(character.getPosition())) {
                if (allowedMove.equals(movementVector)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Test if character is moving inside the grid.
     *
     * @param intendedMovement the movement
     * @param gridService grid service containing the grid
     * @return the movement
     */
    public static boolean movingInsideBounds(
            MovementDataModel intendedMovement,
            GridService gridService
    ) {
        int moveX = intendedMovement.getMovementVector().getX();
        int moveY = intendedMovement.getMovementVector().getY();
        return !gridService.checkPositionInvalid(new Vector2D(moveX, moveY));
    }

    /**
     * Test if multiple characters are moving to the same position.
     *
     * @param intendedMovements all movements
     * @param characters all characters
     * @return all moves that are not to the same position
     */
    public static List<MovementDataModel> validateMovingToSamePosition(
            List<MovementDataModel> intendedMovements, List<CharacterEntity> characters
    ) {
        ArrayList<MovementDataModel> legalMoves = new ArrayList<>();

        Map<String,ArrayList<MovementDataModel>> playerMoves = groupMovesByPlayerId(intendedMovements, characters);

        for (String playerId : playerMoves.keySet()) {
            List<MovementDataModel> moves = playerMoves.get(playerId);

            List<Vector2D> positions = moves.stream()
                    .map(MovementDataModel::getMovementVector)
                    .collect(Collectors.toList());

            List<Vector2D> duplicatePositions = positions.stream()
                    .filter(v -> Collections.frequency(positions, v) > 1)
                    .collect(Collectors.toList());

            for (MovementDataModel intendedMovement : moves) {
                if (!duplicatePositions.contains(intendedMovement.getMovementVector())) {
                    legalMoves.add(intendedMovement);
                }
            }
        }

        return legalMoves;
    }

    /**
     * Test if the characters are moving to a position that is already occupied.
     * <p>
     * Moving to a position that would be freed in the same round is also forbidden.
     *
     * @param intendedMovements all movements
     * @param characters all characters
     * @return all moves which are not trying to move to an occupied position
     */
    public static List<MovementDataModel> validateMovingToOccupiedPosition(
            List<MovementDataModel> intendedMovements,
            List<CharacterEntity> characters
    ) {
        ArrayList<MovementDataModel> legalMoves = new ArrayList<>();

        List<Vector2D> positions = characters.stream()
                .map(CharacterEntity::getPosition)
                .collect(Collectors.toList());

        for (MovementDataModel intendedMovement : intendedMovements) {
            if(!positions.contains(intendedMovement.getMovementVector())) {
                legalMoves.add(intendedMovement);
            }
        }

        return legalMoves;
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
     * @param attacker The attacking character
     * @param hostID the playerID of the hosting player
     * @return true, if attacking inside allowed attack patterns, else false
     */
    public static boolean attackingInsideAttackPattern(
            AttackDataModel attack,
            CharacterEntity attacker,
            String hostID
    ) {

        Vector2D attackPosition = attack.getAttackPosition();
        PatternService[] attackServices = attacker.getCharacterBaseModel().getAttackPatterns();

        if (hostID.equals(attacker.getPlayerId())) {
            attackServices = reversePatternServiceArray(attackServices);
        }

        for (PatternService attackService : attackServices) {
            for (Vector2D allowedAttack : attackService.getPossibleTargetPositions(attacker.getPosition())) {
                if (allowedAttack.equals(attackPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

}