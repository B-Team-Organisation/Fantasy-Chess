package dto;

import com.bteam.common.dto.PatternDTO;
import com.bteam.common.dto.PatternListDTO;
import com.bteam.common.models.PatternModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestPatternListDTO {

	private static PatternDTO patternDTO;

	@BeforeAll
	public static void setup(){
		HashMap<Character,String> subpatternMappings = new HashMap<>();
		subpatternMappings.put('x',".");
		subpatternMappings.put('y',",");
		PatternModel patternModel = new PatternModel("xxx\nx x\nxxx",subpatternMappings,"TestPattern");

		patternDTO = new PatternDTO(patternModel);
	}

	@Test
	void testPatternListDTOtoJson() {
		String patternDTOJson = patternDTO.toJson();

		PatternListDTO patternListDTO;
		List<PatternDTO> patterns = new ArrayList<>();
		String correctJson;

		patternListDTO = new PatternListDTO();
		correctJson = "{\"patterns\":[]}";
		assertEquals(correctJson,patternListDTO.toJson());

		patternListDTO = new PatternListDTO(patterns);
		assertEquals(correctJson,patternListDTO.toJson());

		patterns.add(patternDTO);

		correctJson = "{\"patterns\":["+patternDTO.toJson()+"]}";
		patternListDTO = new PatternListDTO(patterns);
		assertEquals(correctJson,patternListDTO.toJson());

		patterns.add(patternDTO);

		correctJson = "{\"patterns\":["+patternDTO.toJson()+","+patternDTO.toJson()+"]}";
		patternListDTO = new PatternListDTO(patterns);
		assertEquals(correctJson,patternListDTO.toJson());
	}
}
