package com.bteam.common.services;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Player;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.CharacterDataModel;
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

     public enum Winner{None,Draw,Player1,Player2}
     public void applyCommands(List<MovementDataModel> moves,List<CharacterEntity> characters,List<AttackDataModel> attacks){}
    /**
     * Apply all movement to the characters
     * @param intendedMovements intended moves given to each character
     * @param characters list of all the Characters
     * @return List of dead characters as {@link CharacterEntity} or empty list
     */
    public static List<CharacterEntity> applyMovement(List <MovementDataModel> intendedMovements,List<CharacterEntity> characters) {
       return new ArrayList<>();
    }

    /**
     * Checks if a player (or both) have died.
     * <p>
     * If one player dies, the other is returned as the winner
     * This method won't check for a draw, since that means no
     * {@link CharacterEntity}'s left to pass.
     *
     * @param players The two players competing
     * @return The id of the winning player or {@code null} if no result.
     */
    public static Winner checkForWinner(Pair<Player, Player> players) {
        List<CharacterEntity> deadCharacters = new ArrayList<>();

        return Winner.None;
    }
    /**
     * List all characters that are moving to the same position
     *
     * @param characterMovements characters and their respective movement to check, {@code null} for no movement
     * @return Array of characters that are moving to the same position
     */
    private static CharacterEntity[] movingToSamePosition(Pair<CharacterEntity, MovementDataModel>[] characterMovements) {
        ArrayList<CharacterEntity> illegalMovers = new ArrayList<>();
        // checks with methods below
        return illegalMovers.toArray(new CharacterEntity[illegalMovers.size()]);
    }

    /**

}