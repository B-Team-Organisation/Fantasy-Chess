package utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.models.*;
import com.bteam.common.utils.TurnLogic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTurnLogic {

	GridModel gridBefore;
	GridService gridService;

	List<CharacterEntity> characters;
	List<MovementDataModel> movements;
	List<AttackDataModel> attacks;
	String hostID = "player1";

	static CharacterDataModel exploCharData;
	static CharacterDataModel twoEachCharData;

	@BeforeAll
	public static void init(){
		PatternStore patternStore = new PatternStore() {

			private final Map<String,PatternModel> patterns = new HashMap<>(){{
				put("3x3",new PatternModel("xxx\nxxx\nxxx",new HashMap<>(),"3x3"));
			}};

			@Override
			public PatternModel getPatternByName(String patternName) {
				return patterns.get(patternName);
			}
		};

		PatternModel explPattern = new PatternModel("o",new HashMap<>(){{put('o',"3x3");}},"o");
		PatternService exploPatternService;

		PatternModel twoEachPattern = new PatternModel("  x  \n     \nx   x\n     \n  x  ",new HashMap<>(),"twoInEach");
		PatternService twoEachPatternService;

		try {
			exploPatternService = new PatternService(explPattern,patternStore);
			twoEachPatternService = new PatternService(twoEachPattern,patternStore);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		exploCharData = new CharacterDataModel("explo","",10,5,new PatternService[]{exploPatternService},new PatternService[0],"","");
		twoEachCharData = new CharacterDataModel("twoEach","",10,5,new PatternService[]{twoEachPatternService},new PatternService[]{twoEachPatternService},"","");
	}

	@BeforeEach
	public void setUp() {
		gridBefore = new GridModel(3,3);
		gridService = new GridService(gridBefore);

		try {
			gridService.setStartTiles(new int[]{0,1,2});
		} catch (DestinationInvalidException e) {
			throw new RuntimeException(e);
		}

		characters = new ArrayList<>();
		movements = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	private void applyTurnLogic() {
		TurnLogic.applyCommands(movements,characters,attacks, gridService, hostID);
	}

	@Test
	public void testValidMoves(){
		CharacterEntity topLeft = new CharacterEntity(twoEachCharData,"0",10,null,hostID);
		Vector2D topLeftOrigin = new Vector2D(0,0);
		Vector2D topLeftTarget = new Vector2D(2,0);
		characters.add(topLeft);

		MovementDataModel topLeftCommand = new MovementDataModel(topLeft.getId(), topLeftTarget);
		movements.add(topLeftCommand);

		CharacterEntity bottomLeft = new CharacterEntity(twoEachCharData,"1",10,null,hostID);
		Vector2D bottomLeftOrigin = new Vector2D(0,2);
		Vector2D bottomLeftTarget = new Vector2D(2,2);
		characters.add(bottomLeft);

		MovementDataModel bottomLeftCommand = new MovementDataModel(bottomLeft.getId(), bottomLeftTarget);
		movements.add(bottomLeftCommand);

		try {
			gridService.setCharacterTo(topLeftOrigin,topLeft);
			gridService.setCharacterTo(bottomLeftOrigin,bottomLeft);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		applyTurnLogic();

		assert topLeft.getPosition().equals(topLeftTarget);
		assert bottomLeft.getPosition().equals(bottomLeftTarget);
	}

	@Test
	public void testValidSingleTargetAttack(){
		CharacterEntity topLeft = new CharacterEntity(twoEachCharData,"0",15,null,hostID);
		Vector2D topLeftOrigin = new Vector2D(0,0);
		Vector2D topLeftTarget = new Vector2D(0,2);
		characters.add(topLeft);

		AttackDataModel topLeftCommand = new AttackDataModel(topLeftTarget, topLeft.getId());
		attacks.add(topLeftCommand);

		CharacterEntity bottomLeft = new CharacterEntity(twoEachCharData,"1",10,null,hostID);
		Vector2D bottomLeftOrigin = new Vector2D(0,2);
		Vector2D bottomLeftTarget = new Vector2D(0,0);
		characters.add(bottomLeft);

		AttackDataModel bottomLeftCommand = new AttackDataModel(bottomLeftTarget,bottomLeft.getId());
		attacks.add(bottomLeftCommand);

		CharacterEntity dummy = new CharacterEntity(twoEachCharData,"2",10,null,hostID);
		Vector2D dummyOrigin = new Vector2D(2,2);
		characters.add(dummy);

		try {
			gridService.setCharacterTo(topLeftOrigin,topLeft);
			gridService.setCharacterTo(bottomLeftOrigin,bottomLeft);
			gridService.setCharacterTo(dummyOrigin,dummy);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		applyTurnLogic();

		assert topLeft.getHealth() == 10;
		assert bottomLeft.getHealth() == 5;
		assert dummy.getHealth() == 10;
	}

	@Test
	public void testValidAoeDamage(){
		CharacterEntity topLeft = new CharacterEntity(twoEachCharData,"0",10,null,hostID);
		Vector2D topLeftOrigin = new Vector2D(0,0);
		characters.add(topLeft);

		CharacterEntity bottomLeft = new CharacterEntity(twoEachCharData,"1",10,null,hostID);
		Vector2D bottomLeftOrigin = new Vector2D(0,2);
		characters.add(bottomLeft);

		CharacterEntity exploder = new CharacterEntity(exploCharData,"2",10,null,hostID);
		Vector2D exploderOrigin = new Vector2D(1,1);
		characters.add(exploder);

		AttackDataModel exploderAttack = new AttackDataModel(exploderOrigin, exploder.getId());
		attacks.add(exploderAttack);

		try {
			gridService.setCharacterTo(topLeftOrigin,topLeft);
			gridService.setCharacterTo(bottomLeftOrigin,bottomLeft);
			gridService.setCharacterTo(exploderOrigin,exploder);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		applyTurnLogic();

		assert topLeft.getHealth() == 5;
		assert bottomLeft.getHealth() == 5;
		assert exploder.getHealth() == 5;

		assert characters.size() == 3;

		applyTurnLogic();

		assert topLeft.getHealth() == 0;
		assert bottomLeft.getHealth() == 0;
		assert exploder.getHealth() == 0;

		assert characters.isEmpty();
	}
}
