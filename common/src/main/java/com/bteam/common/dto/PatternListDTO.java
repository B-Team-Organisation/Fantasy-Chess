package com.bteam.common.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a list of Patterns
 *
 * @author lukas
 */
public class PatternListDTO implements JsonDTO {
	private List<PatternDTO> patterns;

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
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"patterns\":[");
		for (PatternDTO pattern : patterns) {
			sb.append(pattern.toJson());
			sb.append(",");
		}
		if (!patterns.isEmpty()) {
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]}");
		return sb.toString();
	}
}
