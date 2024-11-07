package models;

import java.util.Map;

public class AttackPatternModel extends PatternModel {
	public final int baseDamage;
	public AttackPatternModel(String patternString, Map<Character, String> subpatternMappings, int baseDamage) {
		super(patternString, subpatternMappings);
		this.baseDamage = baseDamage;
	}
}
