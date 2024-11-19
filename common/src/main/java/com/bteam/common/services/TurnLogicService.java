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
    public static List <CharacterEntity> applyCommands(List<MovementDataModel> moves, List<CharacterEntity> characters,
                                                List<AttackDataModel> attacks, GridModel grid) {
        return new ArrayList<>();
    }


    /**
     * Apply all movement to the characters
     * <p>
     * @param intendedMovements intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */

    private static List<CharacterEntity> applyMovement(List <MovementDataModel> intendedMovements,List<CharacterEntity> characters) {
       return new ArrayList<>();
    }


    /**
     * Apply all attacks to the characters
     * @param intendedAttacks intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */

    private static List<CharacterEntity> applyAttacks(List <AttackDataModel> intendedAttacks,List<CharacterEntity> characters) {
        return new ArrayList<>();
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
        List<CharacterEntity> deadCharacters = new ArrayList<>();

        return "example";
    }




}