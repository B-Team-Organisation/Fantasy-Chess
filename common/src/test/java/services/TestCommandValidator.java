package services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;
import com.bteam.common.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.bteam.common.services.CommandValidator.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCommandValidator {

    private GridService basicGrid;
    private CharacterDataModel baseModel1;
    private CharacterDataModel baseModel2;
    private CharacterDataModel baseModel3;
    private CharacterEntity basicEntity1;
    private CharacterEntity basicEntity2;
    private CharacterEntity basicEntity3;

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
        PatternService longMovementService = new PatternService(
                mockPatternStore.getPatternByName("longMovement"),
                mockPatternStore
        );
        PatternService shortMovementService = new PatternService(
                mockPatternStore.getPatternByName("shortMovement"),
                mockPatternStore
        );

        basicGrid = new GridService(new GridModel(8, 8));
        baseModel1 = new CharacterDataModel(
                "Test1", "Testest Test",
                4, 8,
                new PatternService[]{longRangeAttackService},
                new PatternService[]{shortMovementService}
        );
        baseModel2 = new CharacterDataModel(
            "Test2", "Test Test",
                    8, 4,
                    new PatternService[]{shortRangeAttackService},
                    new PatternService[]{longMovementService}
        );
        baseModel3 = new CharacterDataModel(
                "Chaos Goblin", "Brings chaos to the playing field.",
                4, 8,
                new PatternService[]{longRangeAttackService, shortMovementService},
                new PatternService[]{shortMovementService, longMovementService}
        );
        basicEntity1 = new CharacterEntity(baseModel1, "12345", 4, new Vector2D(2, 2), "1111");
        basicEntity2 = new CharacterEntity(baseModel2, "54321", 2, new Vector2D(7, 7), "2222");
        basicEntity3 = new CharacterEntity(baseModel3, "15243", 4, new Vector2D(3, 2), "1111");
    }


    @Test
    void testValidateSingleCommandsOnly() {

        AttackDataModel attack1 = new AttackDataModel(new Vector2D(0,0), basicEntity1.getId());
        AttackDataModel attack1_2 = new AttackDataModel(new Vector2D(0,0), basicEntity1.getId());
        AttackDataModel attack2 = new AttackDataModel(new Vector2D(0,0), basicEntity2.getId());
        MovementDataModel move1 = new MovementDataModel(basicEntity1.getId(), new Vector2D(0,0));
        MovementDataModel move1_2 = new MovementDataModel(basicEntity1.getId(), new Vector2D(0,0));
        MovementDataModel move2 = new MovementDataModel(basicEntity2.getId(), new Vector2D(0, 0));

        List<AttackDataModel> badDoubleAttack = List.of(attack1, attack1_2);
        List<AttackDataModel> badDoubleAttack2 = List.of(attack1, attack1_2);
        List<AttackDataModel> goodDoubleAttack = List.of(attack1, attack2);
        List<AttackDataModel> goodDoubleAttack2 = List.of(attack1, attack2);
        List<MovementDataModel> badDoubleMove = List.of(move1, move1_2);
        List<MovementDataModel> badDoubleMove2 = List.of(move1, move1_2);
        List<MovementDataModel> goodDoubleMove = List.of(move1, move2);
        List<MovementDataModel> goodDoubleMove2 = List.of(move1, move2);

        validateSingleCommandsOnly(goodDoubleMove, badDoubleAttack);
        assertEquals(List.of(), badDoubleAttack);
        assertEquals(goodDoubleMove, goodDoubleMove2);

        validateSingleCommandsOnly(badDoubleMove, List.of());
        assertEquals(List.of(), badDoubleMove);
        assertEquals(goodDoubleAttack, goodDoubleAttack2);

        validateSingleCommandsOnly(badDoubleMove2, badDoubleAttack2);
        assertEquals(List.of(), badDoubleMove2);
        assertEquals(List.of(), badDoubleAttack2);

        validateSingleCommandsOnly(goodDoubleMove2, goodDoubleAttack2);
        assertEquals(List.of(move1, move2), goodDoubleMove2);
        assertEquals(List.of(attack1, attack2), goodDoubleAttack2);

    }

    @Test
    void testValidateAttacks() {
        List<CharacterEntity> availableCharacters = List.of(basicEntity1, basicEntity2, basicEntity3);

        // insideBounds options
        AttackDataModel outOfBoundsAttack = new AttackDataModel(new Vector2D(10, 10), basicEntity1.getId());
        AttackDataModel insideBoundsAttack = new AttackDataModel(new Vector2D(4, 4), basicEntity1.getId());

        // insidePattern options
        List<Vector2D> character1Attacks = Arrays.asList(
                basicEntity1.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(
                        basicEntity1.getPosition()
                )
        );
        List<Vector2D> baseArray = createGridFromTo(-5, -5, 5, 5);
        List<Vector2D> character1Outside = new ArrayList<>(baseArray);
        character1Outside.removeAll(character1Attacks);
        AttackDataModel outsidePatternAttack = attacksFromList(character1Outside, basicEntity1.getId()).get(0);
        AttackDataModel insidePatternAttack = attacksFromList(character1Attacks, basicEntity1.getId()).get(0);

        List<AttackDataModel> outOfBoundsAndPattern = List.of(outOfBoundsAttack, outsidePatternAttack);
        List<AttackDataModel> insideBoundsOutsidePattern = List.of(insideBoundsAttack, outsidePatternAttack);
        List<AttackDataModel> outsideBoundsInsidePattern = List.of(outOfBoundsAttack, insidePatternAttack);
        List<AttackDataModel> insideBoth = List.of(insidePatternAttack, insideBoundsAttack);

        assertEquals(List.of(), validateAttacks(outOfBoundsAndPattern, availableCharacters, basicGrid));
        assertEquals(List.of(), validateAttacks(insideBoundsOutsidePattern, availableCharacters, basicGrid));
        assertEquals(List.of(insidePatternAttack), validateAttacks(outsideBoundsInsidePattern, availableCharacters, basicGrid));
        assertEquals(List.of(insidePatternAttack), validateAttacks(insideBoth, availableCharacters, basicGrid));
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

}
