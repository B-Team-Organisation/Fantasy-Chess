package dto;

import com.bteam.common.dto.PatternDTO;
import com.bteam.common.models.PatternModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

public class TestPatternDTO {

	@Test
	public void testPatternDTOtoJson(){
		HashMap<Character,String> subpatternMappings = new HashMap<>();
		subpatternMappings.put('x',".");
		subpatternMappings.put('y',",");
		PatternModel patternModel = new PatternModel("xxx\nx x\nxxx",subpatternMappings,"TestPattern");

		PatternDTO patternDTO = new PatternDTO(patternModel);

		String jsonString = patternDTO.toJson();
		String correctJsonString = "{\"patternName\":\"TestPattern\",\"patternString\":\"xxx\nx x\nxxx\",\"subpatternMappings\":{\"x\":\".\",\"y\":\",\"}}";
		assertEquals(correctJsonString,jsonString);
	}
}
