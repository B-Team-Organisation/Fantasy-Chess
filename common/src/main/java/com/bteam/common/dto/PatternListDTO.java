package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a list of Patterns
 *
 * @author lukas
 */
public class PatternListDTO implements JsonDTO {
    private final List<PatternDTO> patterns;

    public PatternListDTO() {
        patterns = new ArrayList<PatternDTO>();
    }

    public PatternListDTO(List<PatternDTO> patterns) {
        this.patterns = patterns;
    }

    public List<PatternDTO> getPatterns() {
        return patterns;
    }


    @Override
    public String toJson() {
        return new JsonWriter().writeList("patterns", patterns).toString();
    }
}
