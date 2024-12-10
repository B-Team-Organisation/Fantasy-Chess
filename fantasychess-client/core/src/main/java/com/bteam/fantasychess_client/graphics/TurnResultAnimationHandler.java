package com.bteam.fantasychess_client.graphics;

import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.services.TurnResult;

import java.util.ArrayDeque;
import java.util.Map;

public class TurnResultAnimationHandler {

    private ArrayDeque<AbstractAnimation> animationQueue;
    private boolean animationStarted;

    public TurnResultAnimationHandler(TurnResult turnResult, Map<String,CharacterSprite> spriteMapper){
        animationQueue = new ArrayDeque<>();

        for (MovementDataModel movementDataModel : turnResult.getValidMoves()) {
            animationQueue.add(new MovementAnimation(movementDataModel, spriteMapper.get(movementDataModel.getCharacterId())));
        }

        for (AttackDataModel attackDataModel : turnResult.getValidAttacks()) {
            animationQueue.add(new AttackAnimation(attackDataModel, spriteMapper.get(attackDataModel.getAttacker())));
        }

        animationStarted = false;
    }

    public boolean isDoneWithAnimation() {
        return animationQueue.isEmpty();
    }

    public void progressAnimation(){
        if (!animationStarted){
            return;
        }

        if (animationQueue.getFirst().isAnimationOver()){
            animationQueue.pop();
            animationQueue.getFirst().startAnimation();
        }
    }

    public void startAnimation(){
        animationStarted = true;
        animationQueue.getFirst().startAnimation();
    }

    // Keine Commands mehr zu zeigen?
    // Zu Command Mode wechseln
    // Wenn es noch kollidierte moves gibt
    // Wenn gerade keine Kollision gezeigt wird
    // Neue Kollision anstoßen
    // Return
    // Wenn Kollision in progress
    // Return
    // Wenn es noch moves gibt
    //      Wenn gerade kein Move gezeigt wird
    // Neuen move anstoßen
    // Return
    // Wenn move in progress
    // Return
    // Wenn es noch Attacken gibt
    // Wenn gerade keine Attacke gezeigt wird
    // Neuen Attacke anstoßen
    // Return
    // Wenn Attak in progress
    // Return



    /*
    public Object getNextAnimationCadidate(){
    }
     */


}
