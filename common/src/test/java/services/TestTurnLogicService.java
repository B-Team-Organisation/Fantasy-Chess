package services;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bteam.common.utils.Pair;
import utils.TestUtils;


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
    List<MovementDataModel> movementsBefore;
    List<AttackDataModel> attacksBefore;
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


        Vector2D cornerPosition = new Vector2D(0, 0);
        Vector2D centerPosition = new Vector2D(4,5);
        Vector2D borderPosition = new Vector2D(9, 8);
        Vector2D farPosition = new Vector2D(8, 8);


        //movement
        normalCharacter1 = new CharacterEntity(
                new CharacterDataModel("Hallo", "Hehe", 10, 21, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "1",10, cornerPosition, player1.getPlayerId()
        );

        normalCharacter2 = new CharacterEntity(
                new CharacterDataModel("Amon", "Test1", 20, 35, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "2",20, centerPosition, player2.getPlayerId()
        );

        closeToDyingCharacter1 = new CharacterEntity(
                new CharacterDataModel("Granger", "A character with low health", 5, 10, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "3",5, borderPosition, player1.getPlayerId()
        );

        longAttackCharacter2 = new CharacterEntity(
                new CharacterDataModel("Lud Yi", "Specializes in long-range attacks", 15, 20, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "4",15, farPosition, player2.getPlayerId()
        );



        //attacks
        lowHealthCharacter1 = new CharacterEntity(
                new CharacterDataModel("pharsa", "charcater with low health", 10, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "5",10, new Vector2D(0,1), player1.getPlayerId()
        );

        lowHealthCharacter2 = new CharacterEntity(
                new CharacterDataModel("Martis", "character with low health", 10, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "6",10, new Vector2D(0,3), player2.getPlayerId()
        );

        highHealthCharacter1 = new CharacterEntity(
                new CharacterDataModel("estes", "A character with high health values", 50, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "7",50, new Vector2D(4,2), player1.getPlayerId()
        );

        highHealthCharacter2 = new CharacterEntity(
                new CharacterDataModel("melissa", "very much health", 50, 25, new PatternService[]{longRangeAttackService}, new PatternService[]{shortMovementService}),
                "8",50, new Vector2D(4,4), player2.getPlayerId()
        );

        legalMovement = new MovementDataModel(closeToDyingCharacter1.getId(), new Vector2D(8, 7));
        legalMovement2 = new MovementDataModel(longAttackCharacter2.getId(), new Vector2D(7, 9));
        legalMovement3 = new MovementDataModel(highHealthCharacter2.getId(), new Vector2D(3, 3));
        legalMovement4 = new MovementDataModel(lowHealthCharacter2.getId(), new Vector2D(0, 2));


        /*
        normalcharacter1.  (0,0)-> attack (0,3)
        normalcharacter2. (4,5)-> attack (4,2)
        highHealthCharacter1. (4,2)-> attack (4,4)
        lowHealthcharacter1. (0,1)-> attack ( 0,0)

        closetoyiingcharacter1. (9,8)-> move (8,7)
        longRangeCharacter2. (8,8) -> move ( 7,9)
        highHealthCharacter2. (4,4)-> move ( 3,3)
        lowHealthCharacter2. (0,3)-> move ( 0,2)
         */



        legalAttack = new AttackDataModel(new Vector2D(0, 3), normalCharacter1.getId());
        legalAttack2 = new AttackDataModel(new Vector2D(4,2), normalCharacter2.getId());
        legalAttack3 = new AttackDataModel(new Vector2D(4, 4), highHealthCharacter1.getId());
        legalAttack4 = new AttackDataModel(new Vector2D(0,0), lowHealthCharacter1.getId());

        charactersBefore = new ArrayList<>();
        charactersAfter = new ArrayList<>();
        movementsBefore = new ArrayList<>();
        attacksBefore = new ArrayList<>();

        charactersAfter.add(normalCharacter1);
        charactersAfter.add(normalCharacter2);
        charactersAfter.add(closeToDyingCharacter1);
        charactersAfter.add(longAttackCharacter2);
        charactersAfter.add(lowHealthCharacter2);
        charactersAfter.add(highHealthCharacter2);
        charactersAfter.add(lowHealthCharacter1);
        charactersAfter.add(highHealthCharacter1);


        movementsBefore.add(legalMovement);
        movementsBefore.add(legalMovement2);
        movementsBefore.add(legalMovement3);
        movementsBefore.add(legalMovement4);

        attacksBefore.add(legalAttack);
        attacksBefore.add(legalAttack2);
        attacksBefore.add(legalAttack3);
        attacksBefore.add(legalAttack4);

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
        //charactersAfter.remove(lowHealthCharacter2); //legalattack1 martis died--
        //charactersAfter.remove(normalCharacter1);// legalattack4 invalid pattern
        //highHealthcharacter2.setHealth(25);//legalattack3 melissa health 25
        ;//legalAttack2 estes health 15

        TurnResult result =
                new TurnResult(charactersAfter,new ArrayList<>(),movementsBefore,attacksBefore);
        TurnResult actual = applyCommands(movementsBefore,charactersBefore,attacksBefore,new GridModel(10,10));

        assertEquals(result.getValidAttacks(),actual.getValidAttacks());
        assertEquals(result.getMovementConflicts(),actual.getMovementConflicts());
        assertEquals(result.getValidMoves(),actual.getValidMoves());
        assertEquals(result.getUpdatedCharacters(),actual.getUpdatedCharacters());
    }


    @Test
    void testApplyAttacks()  throws Exception {
        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);



        charactersAfter.remove(lowHealthCharacter2); //legalattack1 martis died--
        highHealthCharacter1.setHealth(15);//legalAttack2 estes health 15
        highHealthCharacter2.setHealth(25);//legalattack3 melissa health 25
        charactersAfter.remove(normalCharacter1);// legalattack4 invalid pattern


        Method method = TurnLogicService.class.getDeclaredMethod("applyAttacks", List.class, List.class);
        method.setAccessible(true);

        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, attacksBefore, charactersBefore);

        assertEquals(charactersAfter, result);
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

        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, movementsBefore, charactersBefore);


        assertEquals(charactersAfter,result);

    }



    @Test
    void testCheckForWinner() {;

        assertNull(checkForWinner(charactersAfter));
        charactersAfter.remove(normalCharacter1);
        charactersAfter.remove(normalCharacter2);
        charactersAfter.remove(lowHealthCharacter1);
        charactersAfter.remove(highHealthCharacter1);
        charactersAfter.remove(lowHealthCharacter2);
        charactersAfter.remove(highHealthCharacter2);
        //characters.remove(closeToDyingCharacter1);
        charactersAfter.remove(longAttackCharacter2);
        assertEquals("1111", checkForWinner(charactersAfter));
        charactersAfter.add(longAttackCharacter2);
        charactersAfter.remove(closeToDyingCharacter1);
        assertEquals("2222", checkForWinner(charactersAfter));
        assertNull(checkForWinner(charactersBefore));
    }

    //test for after applycommands




}