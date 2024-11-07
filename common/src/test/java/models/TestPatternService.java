package models;

import Exceptions.InvalidSubpatternMappingException;
import Exceptions.PatternShapeInvalidException;
import entities.CharacterEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatternService {

    private static PatternModel emptyModel;
    private static PatternModel simpleModel;
    private static PatternModel deepModel;
    private static PatternModel deepDeepModel;
    private static PatternModel invalidDeepModel;


    private static PatternStore emptyStore;
    private static PatternStore deepStore;
    private static PatternStore deepDeepStore;


    @BeforeAll
    static void beforeAll() {
        emptyModel = new PatternModel(" ",null);
        simpleModel = new PatternModel("x x\n   \nx x",null);
        deepModel = new PatternModel("   \n  I\n   ",new HashMap<Character,String>(){{
            put('I',"LinePattern");
        }});
        deepDeepModel = new PatternModel("   \n  =\n   ",new HashMap<Character,String>(){{
            put('=',"WeirdLinePattern");
        }});

        emptyStore = new PatternStore() {
            @Override
            public Map<String, PatternModel> getPatterns() {
                return Map.of();
            }
        };
        deepStore = new PatternStore(){
            @Override
            public Map<String, PatternModel> getPatterns() {
                HashMap<String,PatternModel> map = new HashMap<>();
                map.put("LinePattern",new PatternModel(" x \n x \n x ",new HashMap<Character,String>(){}));
                map.put("WeirdLinePattern",new PatternModel("-  \n   \n-  ",new HashMap<Character,String>(){{
                    put('-',"Horizontal");
                }}));
                return map;
            }
        };
        deepDeepStore = new PatternStore(){
            @Override
            public Map<String, PatternModel> getPatterns() {
                HashMap<String,PatternModel> map = new HashMap<>();
                map.put("WeirdLinePattern",new PatternModel("-  \n   \n-  ",new HashMap<Character,String>(){{
                    put('-',"Horizontal");
                }}));
                map.put("Horizontal",new PatternModel("   \nxxx\n   ",new HashMap<Character,String>(){}));
                return map;
            }
        };
    }

    private static PatternService emptyService;
    private static PatternService simpleService;
    private static PatternService deepService;
    private static PatternService deepDeepService;

    @Test
    void testConstructor() {
        // Valid cases
        assertDoesNotThrow(() -> emptyService = new PatternService(emptyModel,emptyStore));
        assertDoesNotThrow(() -> simpleService = new PatternService(simpleModel,emptyStore));
        assertDoesNotThrow(() -> deepService = new PatternService(deepModel,deepStore));
        assertDoesNotThrow(() -> deepDeepService = new PatternService(deepDeepModel,deepDeepStore));

        // Test invalid shape detection
        // 1x2
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("  ",null),null));
        // 2x2
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("  \n  ",null),null));
        // 2x1
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel(" \n  ",null),null));
        // mix
        assertThrows(PatternShapeInvalidException.class,()->new PatternService(new PatternModel("   \n  \n   ",null),null));

        // Test missing subpattern mappings
        // 1 deep
        assertThrows(InvalidSubpatternMappingException.class,()->new PatternService(deepModel,emptyStore));
        // 2 deep
        assertThrows(InvalidSubpatternMappingException.class,()->new PatternService(deepDeepModel,deepStore));
    }

    @Test
    void testGetAreaOfEffect(){

    }

    @Test
    void testGetPossibleTargetPositions(){

    }
}
