package utils;

import com.bteam.common.models.TurnResult;
import com.bteam.common.utils.ListNoOrder;

public class TurnResultNoOrder {
    private final TurnResult turnResult;

    public TurnResultNoOrder(TurnResult turnResult) {
        this.turnResult = turnResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurnResultNoOrder)) return false;
        TurnResultNoOrder that = (TurnResultNoOrder) o;

        return new ListNoOrder<>(turnResult.getMovementConflicts())
                    .equals(new ListNoOrder<>(that.turnResult.getMovementConflicts()))
                && new ListNoOrder<>(turnResult.getUpdatedCharacters())
                    .equals(new ListNoOrder<>(that.turnResult.getUpdatedCharacters()))
                && new ListNoOrder<>(turnResult.getValidMoves())
                    .equals(new ListNoOrder<>(that.turnResult.getValidMoves()))
                && new ListNoOrder<>(turnResult.getValidAttacks())
                    .equals(new ListNoOrder<>(that.turnResult.getValidAttacks()));
    }

    @Override
    public String toString() {
        return turnResult.toString();
    }

    @Override
    public int hashCode() {
        return turnResult.hashCode();
    }
}
