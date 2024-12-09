package com.bteam.fantasychess_client.graphics;

import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.services.TurnResult;
import com.bteam.common.utils.PairNoOrder;

import java.util.ArrayList;
import java.util.List;

public class TurnResultAnimationQueue {
    List<MovementDataModel> movesToAnimate;
    List<AttackDataModel> attacksToAnimate;
    List<PairNoOrder<MovementDataModel,MovementDataModel>> collisionsToAnimate;

    public Object getNextAnimationCadidate(){
        if (!collisionsToAnimate.isEmpty()) {
            PairNoOrder<MovementDataModel,MovementDataModel> collision = collisionsToAnimate.get(0);
            collisionsToAnimate.remove(0);
            return collision;
        }
        if (!movesToAnimate.isEmpty()){
            MovementDataModel movement = movesToAnimate.get(0);
            movesToAnimate.remove(movement);
            return movement;
        } else if (!attacksToAnimate.isEmpty()){
            AttackDataModel attack = attacksToAnimate.get(0);
            attacksToAnimate.remove(attack);
            return attack;
        }
        return null;
    }

    public TurnResultAnimationQueue (TurnResult turnResult){
        movesToAnimate = new ArrayList<>();
        attacksToAnimate = new ArrayList<>();
        collisionsToAnimate = new ArrayList<>();

        movesToAnimate.addAll(turnResult.getValidMoves());
        attacksToAnimate.addAll(turnResult.getValidAttacks());
        collisionsToAnimate.addAll(turnResult.getMovementConflicts());
    }
}
