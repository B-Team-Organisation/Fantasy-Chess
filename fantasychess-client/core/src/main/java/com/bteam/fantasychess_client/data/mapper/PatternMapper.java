package com.bteam.fantasychess_client.data.mapper;

import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.models.PatternModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json mappers for patterns
 *
 * @author lukas
 */
public class PatternMapper {

    /**
     * Extracts pattern model from Json
     *
     * @param patternJson the {@link JsonValue} of the {@link com.bteam.common.dto.PatternDTO}
     *
     * @return the extracted {@link PatternModel}
     */
    public static PatternModel patternFromJson(JsonValue patternJson){

        String patternName = patternJson.get("patternName").asString();
        String patternString = patternJson.get("patternString").asString();

        Map<Character,String> subpatternMappings = new HashMap<>();
        JsonValue subpatternMappingsJson = patternJson.get("subpatternMappings");

        subpatternMappingsJson.forEach((s) -> {
            subpatternMappings.put(s.name().charAt(0), s.asString());
        });

        return new PatternModel(patternString,subpatternMappings,patternName);
    }

    /**
     * Extracts all pattern models from Json
     *
     * @param patternsJson the {@link JsonValue} containing a list of {@link com.bteam.common.dto.PatternDTO}
     *
     * @return the extracted list of {@link PatternModel}
     */
    public static List<PatternModel> patternsFromJson(JsonValue patternsJson){
        List<PatternModel> patterns = new ArrayList<>();
        patternsJson.forEach((p) -> patterns.add(patternFromJson(p)));
        return patterns;
    }
}
