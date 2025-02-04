package com.bteam.common.models;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.utils.PairNoOrder;
import java.util.List;
import java.util.Objects;


/**
 * Represents the result of a turn in the game
 *<p>
 * Contains the updated characters position and status,valid {@link MovementDataModel}
 * valid {@link AttackDataModel}attacks and movement conflicts where opponent characters
 * tried to move to the same position.
 *
 * @author Albano, Jacinto, Marc
 * @version 1.0
  */
public class TurnResult {
    private final List<CharacterEntity> updatedCharacters;
    private final List<PairNoOrder<MovementDataModel, MovementDataModel>> movementConflicts;
    private final List <MovementDataModel> validMoves;
    private final List <AttackDataModel> validAttacks;
    private final String winner;

    /**
     * New TurnResult relative to the given round
     *
     * @param updatedCharacters List of characters representing their final states
     *                          (positions and health) after the turn
     * @param movementConflicts List of pairs representing conflicts where two characters
     *                          tried to move to the same position. Each pair contains the conflicting movements
     * @param validMoves        List of movement commands that were successfully executed during the turn
     * @param validAttacks      List of attack commands that were successfully executed during the turn
     */

    public TurnResult(List<CharacterEntity> updatedCharacters,
                      List<PairNoOrder<MovementDataModel, MovementDataModel>> movementConflicts,
                      List<MovementDataModel> validMoves,
                      List<AttackDataModel> validAttacks, String winner) {
        this.updatedCharacters = updatedCharacters;
        this.movementConflicts = movementConflicts;
        this.validMoves = validMoves;
        this.validAttacks = validAttacks;
        this.winner = winner;
    }

    public List<CharacterEntity> getUpdatedCharacters() {
        return updatedCharacters;
    }

    public List<PairNoOrder<MovementDataModel, MovementDataModel>> getMovementConflicts() {
        return movementConflicts;
    }

    public List<MovementDataModel> getValidMoves() {
        return validMoves;
    }

    public List<AttackDataModel> getValidAttacks() {
        return validAttacks;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        return "TurnResult [updatedCharacters=" + updatedCharacters + ", movementConflicts=" + movementConflicts
                + ", validMoves=" + validMoves + ", validAttacks=" + validAttacks + ", winner= " + winner + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TurnResult && updatedCharacters.equals(((TurnResult) o).updatedCharacters)
                && movementConflicts.equals(((TurnResult) o).movementConflicts)
                && validMoves.equals(((TurnResult) o).validMoves)
                && validAttacks.equals(((TurnResult) o).validAttacks)
                && winner.equals(((TurnResult) o).winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updatedCharacters, movementConflicts, validMoves, validAttacks, winner);
    }
}
