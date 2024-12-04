package services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnResult;
import com.bteam.common.utils.Pair;
import com.bteam.common.utils.PairNoOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ListNoOrder;
import utils.TurnResultNoOrder;

import java.util.*;

import static com.bteam.common.services.CommandValidator.*;
import static org.junit.jupiter.api.Assertions.*;

class TestCommandValidator {

    private GridService basicGrid;
    private CharacterDataModel baseModel1;
    private CharacterDataModel baseModel2;
    private CharacterDataModel baseModel3;
    private CharacterDataModel baseModel4;
    private CharacterEntity basicEntity1;
    private CharacterEntity basicEntity2;
    private CharacterEntity basicEntity3;
    private CharacterEntity basicEntity4;
    private CharacterEntity basicEntity5;
    private CharacterEntity basicEntity6;

    @BeforeEach
    void setup() throws PatternShapeInvalidException, InvalidSubpatternMappingException {
        PatternStore mockPatternStore = new PatternStore() {
            private final Map<String, PatternModel> patterns = new HashMap<>() {{
                put(
                        "longRangeAttack",
                        new PatternModel(
                                "   x   \n   x   \n   x   \nxxxxxxx\n   x   \n   x   \n   x   ",
                                new HashMap<>(),
                                "longRangeAttack")
                );
                put(
                        "shortRangeAttack",
                        new PatternModel(
                                "xxx\n x \nxxx",
                                new HashMap<>(),
                                "shortRangeAttack")
                );
                put(
                        "longMovement",
                        new PatternModel(
                                "   x   \n   x   \n   x   \nxxxxxxx\n   x   \n   x   \n   x   ",
                                new HashMap<>(),
                                "longMovement")
                );
                put(
                        "shortMovement",
                        new PatternModel(
                                "xxx\n x \nxxx",
                                new HashMap<>(),
                                "shortMovement"
                        )
                );
                put(
                        "simpleAttack",
                        new PatternModel(
                                "xxx\nxxx\nxxx",
                                new HashMap<>(),
                                "simpleAttack"
                        )
                );
                put(
                        "simpleMovement",
                        new PatternModel(
                                "xxx\nxxx\nxxx",
                                new HashMap<>(),
                                "simpleMovement"
                        )
                );
            }};

            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }
        };

        PatternService longRangeAttackService = new PatternService(
                mockPatternStore.getPatternByName("longRangeAttack"),
                mockPatternStore
        );
        PatternService shortRangeAttackService = new PatternService(
                mockPatternStore.getPatternByName("shortRangeAttack"),
                mockPatternStore
        );
        PatternService simpleAttackService = new PatternService(
                mockPatternStore.getPatternByName("simpleAttack"),
                mockPatternStore
        );
        PatternService longMovementService = new PatternService(
                mockPatternStore.getPatternByName("longMovement"),
                mockPatternStore
        );
        PatternService shortMovementService = new PatternService(
                mockPatternStore.getPatternByName("shortMovement"),
                mockPatternStore
        );
        PatternService simpleMovementService = new PatternService(
                mockPatternStore.getPatternByName("simpleMovement"),
                mockPatternStore
        );

        basicGrid = new GridService(new GridModel(8, 8));
        baseModel1 = new CharacterDataModel(
                "baseModel1", "Test",
                4, 8,
                new PatternService[]{longRangeAttackService},
                new PatternService[]{shortMovementService}
        );
        baseModel2 = new CharacterDataModel(
            "baseModel2", "Test Test",
                    8, 4,
                    new PatternService[]{shortRangeAttackService},
                    new PatternService[]{longMovementService}
        );
        baseModel3 = new CharacterDataModel(
                "baseModel3", "Test Test Test",
                4, 8,
                new PatternService[]{longRangeAttackService, shortMovementService},
                new PatternService[]{shortMovementService, longMovementService}
        );
        baseModel4 = new CharacterDataModel(
                "baseModel4", "Test Test Test Test",
                4, 8,
                new PatternService[]{simpleAttackService},
                new PatternService[]{simpleMovementService}
        );
        basicEntity1 = new CharacterEntity(baseModel1, "baseEntity1", 4, new Vector2D(2, 2), "player1");
        basicEntity2 = new CharacterEntity(baseModel2, "baseEntity2", 2, new Vector2D(7, 7), "player2");
        basicEntity3 = new CharacterEntity(baseModel3, "baseEntity3", 4, new Vector2D(3, 2), "player1");
        basicEntity4 = new CharacterEntity(baseModel4, "baseEntity4", 4, new Vector2D(6, 6), "player2");
        basicEntity5 = new CharacterEntity(baseModel4, "baseEntity5", 4, new Vector2D(0, 0), "player2");
        basicEntity6 = new CharacterEntity(baseModel4, "baseEntity6", 4, new Vector2D(1, 2), "player2");
    }

    /**
     * Command Operations from before:
     * <li>{@link #testValidateMoves}
     * <li>{@link #testValidateAttacks}
     * <li>{@link #testValidateSingleCommandsOnly}
     * <li>{@link #testOpposingPlayersMovingToSamePosition}
     */
    @Test
    void testValidateCommands() {
        List<CharacterEntity> characters = List.of(
                basicEntity1, basicEntity2, basicEntity3, basicEntity4, basicEntity5, basicEntity6
        );

        MovementDataModel validMove1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(2, 1));
        MovementDataModel validMove2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(7, 6));
        MovementDataModel validMove3 = new MovementDataModel(basicEntity3.getId(), new Vector2D(4, 3));
        MovementDataModel moving3Like1 = new MovementDataModel(basicEntity3.getId(), new Vector2D(2, 1));
        MovementDataModel moving3To1 = new MovementDataModel(basicEntity3.getId(), new Vector2D(2, 2));
        MovementDataModel moving2OutOfBounds = new MovementDataModel(basicEntity2.getId(), new Vector2D(7, 10));
        MovementDataModel moving2ForbiddenMovement = new MovementDataModel(basicEntity2.getId(), new Vector2D(5, 4));
        MovementDataModel bounceWith1 = new MovementDataModel(basicEntity6.getId(), new Vector2D(2, 1));
        MovementDataModel doubleMoveOn1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(2, 3));
        MovementDataModel duplicateMove1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(2, 3));

        AttackDataModel validAttack1 = new AttackDataModel(new Vector2D(4, 2), basicEntity1.getId());
        AttackDataModel validAttack2 = new AttackDataModel(new Vector2D(7, 6), basicEntity2.getId());
        AttackDataModel validAttack3 = new AttackDataModel(new Vector2D(3, 1), basicEntity3.getId());
        AttackDataModel validAttack4 = new AttackDataModel(new Vector2D(5, 5), basicEntity4.getId());
        AttackDataModel outOfBounds = new AttackDataModel(new Vector2D(-1, -1), basicEntity5.getId());
        AttackDataModel attack3Forbidden = new AttackDataModel(new Vector2D(6, 6), basicEntity3.getId());

        // Assert no unintentional moves outside movements patterns for tests further down
        List<MovementDataModel> insidePatternsMove = List.of(
                validMove1, validMove2, validMove3, moving3Like1, duplicateMove1,
                moving3To1, moving2OutOfBounds, bounceWith1, doubleMoveOn1
        );
        for (MovementDataModel insidePattern : insidePatternsMove) {
            assertTrue(isMoveInsideCharacterPattern(insidePattern, characters),
                    "Pattern: " + insidePattern
            );
        }
        assertTrue(isMoveInsideCharacterPattern(moving2OutOfBounds, characters));
        //Assert movement is actually outside movement pattern
        assertFalse(isMoveInsideCharacterPattern(moving2ForbiddenMovement, characters));
        //Assert same for attacks
        List<AttackDataModel> insidePatternAttacks = List.of(validAttack1, validAttack2, validAttack3, outOfBounds);
        for (AttackDataModel insidePatternAttack : insidePatternAttacks) {
            assertTrue(isAttackInsideCharacterPattern(insidePatternAttack, characters),
                    "Failed: " + insidePatternAttack);
        }
        assertFalse(isAttackInsideCharacterPattern(attack3Forbidden, characters));

        //empty
        assertEquals(
                    new TurnResultNoOrder(new TurnResult(characters, List.of(), List.of(), List.of())),
                    new TurnResultNoOrder(validateCommands(characters, List.of(), List.of(), basicGrid)
                )
        );
        //all Valid
        assertEquals(
                    new TurnResultNoOrder(
                            new TurnResult(
                                    characters, List.of(), List.of(validMove1, validMove2), List.of(validAttack3)
                            )
                    ),
                    new TurnResultNoOrder(
                            validateCommands(
                                    characters, List.of(validMove1, validMove2), List.of(validAttack3), basicGrid
                            )
                    )
        );
        // Bounce, forbidden attack
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(new PairNoOrder<>(validMove1, bounceWith1)),
                                List.of(), List.of(validAttack2)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, bounceWith1),
                                List.of(validAttack2, attack3Forbidden), basicGrid
                        )
                )
        );
        //non-single command, outOfBoundsAttack
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(),
                                List.of(), List.of(validAttack2)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, doubleMoveOn1),
                                List.of(validAttack2, attack3Forbidden), basicGrid
                        )
                )
        );
        //checks from validateMoves + validAttack4
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(),
                                List.of(validMove2), List.of(validAttack4)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, validMove2, moving3Like1),
                                List.of(validAttack4), basicGrid
                        )
                )
        );
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(),
                                List.of(validMove1), List.of(validAttack4)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, moving3To1),
                                List.of(validAttack4), basicGrid
                        )
                )
        );
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(),
                                List.of(validMove1), List.of(validAttack4)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, moving2OutOfBounds),
                                List.of(validAttack4), basicGrid
                        )
                )
        );
        assertEquals(
                new TurnResultNoOrder(
                        new TurnResult(
                                characters, List.of(),
                                List.of(validMove1), List.of(validAttack4)
                        )
                ),
                new TurnResultNoOrder(
                        validateCommands(
                                characters, List.of(validMove1, moving2ForbiddenMovement),
                                List.of(validAttack4), basicGrid
                        )
                )
        );
    }

    /**
     * Checks by validateMoves:
     * <li>Trying to move multiple characters to the same position
     * <li>Trying to move to where another character already is
     * <li>Out of bounds regarding grid map
     * <li>Forbidden movement pattern based on an entities {@link CharacterDataModel}
     */
    @Test
    void testValidateMoves() {
        List<CharacterEntity> characters = List.of(basicEntity1, basicEntity2, basicEntity3);

        MovementDataModel valid1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(2, 1));
        MovementDataModel valid2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(7, 6));
        MovementDataModel valid3 = new MovementDataModel(basicEntity3.getId(), new Vector2D(4, 3));
        MovementDataModel moving3Like1 = new MovementDataModel(basicEntity3.getId(), new Vector2D(2, 1));
        MovementDataModel moving3To1 = new MovementDataModel(basicEntity3.getId(), new Vector2D(2, 2));
        MovementDataModel moving2OutOfBounds = new MovementDataModel(basicEntity2.getId(), new Vector2D(7, 10));
        MovementDataModel moving2ForbiddenMovement = new MovementDataModel(basicEntity2.getId(), new Vector2D(5, 4));

        // Assert no unintentional moves outside movements patterns for tests further down
        List<MovementDataModel> insidePatternsMove = List.of(valid1, valid2, valid3, moving3Like1, moving3To1, moving2OutOfBounds);
        for (MovementDataModel insidePattern : insidePatternsMove) {
            assertTrue(isMoveInsideCharacterPattern(insidePattern, characters));
        }
        assertTrue(isMoveInsideCharacterPattern(moving2OutOfBounds, characters));
        //Assert movement is actually outside movement pattern
        assertFalse(isMoveInsideCharacterPattern(moving2ForbiddenMovement, characters));

        List<MovementDataModel> allValid = List.of(valid1, valid2, valid3);
        List<MovementDataModel> firstValidButMovingToSamePosition = List.of(valid2, valid1, moving3Like1);
        List<MovementDataModel> firstValidButMovingToOccupied = List.of(valid1, moving3To1);
        List<MovementDataModel> firstValidButMovingOutOfBounds = List.of(valid1, moving2OutOfBounds);
        List<MovementDataModel> firstValidButForbiddenMovement = List.of(valid1, moving2ForbiddenMovement);

        assertEquals(
                new ListNoOrder<>(allValid),
                new ListNoOrder<>(validateMovements(List.copyOf(allValid), characters, basicGrid))
        );
        assertEquals(
                new ListNoOrder<>(List.of(valid2)),
                new ListNoOrder<>(validateMovements(List.copyOf(firstValidButMovingToSamePosition), characters, basicGrid))
        );
        assertEquals(
                new ListNoOrder<>(List.of(valid1)),
                new ListNoOrder<>(validateMovements(List.copyOf(firstValidButMovingToOccupied), characters, basicGrid))
        );
        assertEquals(
                new ListNoOrder<>(List.of(valid1)),
                new ListNoOrder<>(validateMovements(List.copyOf(firstValidButMovingOutOfBounds), characters, basicGrid))
        );
        assertEquals(
                new ListNoOrder<>(List.of(valid1)),
                new ListNoOrder<>(validateMovements(List.copyOf(firstValidButForbiddenMovement), characters, basicGrid))
        );
    }

    @Test
    void testValidateMovingToOccupiedPosition() {
        List<CharacterEntity> characters = List.of(basicEntity1, basicEntity2, basicEntity3);

        MovementDataModel moveInvalid = new MovementDataModel(basicEntity1.getId(), new Vector2D(7, 7));
        MovementDataModel moveValid = new MovementDataModel(basicEntity1.getId(), new Vector2D(4, 4));
        MovementDataModel moveInvalid2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(2, 2));
        MovementDataModel moveValid2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(5, 5));

        assertEquals(List.of(moveValid),
                validateMovingToOccupiedPosition(List.of(moveInvalid, moveValid, moveInvalid2), characters)
        );

        assertEquals(List.of(moveValid, moveValid2),
                validateMovingToOccupiedPosition(List.of(moveValid, moveValid2), characters)
        );

        assertEquals(List.of(), validateMovingToOccupiedPosition(List.of(moveInvalid, moveInvalid2), characters));
    }

    @Test
    void testValidateMovingToSamePosition() {
        List<CharacterEntity> characters = List.of(basicEntity1, basicEntity2, basicEntity3);
        MovementDataModel move1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(1, 1));
        MovementDataModel move2To1Valid = new MovementDataModel(basicEntity2.getId(), new Vector2D(1, 1));
        MovementDataModel move3 = new MovementDataModel(basicEntity3.getId(), new Vector2D(2, 2));
        MovementDataModel move4 = new MovementDataModel(basicEntity3.getId(), new Vector2D(1, 1));

        assertEquals(List.of(move1, move2To1Valid), validateMovingToSamePosition(List.of(move1, move2To1Valid), characters));
        assertEquals(List.of(move1, move3), validateMovingToSamePosition(List.of(move1, move3), characters));
        assertEquals(List.of(move2To1Valid), validateMovingToSamePosition(List.of(move1, move2To1Valid, move4), characters));
    }


    @Test
    void testMovingInsideBounds() {
        List<Vector2D> outOfBoundsPositions = List.of(
                new Vector2D(10, 1), new Vector2D(1, 10), new Vector2D(10, 10),
                new Vector2D(10, -1), new Vector2D(-2, 19), new Vector2D(-1, -1),
                new Vector2D(Integer.MAX_VALUE, 0), new Vector2D(0, Integer.MAX_VALUE),
                new Vector2D(Integer.MIN_VALUE, 0), new Vector2D(0, Integer.MIN_VALUE)
        );

        List<MovementDataModel> movementOutOfBounds = movesFromList(outOfBoundsPositions, basicEntity1.getId());

        movementOutOfBounds.forEach(movement -> assertFalse(
                movingInsideBounds(movement, basicGrid),
                "Expected movement at " + movement.getMovementVector().toString() + " on a grid of " +
                        basicGrid.toString() + " to be rejected."
        ));

        assertTrue(movingInsideBounds(
                        new MovementDataModel(basicEntity1.getId(), new Vector2D(4, 4)), basicGrid),
                "Expected an attack of (4, 4) in a grid of " + basicGrid.toString() + " to be accepted."
        );
    }

    @Test
    void testMovingInsideMovementPattern() {

        List<CharacterEntity> availableCharacters = List.of(basicEntity1, basicEntity2, basicEntity3);
        // Get all possible moves from character1 and character3 (one with 1 pattern and 1 with multiple patterns
        List<Vector2D> character1Moves = Arrays.asList(
                basicEntity1.getCharacterBaseModel().getMovementPatterns()[0].getPossibleTargetPositions(
                        basicEntity1.getPosition()
                )
        );
        ArrayList<Vector2D> character3Moves = new ArrayList<>(Arrays.asList(
                basicEntity3.getCharacterBaseModel().getMovementPatterns()[0].getPossibleTargetPositions(
                        basicEntity3.getPosition()
                )
        ));
        character3Moves.addAll(
                Arrays.asList(
                        basicEntity3.getCharacterBaseModel().getMovementPatterns()[1].getPossibleTargetPositions(
                                basicEntity3.getPosition()
                        )
                )
        );

        List<Vector2D> baseArray = createGridFromTo(-5, -5, 5, 5);
        List<Vector2D> character1Outside = new ArrayList<>(baseArray);
        List<Vector2D> character3Outside = new ArrayList<>(baseArray);

        character1Outside.removeAll(character1Moves);
        character3Outside.removeAll(character3Moves);

        List<MovementDataModel> working1 = movesFromList(character1Moves, basicEntity1.getId());
        List<MovementDataModel> working3 = movesFromList(character3Moves, basicEntity3.getId());
        List<MovementDataModel> failing1 = movesFromList(character1Outside, basicEntity1.getId());
        List<MovementDataModel> failing3 = movesFromList(character3Outside, basicEntity3.getId());

        working1.forEach(movement -> assertTrue(movingInsideMovementPattern(movement, availableCharacters)));
        working3.forEach(movement -> assertTrue(movingInsideMovementPattern(movement, availableCharacters)));
        failing1.forEach(movement -> assertFalse(movingInsideMovementPattern(movement, availableCharacters)));
        failing3.forEach(movement -> assertFalse(movingInsideMovementPattern(movement, availableCharacters)));

    }

    @Test
    void testOpposingPlayersMovingToSamePosition() {

        List<CharacterEntity> characters = List.of(basicEntity1, basicEntity2, basicEntity3);
        // character1 & 3 are player 1111 and character2 is player 2222
        MovementDataModel player1move1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(2, 2));
        MovementDataModel player1move1v1 = new MovementDataModel(basicEntity3.getId(), new Vector2D(3, 3));
        MovementDataModel player1move2 = new MovementDataModel(basicEntity1.getId(), new Vector2D(4, 4));
        MovementDataModel player2move1 = new MovementDataModel(basicEntity2.getId(), new Vector2D(2, 2));
        MovementDataModel player2move2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(4, 4));

        assertEquals(
                List.of(new PairNoOrder<>(player2move1, player1move1), new PairNoOrder<>(player2move2, player1move2)),
                opposingPlayersMovingToSamePosition(characters, List.of(
                        player1move1, player2move1, player1move2, player2move2
                ))
        );

        assertEquals(
                List.of(new PairNoOrder<>(player2move1, player1move1)),
                opposingPlayersMovingToSamePosition(characters, List.of(player1move1, player1move1v1, player2move1))
        );

        assertEquals(
                List.of(),
                opposingPlayersMovingToSamePosition(characters, List.of(player1move1, player1move1v1))
        );

        assertEquals(
                List.of(),
                opposingPlayersMovingToSamePosition(characters, List.of(player1move2, player2move1))
        );

    }

    @Test
    void testValidateSingleCommandsOnly() {

        AttackDataModel attack1 = new AttackDataModel(new Vector2D(0,0), basicEntity1.getId());
        AttackDataModel attack1v2 = new AttackDataModel(new Vector2D(0,0), basicEntity1.getId());
        AttackDataModel attack2 = new AttackDataModel(new Vector2D(0,0), basicEntity2.getId());
        AttackDataModel attack3 = new AttackDataModel(new Vector2D(0,0), basicEntity3.getId());
        
        MovementDataModel move1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(0,0));
        MovementDataModel move1v2 = new MovementDataModel(basicEntity1.getId(), new Vector2D(0,0));
        MovementDataModel move2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(0, 0));
        MovementDataModel move3 = new MovementDataModel(basicEntity3.getId(), new Vector2D(0, 0));

        //Two conflicting attacks, one none conflicting move
        Pair<List<MovementDataModel>, List<AttackDataModel>> result1 =validateSingleCommandsOnly(
                List.of(move2), List.of(attack1,attack1v2)
        );
        assertEquals(List.of(move2), result1.getFirst());
        assertEquals(List.of(), result1.getSecond());

        //Two conflicting moves, one none conflicting attack
        Pair<List<MovementDataModel>,List<AttackDataModel>> result2 = validateSingleCommandsOnly(
                List.of(move1,move1v2), List.of(attack3)
        );
        assertEquals(List.of(), result2.getFirst());
        assertEquals(List.of(attack3), result2.getSecond());

        //Conflicting attack & move, and one non-conflicting attack and move each
        Pair<List<MovementDataModel>,List<AttackDataModel>> result3 = validateSingleCommandsOnly(
                List.of(move1,move3), List.of(attack1,attack2)
        );
        assertEquals(List.of(move3), result3.getFirst());
        assertEquals(List.of(attack2), result3.getSecond());

        //One of each non-conflicting attack and move
        Pair<List<MovementDataModel>,List<AttackDataModel>> result4 = validateSingleCommandsOnly(
                List.of(move1), List.of(attack2)
        );
        assertEquals(List.of(move1), result4.getFirst());
        assertEquals(List.of(attack2), result4.getSecond());

    }

    /**
     * Checks from the method:
     * <li>Out of Bounds (Grid map)
     * <li>Forbidden Attack Pattern
     */
    @Test
    void testValidateAttacks() {
        List<CharacterEntity> availableCharacters = List.of(basicEntity1, basicEntity2, basicEntity3);

        // insideBounds options
        AttackDataModel outOfBoundsAttack = new AttackDataModel(new Vector2D(10, 10), basicEntity1.getId());
        AttackDataModel insideBoundsAttack = new AttackDataModel(new Vector2D(5, 2), basicEntity1.getId());

        // insidePattern options
        List<Vector2D> character1Attacks = Arrays.asList(
                basicEntity1.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(
                        basicEntity1.getPosition()
                )
        );
        List<Vector2D> baseArray = createGridFromTo(-5, -5, 5, 5);
        List<Vector2D> character1Outside = new ArrayList<>(baseArray);
        character1Outside.removeAll(character1Attacks);
        AttackDataModel outsidePatternAttack = attacksFromList(character1Outside, basicEntity1.getId()).getFirst();
        AttackDataModel insidePatternAttack = attacksFromList(character1Attacks, basicEntity1.getId()).getFirst();

        List<AttackDataModel> outOfBoundsAndPattern = List.of(outOfBoundsAttack, outsidePatternAttack);
        List<AttackDataModel> insideBoundsOutsidePattern = List.of(insideBoundsAttack, outsidePatternAttack);
        List<AttackDataModel> outsideBoundsInsidePattern = List.of(outOfBoundsAttack, insidePatternAttack);
        List<AttackDataModel> insideBoth = List.of(insidePatternAttack, insideBoundsAttack);

        assertEquals(List.of(), validateAttacks(outOfBoundsAndPattern, availableCharacters, basicGrid));//out of bounds attacks
        assertEquals(List.of(insideBoundsAttack), validateAttacks(insideBoundsOutsidePattern, availableCharacters, basicGrid));//outside pattern
        assertEquals(List.of(insidePatternAttack), validateAttacks(outsideBoundsInsidePattern, availableCharacters, basicGrid));//only the inside attacks
        assertEquals(List.of(insidePatternAttack,insideBoundsAttack), validateAttacks(insideBoth, availableCharacters, basicGrid));
    }

    @Test
    void testAttackingInsideBounds() {
        List<Vector2D> outOfBoundsPositions = List.of(
                new Vector2D(10, 1), new Vector2D(1, 10), new Vector2D(10, 10),
                new Vector2D(10, -1), new Vector2D(-2, 19), new Vector2D(-1, -1),
                new Vector2D(Integer.MAX_VALUE, 0), new Vector2D(0, Integer.MAX_VALUE),
                new Vector2D(Integer.MIN_VALUE, 0), new Vector2D(0, Integer.MIN_VALUE)
        );

        List<AttackDataModel> attackOutOfBounds = attacksFromList(outOfBoundsPositions, basicEntity1.getId());

        attackOutOfBounds.forEach(attack -> assertFalse(
                attackInsideBounds(attack, basicGrid),
                "Expected attack at " + attack.getAttackPosition().toString() + " on a grid of " +
                        basicGrid.toString() + " to be rejected."
        ));

        assertTrue(attackInsideBounds(
                        new AttackDataModel(new Vector2D(4, 4), basicEntity1.getId()), basicGrid),
                "Expected an attack of (4, 4) in a grid of " + basicGrid.toString() + " to be accepted."
        );
    }

    @Test
    void testAttackingInsideAttackPattern() {

        List<CharacterEntity> availableCharacters = List.of(basicEntity1, basicEntity2, basicEntity3);
        // Get all possible attacks from character1 and character3 (one with 1 pattern and 1 with multiple patterns
        List<Vector2D> character1Attacks = Arrays.asList(
                    basicEntity1.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(
                            basicEntity1.getPosition()
                    )
        );
        ArrayList<Vector2D> character3Attacks = new ArrayList<>(Arrays.asList(
                basicEntity3.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(
                        basicEntity3.getPosition()
                )
        ));
        character3Attacks.addAll(
                Arrays.asList(
                    basicEntity3.getCharacterBaseModel().getAttackPatterns()[1].getPossibleTargetPositions(
                            basicEntity3.getPosition()
                    )
                )
        );

        List<Vector2D> baseArray = createGridFromTo(-5, -5, 5, 5);
        List<Vector2D> character1Outside = new ArrayList<>(baseArray);
        List<Vector2D> character3Outside = new ArrayList<>(baseArray);

        character1Outside.removeAll(character1Attacks);
        character3Outside.removeAll(character3Attacks);


        List<AttackDataModel> working1 = attacksFromList(character1Attacks, basicEntity1.getId());
        List<AttackDataModel> working3 = attacksFromList(character3Attacks, basicEntity3.getId());
        List<AttackDataModel> failing1 = attacksFromList(character1Outside, basicEntity1.getId());
        List<AttackDataModel> failing3 = attacksFromList(character3Outside, basicEntity3.getId());

        working1.forEach(attack -> assertTrue(attackingInsideAttackPattern(attack, availableCharacters)));
        working3.forEach(attack -> assertTrue(attackingInsideAttackPattern(attack, availableCharacters)));
        failing1.forEach(attack -> assertFalse(attackingInsideAttackPattern(attack, availableCharacters)));
        failing3.forEach(attack -> assertFalse(attackingInsideAttackPattern(attack, availableCharacters)));

    }

    /**
     * Create multiple movements from multiple vertices with the same characterId
     *
     * @param positions The positions
     * @param characterId The characterID
     * @return The movements
     */
    private static List<MovementDataModel> movesFromList(List<Vector2D> positions, String characterId) {
        ArrayList<MovementDataModel> movements = new ArrayList<>();
        for (Vector2D position : positions) {
            movements.add(new MovementDataModel(characterId, position));
        }
        return movements;
    }

    /**
     * Create multiple attacks from multiple vertices with the same characterId
     *
     * @param positions The positions
     * @param characterId The characterID
     * @return The attacks
     */
    private static List<AttackDataModel> attacksFromList(List<Vector2D> positions, String characterId) {
        ArrayList<AttackDataModel> attacks = new ArrayList<>();
        for (Vector2D position : positions) {
            attacks.add(new AttackDataModel(position, characterId));
        }
        return attacks;
    }

    /**
     * Create a List of 2D vertices that span fromX and fromY to toX and toY, essentially spanning a plane.
     *
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return The list of vertices
     */
    private static List<Vector2D> createGridFromTo(int fromX, int fromY, int toX, int toY) {
        List<Vector2D> grid = new ArrayList<>();
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                grid.add(new Vector2D(x, y));
            }
        }
        return grid;
    }

    /**
     * Get the matching characterEntity given an id.
     *
     * @param characterEntities The entities
     * @param characterId The id to search for
     * @return The character
     */
    private static CharacterEntity getCharacterWithId(
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
     * Check if move is inside characters patterns
     */
    private static boolean isMoveInsideCharacterPattern(MovementDataModel move, List<CharacterEntity> characters) {
        boolean isMoveValid = false;

        CharacterEntity character = getCharacterWithId(characters, move.getCharacterId());
        assertNotNull(character);
        PatternService[] movementPatterns = character.getCharacterBaseModel().getMovementPatterns();
        for (PatternService movementPattern : movementPatterns) {
            Vector2D position = character.getPosition();
            Vector2D intendedMove = move.getMovementVector();
            List<Vector2D> allowedPositions = Arrays.asList(movementPattern.getPossibleTargetPositions(position));
            if (allowedPositions.contains(intendedMove)) isMoveValid = true;
        }

        return isMoveValid;
    }

    /**
     * Check if move is inside characters patterns
     */
    private static boolean isAttackInsideCharacterPattern(AttackDataModel attack, List<CharacterEntity> characters) {
        boolean isAttackValid = false;

        CharacterEntity character = getCharacterWithId(characters, attack.getAttacker());
        assertNotNull(character);
        PatternService[] attackPatterns = character.getCharacterBaseModel().getAttackPatterns();
        for (PatternService attackPattern : attackPatterns) {
            Vector2D position = character.getPosition();
            Vector2D intendedMove = attack.getAttackPosition();
            List<Vector2D> allowedPositions = Arrays.asList(attackPattern.getPossibleTargetPositions(position));
            if (allowedPositions.contains(intendedMove)) isAttackValid = true;
        }

        return isAttackValid;
    }

}
