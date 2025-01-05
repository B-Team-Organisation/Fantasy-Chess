package models;

import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;
import com.bteam.common.models.PatternModel;
import com.bteam.common.models.PatternStore;
import com.bteam.common.models.PatternService;
import com.bteam.common.models.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatternService {

    private static PatternModel emptyModel;
    private static PatternModel simpleModel;
    private static PatternModel deepModel;
    private static PatternModel deepDeepModel;


    private static PatternStore emptyStore;
    private static PatternStore deepStore;
    private static PatternStore deepDeepStore;

    private static Vector2D playerPosition = new Vector2D(4,4);

    // Directions
    private static Vector2D targetPositionUp = new Vector2D(4,3);
    private static Vector2D targetPositionRight = new Vector2D(5,4);
    private static Vector2D targetPositionDown = new Vector2D(4,5);
    private static Vector2D targetPositionLeft = new Vector2D(3,4);

    // Corners
    private static Vector2D targetPositionUpLeft = new Vector2D(3,3);
    private static Vector2D targetPositionUpRight = new Vector2D(5,3);
    private static Vector2D targetPositionDownLeft = new Vector2D(3,5);
    private static Vector2D targetPositionDownRight = new Vector2D(5,5);


    @BeforeAll
    static void beforeAll() {
        emptyModel = new PatternModel(" ",null,"empty");
        simpleModel = new PatternModel("x x\n   \nx x",null,"corners");
        deepModel = new PatternModel("   \n  I\n   ",new HashMap<Character,String>(){{
            put('I',"LinePattern");
        }},"deep");
        deepDeepModel = new PatternModel("   \n  =\n   ",new HashMap<Character,String>(){{
            put('=',"WeirdLinePattern");
        }},"deepDeep");

        emptyStore = new PatternStore() {
            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }

            private Map<String, PatternModel> patterns = new HashMap<>();
        };
        deepStore = new PatternStore(){
            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }

            private Map<String, PatternModel> patterns = new HashMap<>(){
                {
                    put("LinePattern", new PatternModel(" x \n x \n x ", new HashMap<Character, String>() {
                    },"LinePattern"));
                    put("WeirdLinePattern", new PatternModel("-  \n   \n-  ", new HashMap<Character, String>() {{
                        put('-', "Horizontal");
                    }},"WeirdLinePattern"));
                }
            };
        };
        deepDeepStore = new PatternStore(){
            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }

            private Map<String, PatternModel> patterns = new HashMap<>(){
                {
                put("WeirdLinePattern",new PatternModel("-  \n   \n-  ",new HashMap<Character,String>(){{
                    put('-',"Horizontal");
                }},"WeirdLinePattern"));
                put("Horizontal",new PatternModel("   \nxxx\n   ",new HashMap<Character,String>(){},"Horizontal"));
                }
            };
        };

        playerPosition = new Vector2D(4,4);

        targetPositionUp = new Vector2D(4,3);
        targetPositionRight = new Vector2D(5,4);
        targetPositionDown = new Vector2D(4,5);
        targetPositionLeft = new Vector2D(3,4);

        targetPositionUpLeft = new Vector2D(3,3);
        targetPositionUpRight = new Vector2D(5,3);
        targetPositionDownLeft = new Vector2D(3,5);
        targetPositionDownRight = new Vector2D(5,5);
    }

    private static PatternService emptyService;
    private static PatternService simpleService;
    private static PatternService deepService;
    private static PatternService deepDeepService;

    @BeforeEach
    void setUp() {
        try {
            // Tested in testConstructor
            emptyService = new PatternService(emptyModel,emptyStore);
            simpleService = new PatternService(simpleModel,emptyStore);
            deepService = new PatternService(deepModel,deepStore);
            deepDeepService = new PatternService(deepDeepModel,deepDeepStore);
        } catch (Exception e){
            assertNull(e);
        }
    }

    @Test
    void testConstructor() {
        // Valid cases
        assertDoesNotThrow(() -> new PatternService(emptyModel,emptyStore));
        assertDoesNotThrow(() -> new PatternService(simpleModel,emptyStore));
        assertDoesNotThrow(() -> new PatternService(deepModel,deepStore));
        assertDoesNotThrow(() -> new PatternService(deepDeepModel,deepDeepStore));

        // Test invalid shape detection
        // 1x2
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("  ",null,""),null));
        // 2x2
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("  \n  ",null,""),null));
        // 2x1
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel(" \n  ",null,""),null));
        // 3x5
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("   \n   \n   \n   \n   ",null,""),null));
        // mix
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("   \n  \n   ",null,""),null));

        // Test missing subpattern mappings
        // 1 deep
        assertThrows(InvalidSubpatternMappingException.class,()->new PatternService(deepModel,emptyStore));
        // 2 deep
        assertThrows(InvalidSubpatternMappingException.class,()->new PatternService(deepDeepModel,deepStore));
    }

    @Test
    void testGetAreaOfEffect(){
        // Test empty attack position
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionUp));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionRight));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionDown));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionLeft));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionUpLeft));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionUpRight));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionDownLeft));
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, targetPositionDownRight));

        // Test distant
        assertArrayEquals(new Vector2D[0], emptyService.getAreaOfEffect(playerPosition, new Vector2D(12332,-123)));

        // Test simple attack pattern
        assertArrayEquals(new Vector2D[]{targetPositionUpLeft}, simpleService.getAreaOfEffect(playerPosition, targetPositionUpLeft));
        assertArrayEquals(new Vector2D[]{targetPositionUpRight}, simpleService.getAreaOfEffect(playerPosition, targetPositionUpRight));
        assertArrayEquals(new Vector2D[]{targetPositionDownLeft}, simpleService.getAreaOfEffect(playerPosition, targetPositionDownLeft));
        assertArrayEquals(new Vector2D[]{targetPositionDownRight}, simpleService.getAreaOfEffect(playerPosition, targetPositionDownRight));

        // Test deep attack pattern
        Set<Vector2D> expected = Set.of(targetPositionUpRight, targetPositionRight, targetPositionDownRight);
        Set<Vector2D> returned = Set.of(deepService.getAreaOfEffect(playerPosition, targetPositionRight));
        assertEquals(expected, returned);
        assertArrayEquals(new Vector2D[0], deepService.getAreaOfEffect(playerPosition, new Vector2D(5,5)));

        // Test deep deep attack pattern
        expected = Set.of(targetPositionUpLeft,targetPositionUp, targetPositionUpRight,targetPositionDownLeft,targetPositionDown,targetPositionDownRight);
        returned = Set.of(deepDeepService.getAreaOfEffect(playerPosition, targetPositionRight));
        assertEquals(expected, returned);
        assertArrayEquals(new Vector2D[0], deepDeepService.getAreaOfEffect(playerPosition, new Vector2D(5,5)));
    }

    @Test
    void testGetPossibleTargetPositions(){
        // Empty
        assertArrayEquals(new Vector2D[0], emptyService.getPossibleTargetPositions(playerPosition));
        // Simple
        Set<Vector2D> expected = Set.of(targetPositionUpLeft,targetPositionUpRight,targetPositionDownLeft,targetPositionDownRight);
        Set<Vector2D> returned = Set.of(simpleService.getPossibleTargetPositions(playerPosition));
        assertEquals(expected, returned);
        // Deep
        expected = Set.of(targetPositionRight);
        returned = Set.of(deepService.getPossibleTargetPositions(playerPosition));
        assertEquals(expected, returned);
        // Deep deep
        expected = Set.of(targetPositionRight);
        returned = Set.of(deepDeepService.getPossibleTargetPositions(playerPosition));
        assertEquals(expected, returned);
    }

    @Test
    void testReversePattern(){
        Vector2D[] oldAoE = deepService.getAreaOfEffect(playerPosition, targetPositionRight);

        try {
            PatternService reversedService = deepService.reversePattern();

            Vector2D[] newAoE = reversedService.getAreaOfEffect(playerPosition, targetPositionLeft);

            assert oldAoE.length == newAoE.length;

            for (int i = 0; i < oldAoE.length; i++) {
                Vector2D oldRel = oldAoE[i].subtract(playerPosition);
                Vector2D newRel = newAoE[i].subtract(playerPosition);

                assert oldRel.getX() == -newRel.getX();
                assert oldRel.getY() == -newRel.getY();
            }

        } catch (Exception e) {
            Assertions.fail("An exception was thrown unexpectedly: " + e.getMessage());
        }

    }
}
