package data.mapper;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.PatternDTO;
import com.bteam.common.dto.PatternListDTO;
import com.bteam.common.models.PatternModel;
import com.bteam.fantasychess_client.data.mapper.PatternMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPatternMapper {

    private static PatternModel patternModel;

    @BeforeAll
    public static void setup() {
        Map<Character, String> subpatternMappings = new HashMap<>();
        subpatternMappings.put('a', "x");
        subpatternMappings.put('b', "y");

        patternModel = new PatternModel("xxx\nx x\nxxx", subpatternMappings, "TestPattern");

    }

    @Test
    public void testPatternFromJson() {
        PatternDTO patternDTO = new PatternDTO(patternModel);
        JsonValue patternJson = new JsonReader().parse(patternDTO.toJson());
        PatternModel extractedModel = PatternMapper.patternFromJson(patternJson);

        assertEquals(patternModel, extractedModel);
    }

    @Test
    public void testPatternsFromJson() {
        List<PatternModel> patternList = new ArrayList<>();
        patternList.add(patternModel);
        patternList.add(patternModel);
        patternList.add(patternModel);

        List<PatternDTO> patternDTOList = new ArrayList<>();
        patternList.forEach((p) -> patternDTOList.add(new PatternDTO(p)));

        PatternListDTO patternListDTO = new PatternListDTO(patternDTOList);
        JsonValue patternListDTOJson = new JsonReader().parse(patternListDTO.toJson());

        assertEquals(patternList, PatternMapper.patternsFromJson(patternListDTOJson.get("patterns")));
    }
}
