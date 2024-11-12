package services;
import entities.CharacterEntity;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class TestTurnLogicService {

    @BeforeEach
    void setUp() {
        TurnLogicService turnLogicService = new TurnLogicService();


        AttackPatternDataModel attack = new AttackPatternDataModel()
        CharacterDataModel characterDataModel = new CharacterDataModel("Hallo", "Hehe", 10, )


                /*
        CharacterEntity normalCharacter1 = new CharacterEntity(
                new CharacterDataModel("Example1", "Info1", 20,
                ), 10, new Vector2D(6, 6));
        CharacterEntity normalCharacter2 = new CharacterEntity(
                new CharacterDataModel(

                ), 10, new Vector2D(3, 3));
        CharacterEntity dyingCharacter = new CharacterEntity(
                new CharacterDataModel(

                ), 1, new Vector2D(5, 5));
        CharacterEntity closeToGridCharacter = new CharacterEntity(
                new CharacterDataModel(

                ), 10, new Vector2D(0,0));
        MovementDataModel normalMovementForward = new MovementDataModel(normalCharacter1,new Vector2D(0,2));
        MovementDataModel movementOffTheGrid = new MovementDataModel(normalCharacter1,new Vector2D(0,-2));
        AttackDataModel attackOffTheGrid = new AttackDataModel(new Vector2D(0,-2),normalCharacter1);
        AttackDataModel normalAttackForward = new AttackDataModel(new Vector2D(0,2),normalCharacter1);
        // ToDO: AttackPatternDataModel must be removed



    }


    @Test
    void testIsMovementLegal() {
       // turnLogicService.isMovementLegal(, movement);

    }

    @Test
    void testCheckForWinner(){
    }

   @Test
    void testCheckForDeaths(){

    }

   @Test
   void testIsAttackLegal(){

   }

   @Test
   void testMovingToSameLocation(){

   }

   @Test
    void testCheckForIllegalMovement(){

   }


}