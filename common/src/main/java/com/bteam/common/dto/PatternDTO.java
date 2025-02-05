package com.bteam.common.dto;

import com.bteam.common.models.PatternModel;
import com.bteam.common.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for all infos about a pattern
 *
 * @author lukas
 */
public class PatternDTO implements JsonDTO {
    private final String patternName;
    private final String patternString;
    private final Map<Character, String> subpatternMappings;

    public PatternDTO() {
        patternName = "";
        patternString = "";
        subpatternMappings = new HashMap<>();
    }

    public PatternDTO(PatternModel patternModel) {
        patternName = patternModel.getPatternName();
        patternString = patternModel.getPatternString();
        subpatternMappings = patternModel.getSubpatternMappings();
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("patternName", patternName)
            .and().writeKeyValue("patternString", patternString)
            .and().writeMap("subpatternMappings", subpatternMappings)
            .toString();
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
}
