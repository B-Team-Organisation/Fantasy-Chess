package com.bteam.common.services;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.common.utils.Pair;

/**
 * Service for turn-based logic checks.
 * <p>
 * A service class providing services to make checks which are
 * necessary for each turn, namely movement, attacks, character deaths
 * and winning the game.
 *
 * @author Jacinto, Albano
 * @version 1.0
 */
public class TurnLogicService {

    private TurnLogicService() {}

    /**
     * Apply all the commands to the characters
     * <p>
     * Applies the commands , moves and attacks , to the given characters
     * @param moves List of the movement received
     * @param characters List of all characters
     * @param attacks List of the attacks received
     * @param grid model with number of rows and columns
     */
    public static  List <CharacterEntity> applyCommands(List<MovementDataModel> moves, List<CharacterEntity> characters,
                                                List<AttackDataModel> attacks, GridModel grid) {


        CommandValidator validatorAllCommands = new CommandValidator();
        List<CharacterEntity> invalidMovers = validatorAllCommands.validateCommands(characters, moves, attacks,grid);

        List<MovementDataModel> validMovements = new ArrayList<>();
        for (MovementDataModel move : moves) {
            if (!invalidMovers.contains(move.getCharacterEntity())) {
                validMovements.add(move);
            }
        }
        List<AttackDataModel> validAttacks = new ArrayList<>();
        for (AttackDataModel attack : attacks) {
            if (!invalidMovers.contains(attack.getAttacker())) {
                validAttacks.add(attack);
            }
        }

        List<CharacterEntity> charactersAfterMovement = applyMovement(validMovements, characters);
        return applyAttacks(validAttacks, charactersAfterMovement);
    }



    /**
     * Apply all movement to the characters
     * <p>
     * @param intendedMovements intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */

    private static List<CharacterEntity> applyMovement(List<MovementDataModel> intendedMovements, List<CharacterEntity> characters) {
        List<CharacterEntity> characterAfterMovement = new ArrayList<>(characters);

        for (MovementDataModel movement : intendedMovements) {
            CharacterEntity characterToMove = movement.getCharacterEntity();

            for (CharacterEntity character : characterAfterMovement) {
                if (character.getCharacterBaseModel().equals(characterToMove.getCharacterBaseModel())
                        && character.getPlayerId().equals(characterToMove.getPlayerId())) {

                    character.setPosition(character.getPosition().add(movement.getMovementVector()));
                    break;
                }
            }
        }

        return characterAfterMovement;
    }





    /**
     * Apply all attacks to the characters
     * @param intendedAttacks intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */

    private static List<CharacterEntity> applyAttacks(List <AttackDataModel> intendedAttacks,List<CharacterEntity> characters) {
        List<CharacterEntity> charactersAfterAttacks = new ArrayList<>();

        for (AttackDataModel attackMove : intendedAttacks) {
            CharacterEntity attacker = attackMove.getAttacker();
            Vector2D attackPoint = attacker.getPosition().add(attackMove.getAttackPosition());

            // List<Vector2D> attackArea=  area of attack per character.
            //for attack per attackArea: do damage if a character there
            //set health,
            // if not dead list.add()


        }
        return new ArrayList<>(characters);
    }
    /**
     * Checks if the Game has a Winner
     * <p>
     * If one player dies, the other is returned as the winner
     * This method won't check for a draw, since that means no
     * {@link CharacterEntity}'s left to pass.
     *
     * @param  characters List of the characters
     * @return The id of the winning player or {@code null} if no result.
     */
    public static String checkForWinner(List<CharacterEntity> characters) {

        String playerId = null;

        for (CharacterEntity character : characters) {
            if (playerId ==null) {
                playerId = character.getPlayerId();
            }
            else if (!playerId.equals(character.getPlayerId() )) {
                return null;
            }
        }
        return playerId;
    }


}