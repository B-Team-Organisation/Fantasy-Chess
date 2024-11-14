package com.bteam.common.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Data model for a pattern
 * <p>
 * This class stores a string representation of a pattern, its name and the {@code subpatternMappings} to decipher it.
 * It can be used to save data necessary to describe a pieces movement or attack capabilities.
 *
 * @author Lukas
 * @version 1.0
 */
public class PatternModel {
    private final String patternName;
    private final String patternString;
    private final Map<Character, String> subpatternMappings;
    /**
     * Constructor for the PatternModel.
     *
     * @param patternString String that saves the position mappings
     * @param subpatternMappings Map that contains all necessary pattern references
     * @param patternName the name of the pattern
     */
    public PatternModel(String patternString, Map<Character, String> subpatternMappings, String patternName) {
        this.patternName = patternName;
        this.patternString = patternString;
        if (Objects.isNull(subpatternMappings)){
            this.subpatternMappings = new HashMap<>();
        } else {
            this.subpatternMappings = subpatternMappings;
        }
	}

    public String getPatternName() {
        return patternName;
    }

    public String getPatternString() {
        return patternString;
    }

    public Map<Character, String> getSubpatternMappings() {
        return subpatternMappings;
    }

    @Override
    public String toString() {
        return "PatternModel [patternName=" + patternName + ", patternString=" + patternString + ", subpatternMappings=" + subpatternMappings + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PatternModel
                && patternName.equals(((PatternModel) o).getPatternName())
                && this.patternString.equals(((PatternModel) o).getPatternString())
                && this.subpatternMappings.equals(((PatternModel) o).getSubpatternMappings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternName, patternString, subpatternMappings);
    }
}
