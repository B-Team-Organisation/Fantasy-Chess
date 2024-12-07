package services;
import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;
import utils.TurnResultNoOrder;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static com.bteam.common.services.TurnLogicService.*;


class TestTurnLogicService {


    private CharacterEntity normalCharacter1;
    private CharacterEntity normalCharacter2;
    private CharacterEntity closeToDyingCharacter1;
    private CharacterEntity longAttackCharacter2;
    private CharacterEntity lowHealthCharacter1;
    private CharacterEntity lowHealthCharacter2;
    private CharacterEntity highHealthCharacter1;
    private CharacterEntity highHealthCharacter2;

    MovementDataModel legalMovement;
    MovementDataModel legalMovement2;
    MovementDataModel legalMovement3;
    MovementDataModel legalMovement4;

    AttackDataModel legalAttack;
    AttackDataModel legalAttack2;
    AttackDataModel legalAttack3;
    AttackDataModel legalAttack4;

    Player player1 ;
    Player player2 ;

    List<CharacterEntity> charactersBefore;
    List<CharacterEntity> charactersAfter;
    List<MovementDataModel> movements;
    List<AttackDataModel> attacks;

    @BeforeEach
    void setUp() throws Exception {

        player1 = new Player("Username1", "1111", new ArrayList<>());
        player2 = new Player("Username2", "2222", new ArrayList<>());

        PatternStore mockPatternStore = new PatternStore() {
            private final Map<String, PatternModel> patterns = new HashMap<>() {{
                put("longRangeAttack", new PatternModel("   x   \n   x   \n   x   \nxxxxxxx\n   x   \n   x   \n   x   ", new HashMap<>(), "longRangeAttack"));
                put("shortMovement", new PatternModel("xxx\n x \nxxx", new HashMap<>(), "shortMovement"));
            }};

            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }
        };

        PatternService longRangeAttackService = new PatternService(
                mockPatternStore.getPatternByName("longRangeAttack"),
                mockPatternStore
        );
        PatternService shortMovementService = new PatternService(
                mockPatternStore.getPatternByName("shortMovement"),
                mockPatternStore
        );

        normalCharacter1 = new CharacterEntity(
                new CharacterDataModel("Hallo", "char1", 10, 21, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "1",10, new Vector2D(0, 0), player1.getPlayerId()
        );

        normalCharacter2 = new CharacterEntity(
                new CharacterDataModel("Amon", "char2", 20, 35, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "2",20, new Vector2D(4,5), player2.getPlayerId()
        );

        closeToDyingCharacter1 = new CharacterEntity(
                new CharacterDataModel("Granger", "char3", 5, 10, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "3",5, new Vector2D(9, 8), player1.getPlayerId()
        );

        longAttackCharacter2 = new CharacterEntity(
                new CharacterDataModel("Lud Yi", "char4", 15, 20, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "4",15, new Vector2D(8, 8), player2.getPlayerId()
        );

        lowHealthCharacter1 = new CharacterEntity(
                new CharacterDataModel("pharsa", "char5", 10, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "5",10, new Vector2D(0,1), player1.getPlayerId()
        );

        lowHealthCharacter2 = new CharacterEntity(
                new CharacterDataModel("Martis", "char6", 10, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "6",10, new Vector2D(0,3), player2.getPlayerId()
        );

        highHealthCharacter1 = new CharacterEntity(
                new CharacterDataModel("estes", "char7", 50, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "7",50, new Vector2D(4,2), player1.getPlayerId()
        );

        highHealthCharacter2 = new CharacterEntity(
                new CharacterDataModel("melissa", "char8", 50, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "8",50, new Vector2D(4,4), player2.getPlayerId()
        );

        legalMovement = new MovementDataModel(closeToDyingCharacter1.getId(), new Vector2D(8, 7));
        legalMovement2 = new MovementDataModel(longAttackCharacter2.getId(), new Vector2D(7, 9));
        legalMovement3 = new MovementDataModel(highHealthCharacter2.getId(), new Vector2D(3, 3));
        legalMovement4 = new MovementDataModel(lowHealthCharacter2.getId(), new Vector2D(0, 2));

        legalAttack = new AttackDataModel(new Vector2D(0, 3), normalCharacter1.getId());
        legalAttack2 = new AttackDataModel(new Vector2D(4,2), normalCharacter2.getId());
        legalAttack3 = new AttackDataModel(new Vector2D(4, 4), highHealthCharacter1.getId());
        legalAttack4 = new AttackDataModel(new Vector2D(0,0), lowHealthCharacter1.getId());

        movements = new ArrayList<>();
        movements.add(legalMovement);
        movements.add(legalMovement2);
        movements.add(legalMovement3);
        movements.add(legalMovement4);

        attacks = new ArrayList<>();
        attacks.add(legalAttack);
        attacks.add(legalAttack2);
        attacks.add(legalAttack3);
        attacks.add(legalAttack4);

        charactersAfter = new ArrayList<>();
        charactersAfter.add(normalCharacter1);
        charactersAfter.add(normalCharacter2);
        charactersAfter.add(closeToDyingCharacter1);
        charactersAfter.add(longAttackCharacter2);
        charactersAfter.add(lowHealthCharacter2);
        charactersAfter.add(highHealthCharacter2);
        charactersAfter.add(lowHealthCharacter1);
        charactersAfter.add(highHealthCharacter1);

        charactersBefore = new ArrayList<>();
        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);
    }

    @Test

    void testApplyCommands() {

        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);

        lowHealthCharacter2.setPosition(new Vector2D(0, 2));
        highHealthCharacter2.setPosition(new Vector2D(3, 3));
        closeToDyingCharacter1.setPosition(new Vector2D(8, 7));
        longAttackCharacter2.setPosition(new Vector2D(7, 9));
        highHealthCharacter1.setHealth(15);
        charactersAfter.remove(normalCharacter1);

        TurnResult result = new TurnResult(charactersAfter,new ArrayList<>(),movements,attacks);
        TurnResult actual = applyCommands(movements,charactersBefore,attacks,new GridModel(10,10));

        assertEquals(new TurnResultNoOrder(result), new TurnResultNoOrder(actual));
    }


    @Test
    void testApplyAttacks()  throws Exception {
        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);

        charactersAfter.remove(lowHealthCharacter2);
        highHealthCharacter1.setHealth(15);
        highHealthCharacter2.setHealth(25);
        charactersAfter.remove(normalCharacter1);

        Method method = TurnLogicService.class.getDeclaredMethod("applyAttacks", List.class, List.class);
        method.setAccessible(true);
        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, attacks, charactersBefore);

        assertEquals(charactersAfter, result);
    }

    @Test

    void testApplyAreaAttack() throws PatternShapeInvalidException, InvalidSubpatternMappingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        PatternModel deepModel = new PatternModel("   \n  I\n   ",new HashMap<Character,String>(){{
            put('I',"LinePattern");
        }},"RightLine");
        PatternStore deepStore = new PatternStore(){
            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }

            private Map<String, PatternModel> patterns = new HashMap<>(){
                {
                    put("LinePattern", new PatternModel(" x \n x \n x ", new HashMap<Character, String>() {
                    },"LinePattern"));
                }
            };
        };
        PatternService deepService = new PatternService(deepModel,deepStore);

        CharacterEntity areaDamageCharacter2 = new CharacterEntity(
                new CharacterDataModel("Xavier", "", 50, 10, new PatternService[]{deepService}, new PatternService[]{}),
                "9",50, new Vector2D(0,0), player2.getPlayerId()
        );

        CharacterEntity victim1 = new CharacterEntity (new CharacterDataModel("","",10,0,new PatternService[]{},new PatternService[]{}),"10",10,new Vector2D(0,1),player1.getPlayerId());
        CharacterEntity victim2 = new CharacterEntity (new CharacterDataModel("","",11,0,new PatternService[]{},new PatternService[]{}),"11",11,new Vector2D(1,1),player1.getPlayerId());

        AttackDataModel areaAttack = new AttackDataModel(new Vector2D(0,1),areaDamageCharacter2.getId());
        attacks.clear();
        attacks.add(areaAttack);

        charactersAfter.clear();

        charactersAfter.add(areaDamageCharacter2);
        charactersAfter.add(victim1);
        charactersAfter.add(victim2);

        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);

        victim2.setHealth(1);
        charactersAfter.remove(victim1);

        Method method = TurnLogicService.class.getDeclaredMethod("applyAttacks", List.class, List.class);
        method.setAccessible(true);
        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, attacks, charactersBefore);

        assertEquals(charactersAfter,result);


    }
    @Test
    void testApplyMovement() throws Exception {

        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);
        closeToDyingCharacter1.setPosition(new Vector2D(8, 7));
        longAttackCharacter2.setPosition(new Vector2D(7, 9));
        highHealthCharacter2.setPosition(new Vector2D(3, 3));
        lowHealthCharacter2.setPosition(new Vector2D(0, 2));

        Method method = TurnLogicService.class.getDeclaredMethod("applyMovement", List.class, List.class);
        method.setAccessible(true);
        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, movements, charactersBefore);

        assertEquals(charactersAfter,result);

    }



    @Test
    void testCheckForWinner() {

        assertNull(checkForWinner(charactersAfter));
        charactersAfter.remove(normalCharacter1);
        charactersAfter.remove(normalCharacter2);
        charactersAfter.remove(lowHealthCharacter1);
        charactersAfter.remove(highHealthCharacter1);
        charactersAfter.remove(lowHealthCharacter2);
        charactersAfter.remove(highHealthCharacter2);
        charactersAfter.remove(longAttackCharacter2);
        assertEquals("1111", checkForWinner(charactersAfter));
        charactersAfter.add(longAttackCharacter2);
        charactersAfter.remove(closeToDyingCharacter1);
        assertEquals("2222", checkForWinner(charactersAfter));
        assertNull(checkForWinner(charactersBefore));
    }

}