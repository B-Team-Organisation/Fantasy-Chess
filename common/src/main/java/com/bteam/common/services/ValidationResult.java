package com.bteam.common.services;
import com.bteam.common.utils.ListNoOrder;
import com.bteam.common.utils.PairNoOrder;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.AttackDataModel;

import java.util.List;
import java.util.Objects;

/**
 * Represents the result of a validation from {@link CommandValidatorService}.
 * <p>
 * Contains the movement conflicts appearing when two players
 * try to move to the same location as a List of {@link PairNoOrder}
 * of clashing {@link MovementDataModel}'s, as well as a list
 * of all valid movements and attacks, represented by Lists of
 * {@link MovementDataModel}'s and {@link AttackDataModel}'s
 * respectively.
 *
 * @author Jacinto
 * @version 1.0
 */
public class ValidationResult {
    private final List<PairNoOrder<MovementDataModel, MovementDataModel>> movementConflicts;
    private final List<MovementDataModel> validMoves;
    private final List<AttackDataModel> validAttacks;

    /**
     * Create a new ValidationResult object.
     *
     * @param movementConflicts Conflicts from different players trying to move to the same position
     * @param validMoves Moves that have been validated
     * @param validAttacks Attacks that have been validated
     */
    public ValidationResult(
            List<PairNoOrder<MovementDataModel, MovementDataModel>> movementConflicts,
            List<MovementDataModel> validMoves, List<AttackDataModel> validAttacks
    ) {
        this.movementConflicts = movementConflicts;
        this.validMoves = validMoves;
        this.validAttacks = validAttacks;
    }

    public List<PairNoOrder<MovementDataModel, MovementDataModel>> getMovementConflicts() {
        return this.movementConflicts;
    }

    public List<MovementDataModel> getValidMoves() {
        return validMoves;
    }

    public List<AttackDataModel> getValidAttacks() {
        return validAttacks;
    }

    @Override
    public String toString() {
        return "ValidationResult [movementConflicts=" + movementConflicts + ", validMoves="
                + validMoves + ", validAttacks=" + validAttacks + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationResult)) return false;
        ValidationResult that = (ValidationResult) o;
        return new ListNoOrder<>(this.movementConflicts).equals(new ListNoOrder<>(that.movementConflicts))
                && new ListNoOrder<>(this.validMoves).equals(new ListNoOrder<>(that.validMoves))
                && new ListNoOrder<>(this.validAttacks).equals(new ListNoOrder<>(that.validAttacks));
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementConflicts, validMoves, validAttacks);
    }
}
