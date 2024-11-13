package services;
import entities.CharacterEntity;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Pair;


import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TestTurnLogicService {

    TurnLogicService turnLogicService = new TurnLogicService();
    private CharacterEntity normalCharacter1;
    private CharacterEntity normalCharacter2;
    private CharacterEntity closeToDyingCharacter;
    private CharacterEntity longAttackCharacter;

    MovementDataModel legalMovement;
    MovementDataModel illegalMovement ;
    MovementDataModel forbiddenPatternMovement ;
    MovementDataModel occupiedMovement ;

    AttackDataModel legalAttack;
    AttackDataModel illegalAttack;
    AttackDataModel forbiddenPatternAttack ;

    @BeforeEach
    void setUp() {



        //TurnLogicService turnLogicService = new TurnLogicService();


        HashMap<Character, String> map = new HashMap<>();
        map.put('x',"subpattern");


        legalMovement = new MovementDataModel(normalCharacter1,new Vector2D(0,5) );
        illegalMovement = new MovementDataModel(normalCharacter2, new Vector2D(4, -1));
        forbiddenPatternMovement = new MovementDataModel(closeToDyingCharacter,new Vector2D(2,2) );
        occupiedMovement = new MovementDataModel(longAttackCharacter, new Vector2D(5,5));

        legalAttack = new AttackDataModel(new Vector2D(0,1),normalCharacter1);
        illegalAttack = new AttackDataModel(new Vector2D(5,-1),normalCharacter2);
        forbiddenPatternAttack = new AttackDataModel(new Vector2D(2,2),closeToDyingCharacter);


        PatternModel longRangeAttack = new PatternModel("   x   \n   x   \nxxxxxxx\n   x   \n   x   ", map, "longRangeAttack");
        PatternModel shortMovement = new PatternModel(" x \n x \n x ", map, "shortMovement");
        PatternModel[] longRangeAttacks = new PatternModel[]{longRangeAttack};
        PatternModel[] shortMovements = new PatternModel[]{shortMovement};

        Vector2D cornerPosition = new Vector2D(0, 0);
        Vector2D centerPosition = new Vector2D(5, 5);
        Vector2D borderPosition = new Vector2D(9, 0);
        Vector2D farPosition = new Vector2D(8, 8);


        normalCharacter1= new CharacterEntity(
                new CharacterDataModel("Hallo", "Hehe", 10, 34,longRangeAttacks, shortMovements ),
                10,cornerPosition)
        ;

        normalCharacter2 = new CharacterEntity(
                new CharacterDataModel("Amon", "Test1", 20, 15, longRangeAttacks, shortMovements),
                20, centerPosition
        );

        closeToDyingCharacter = new CharacterEntity(
                new CharacterDataModel("Granger", "A character with low health", 5, 10, longRangeAttacks, shortMovements),
                5, borderPosition
        );

        longAttackCharacter = new CharacterEntity(
                new CharacterDataModel("Lud Yi", "Specializes in long-range attacks", 15, 20, longRangeAttacks, shortMovements),
                15, farPosition
        );

    }
    @Test
    void testIsMovementLegal() {


        assertTrue(turnLogicService.isMovementLegal(normalCharacter1, legalMovement));
        assertFalse(turnLogicService.isMovementLegal(normalCharacter2, illegalMovement));
        assertFalse(turnLogicService.isMovementLegal(closeToDyingCharacter,forbiddenPatternMovement));
        assertFalse(turnLogicService.isMovementLegal(longAttackCharacter,occupiedMovement));
    }



    @Test
    void testCheckForWinner() {

        //TODO player1 win, player2 win, list of Characters
        assertEquals(TurnLogicService.Winner.NONE, turnLogicService.checkForWinner());


        closeToDyingCharacter.setHealth(0);
        assertEquals(TurnLogicService.Winner.NONE, turnLogicService.checkForWinner());


        normalCharacter1.setHealth(0);
        normalCharacter2.setHealth(0);
        longAttackCharacter.setHealth(0);
        closeToDyingCharacter.setHealth(0);
        assertEquals(TurnLogicService.Winner.DRAW, turnLogicService.checkForWinner());


        normalCharacter1.setHealth(100);
        //assertEquals(TurnLogicService.Winner.? turnLogicService.checkForWinner());
    }

    @Test
    void testCheckForDeaths(){
        CharacterEntity[] noDeathCharacters = turnLogicService.checkForDeaths();

        closeToDyingCharacter.setHealth(0);
        normalCharacter2.setHealth(0);

        CharacterEntity[] twoDeadCharacters = turnLogicService.checkForDeaths();


        assertEquals(0 , noDeathCharacters.length);
        assertTrue(List.of(twoDeadCharacters).contains(closeToDyingCharacter));
        assertEquals(2, twoDeadCharacters.length);
        assertTrue(List.of(twoDeadCharacters).contains(normalCharacter2));

    }

    @Test
    void testIsAttackLegal(){
        assertTrue(turnLogicService.isAttackLegal(normalCharacter1,legalAttack));
        assertFalse(turnLogicService.isAttackLegal(normalCharacter2,illegalAttack));
        assertFalse(turnLogicService.isAttackLegal(closeToDyingCharacter,forbiddenPatternAttack));

    }


    @Test
    void testMovingToSameLocation() {

        MovementDataModel move1 = new MovementDataModel(normalCharacter1, new Vector2D(2, 2));
        MovementDataModel move2 = new MovementDataModel(normalCharacter2, new Vector2D(2, 2));
        MovementDataModel move3 = new MovementDataModel(closeToDyingCharacter, new Vector2D(1, 1));
        MovementDataModel move4 = new MovementDataModel(longAttackCharacter, new Vector2D(3, 3));



        MovementDataModel[] turnMoves = new MovementDataModel[]{move1, move2, move3, move4};

        List<Pair<CharacterEntity,CharacterEntity>> result= turnLogicService.movingToSameLocation(turnMoves);
        assertEquals(1, result.size());
        Pair<CharacterEntity, CharacterEntity> pair = result.getFirst();

        assertTrue(
                (pair.getFirst().equals(normalCharacter1) && pair.getSecond().equals(normalCharacter2)) ||
                        (pair.getFirst().equals(normalCharacter2) && pair.getSecond().equals(normalCharacter1))
        );
    }


    @Test
    void testCheckForIllegalMovement() {
        //TODO
        //where to save the moves per round?
    }


}