package com.bteam.common.dto;

import com.bteam.common.models.PatternModel;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for all infos about a pattern
 *
 * @author lukas
 */
public class PatternDTO implements JsonDTO {
	private String patternName;
	private String patternString;
	private Map<Character, String> subpatternMappings;

	public PatternDTO() {
		patternName = "";
		patternString = "";
		subpatternMappings = new HashMap<Character, String>();
	}

	public PatternDTO(PatternModel patternModel){
		patternName = patternModel.getPatternName();
		patternString = patternModel.getPatternString();
		subpatternMappings = patternModel.getSubpatternMappings();
	}

	@Override
	public String toJson() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"patternName\":\"");
		sb.append(patternName);
		sb.append("\",");
		sb.append("\"patternString\":\"");
		sb.append(patternString);
		sb.append("\",");
		sb.append("\"subpatternMappings\":{");
		String mappingsJson = subpatternMappings.entrySet()
				.stream()
				.map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
				.collect(Collectors.joining(","));
		sb.append(mappingsJson);
		sb.append("}");
		sb.append("}");
		return sb.toString();
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
