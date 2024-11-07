package models;

import java.util.Map;

public class PatternModel {
    private final String patternString;
    private final Map<Character, String> subpatternMappings;

    public PatternModel(String patternString, Map<Character,String> subpatternMappings) {
        this.patternString = patternString;
		this.subpatternMappings = subpatternMappings;
	}

    public String getPatternString() {
        return patternString;
    }

    public Map<Character, String> getSubpatternMappings() {
        return subpatternMappings;
    }
}
