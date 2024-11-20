package services;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bteam.common.utils.Pair;
import utils.TestUtils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.bteam.common.services.TurnLogicService.*;


class TestTurnLogicService {


    private CharacterEntity normalCharacter1;
    private CharacterEntity normalCharacter2;
    private CharacterEntity closeToDyingCharacter1;
    private CharacterEntity longAttackCharacter2;

    MovementDataModel legalMovement;
    MovementDataModel legalMovement2;
    MovementDataModel legalMovement3;


    AttackDataModel legalAttack;
    AttackDataModel legalAttack2;
    AttackDataModel legalAttack3;

    Player player1 ;
    Player player2 ;
    List<CharacterEntity> charactersBefore;
    List<CharacterEntity> charactersAfter;
    List<MovementDataModel> movementsBefore;
    List<AttackDataModel> attacksBefore;
    @BeforeEach
    void setUp() {



        //TurnLogicService turnLogicService = new TurnLogicService();
        player1 = new Player("Username1","1111",new ArrayList<>());
        player2 = new Player("Username2","2222",new ArrayList<>());

        HashMap<Character, String> map = new HashMap<>();
        map.put('x',"subpattern");

        PatternModel longRangeAttack = new PatternModel("   x   \n   x   \nxxxxxxx\n   x   \n   x   ", map, "longRangeAttack");
        PatternModel shortMovement = new PatternModel(" x \n x \n x ", map, "shortMovement");
        PatternModel[] longRangeAttacks = new PatternModel[]{longRangeAttack};
        PatternModel[] shortMovements = new PatternModel[]{shortMovement};

        Vector2D cornerPosition = new Vector2D(0, 0);
        Vector2D centerPosition = new Vector2D(5, 5);
        Vector2D borderPosition = new Vector2D(9, 0);
        Vector2D farPosition = new Vector2D(8, 8);


        normalCharacter1= new CharacterEntity(
                new CharacterDataModel("Hallo", "Hehe",
                        10, 34,longRangeAttacks, shortMovements ),
                10,cornerPosition, player1.getPlayerId()
        );

        normalCharacter2 = new CharacterEntity(
                new CharacterDataModel("Amon", "Test1",
                        20, 15, longRangeAttacks, shortMovements),
                20, centerPosition, player2.getPlayerId()
        );

        closeToDyingCharacter1 = new CharacterEntity(
                new CharacterDataModel("Granger", "A character with low health",
                        5, 10, longRangeAttacks, shortMovements),
                5, borderPosition, player1.getPlayerId()
        );

        longAttackCharacter2 = new CharacterEntity(
                new CharacterDataModel("Lud Yi", "Specializes in long-range attacks",
                        15, 20, longRangeAttacks, shortMovements),
                15, farPosition, player2.getPlayerId()
        );






        legalMovement = new MovementDataModel(normalCharacter1,new Vector2D(0,5) );
        legalMovement2 = new MovementDataModel(normalCharacter2,new Vector2D(2,2) );
        legalMovement3 = new MovementDataModel(closeToDyingCharacter1,new Vector2D(1,1) );

        legalAttack = new AttackDataModel(new Vector2D(0,1),normalCharacter1);
        legalAttack2 = new AttackDataModel(new Vector2D(2,1),normalCharacter2);
        legalAttack3 = new AttackDataModel(new Vector2D(1,1),longAttackCharacter2);






       /* player1.addCharacter(normalCharacter1);
        player2.addCharacter(normalCharacter2);
        player1.addCharacter(closeToDyingCharacter);
        player2.addCharacter(longAttackCharacter);*/

        charactersBefore = new ArrayList<>();
        charactersAfter = new ArrayList<>();
        movementsBefore = new ArrayList<>();
        attacksBefore = new ArrayList<>();


        charactersAfter.add(normalCharacter1);
        charactersAfter.add(normalCharacter2);
        charactersAfter.add(closeToDyingCharacter1);
        charactersAfter.add(longAttackCharacter2);

        movementsBefore.add(legalMovement);
        movementsBefore.add(legalMovement2);
        movementsBefore.add(legalMovement3);

        attacksBefore.add(legalAttack);
        attacksBefore.add(legalAttack2);
        attacksBefore.add(legalAttack3);


        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);


    }

    @Test

    void testApplyCommands() {

        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);


       assertEquals(charactersAfter,applyCommands(movementsBefore,charactersBefore,attacksBefore,new GridModel(10,10)));

    }


    @Test
    void testApplyAttacks()  throws Exception {
        charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);
        charactersAfter.remove(normalCharacter1);
        charactersAfter.remove(normalCharacter2);
        Method method = TurnLogicService.class.getDeclaredMethod("applyAttacks", List.class, List.class);
        method.setAccessible(true);

        List<CharacterEntity> result = (List<CharacterEntity>) method.invoke(null, movementsBefore, charactersBefore);

        assertEquals(charactersAfter, result);
    }

     @Test
     void testApplyMovement() throws Exception {

         charactersBefore = TestUtils.deepCopyCharacterList(charactersAfter);
         normalCharacter1.setPosition(new Vector2D(0,5));
         normalCharacter2.setPosition(new Vector2D(7,7));
         closeToDyingCharacter1.setPosition(new Vector2D(10,1));
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