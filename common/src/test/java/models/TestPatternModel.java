package models;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestPatternModel {

    @Test
    void testConstructor() {
        // Create map
        HashMap<Character, String> map = new HashMap<>();
        map.put('x',"subpattern");
        // Create patternModel
        PatternModel patternModel = new PatternModel("xxx\nxxx\nxxx",map,"TestPattern");
        // Validate patternString
        assertEquals("xxx\nxxx\nxxx",patternModel.getPatternString());
        //Validate patternName
        assertEquals("TestPattern",patternModel.getPatternName());
        // Create validation map
        HashMap<Character, String> validMap = new HashMap<>();
        validMap.put('x',"subpattern");
        // Validate map
        assertEquals(validMap,patternModel.getSubpatternMappings());

        // Validate always has subpatternMapping
        assertNotNull(new PatternModel("",null,"").getSubpatternMappings());
    }
}
