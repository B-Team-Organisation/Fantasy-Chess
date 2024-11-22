package com.bteam.common.services;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public static TurnResult applyCommands(
            List<MovementDataModel> moves,
            List<CharacterEntity> characters,
            List<AttackDataModel> attacks,
            GridModel grid) {

        CommandValidator validatorAllCommands = new CommandValidator();
        List<CharacterEntity> invalidMovers = validatorAllCommands.validateCommands(characters, moves, attacks, grid);

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

        Pair<List<CharacterEntity>, List<Pair<CharacterEntity, CharacterEntity>>> movementResult =
                applyMovement(validMovements, characters);

        List<CharacterEntity> charactersAfterMovement = movementResult.getFirst();
        List<Pair<CharacterEntity, CharacterEntity>> conflicts = movementResult.getSecond();

        List<CharacterEntity> charactersAfterAttacks = applyAttacks(validAttacks, charactersAfterMovement);


        return new TurnResult(charactersAfterAttacks, conflicts);


    }

        /**
         * Apply all movement to the characters
         * <p>
         * @param intendedMovements intended moves given to each character
         * @param characters list of all the Characters
         * @return List of dead characters as {@link CharacterEntity} or empty list
         */

        private static Pair<List<CharacterEntity>, List<Pair<CharacterEntity, CharacterEntity>>> applyMovement(
                List<MovementDataModel> intendedMovements, List<CharacterEntity> characters) {


            List<CharacterEntity> charactersAfterMovement = new ArrayList<>(characters);


            HashMap<Vector2D, List<CharacterEntity>> positionMap = new HashMap<>();
            List<Pair<CharacterEntity, CharacterEntity>> conflicts = new ArrayList<>();


            for (MovementDataModel movement : intendedMovements) {
                CharacterEntity characterToMove = movement.getCharacterEntity();
                Vector2D newPosition = movement.getMovementVector();



                positionMap.putIfAbsent(newPosition, new ArrayList<>());
                positionMap.get(newPosition).add(characterToMove);

                System.out.println("actualize positionMap: " + positionMap);
                System.out.println("fine");
            }

            for (CharacterEntity character : characters) {
                Vector2D intendedPosition = null;

                System.out.println("number moves: " + intendedMovements.size());
                for (MovementDataModel movement : intendedMovements) {
                    System.out.println("move: Character = " + movement.getCharacterEntity().getCharacterBaseModel().getName()
                            + ", destination = " + movement.getMovementVector());

                    System.out.println("" +movement.getCharacterEntity());
                    System.out.println(
                                    "=============chararacter"+ character +"move"+movement.getMovementVector());
                    System.out.println("?????"+movement.getCharacterEntity().equals(character)+"ergebnis");
                    if (movement.getCharacterEntity().equals(character)) {

                        intendedPosition = movement.getMovementVector();
                        break;
                    }
                }

                if (intendedPosition == null) {
                    System.out.println("No move for " + character.getCharacterBaseModel().getName());
                    continue;
                }

                System.out.println("Character " + character.getCharacterBaseModel().getName()
                        + " wants to go to  " + intendedPosition);

                List<CharacterEntity> charactersAtPosition = positionMap.get(intendedPosition);
                if (charactersAtPosition.size() > 1) {
                    System.out.println("conflict in position: " + intendedPosition
                            + " w Characters: " + charactersAtPosition);
                    for (CharacterEntity conflictingCharacter : charactersAtPosition) {
                        if (!conflictingCharacter.equals(character)) {
                            conflicts.add(new Pair<>(character, conflictingCharacter));
                            System.out.println("Conflict added: "
                                    + character.getCharacterBaseModel().getName() + " <-> "
                                    + conflictingCharacter.getCharacterBaseModel().getName());
                        }
                    }
                } else {

                    for (CharacterEntity charAfterMovement : charactersAfterMovement) {
                        if (charAfterMovement.equals(character)) {
                            System.out.println("Move Character " + character.getCharacterBaseModel().getName()
                                    + " to Position: " + intendedPosition);
                            charAfterMovement.setPosition(intendedPosition);
                            break;
                        }
                    }
                }
            }


            System.out.println("Gefundene conflicts: " + conflicts);


            return new Pair<>(charactersAfterMovement, conflicts);
        }

    /**
     * Apply all attacks to the characters
     * @param intendedAttacks intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */

    private static List<CharacterEntity> applyAttacks(List<AttackDataModel> intendedAttacks, List<CharacterEntity> characters) {
        List<CharacterEntity> charactersAfterAttacks = new ArrayList<>(characters);



        for (AttackDataModel attackMove : intendedAttacks) {
            CharacterEntity attacker = attackMove.getAttacker();
            Vector2D attackPoint = attackMove.getAttackPosition();

            System.out.println("Attacker: " + attacker);
            System.out.println("Attack Position: " + attackPoint);

            Vector2D[] attackArea = attacker.getCharacterBaseModel()
                    .getAttackPatterns()[0]
                    .getAreaOfEffect(attacker.getPosition(), attackPoint);

            System.out.println("base model"+ attacker.getCharacterBaseModel());
            System.out.println("attack pattern"+ attacker.getCharacterBaseModel().getAttackPatterns()[0]);

            System.out.println("Attack Area: " + Arrays.toString(attackArea));

            for (Vector2D affectedPosition : attackArea) {
                for (CharacterEntity target : new ArrayList<>(charactersAfterAttacks)) {
                    if (target.getPosition().equals(affectedPosition) &&
                            !target.getPlayerId().equals(attacker.getPlayerId())) {

                        System.out.println("_____________Character hit: " + target);

                        int newHealth = target.getHealth() - attacker.getCharacterBaseModel().getAttackPower();
                        target.setHealth(newHealth);

                        System.out.println("New Health: " + target.getHealth());
                        if (newHealth <= 0) {
                            System.out.println("____________Character died: " + target);
                            charactersAfterAttacks.remove(target);
                        }
                    }
                }
            }
        }

        return charactersAfterAttacks;
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