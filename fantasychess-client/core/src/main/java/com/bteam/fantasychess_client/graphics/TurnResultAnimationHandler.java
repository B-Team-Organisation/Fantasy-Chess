package com.bteam.fantasychess_client.graphics;

import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.services.TurnResult;
import com.bteam.common.utils.PairNoOrder;
import com.bteam.common.entities.CharacterEntity;

import java.util.ArrayDeque;
import java.util.Map;

/**
 * Class that handles the animation of a turn outcome
 * <p>
 * Animated the turn outcome on basis of a turn result object.
 * Provides methods to start, progress and tell the status of the entire animation.
 *
 * @version 1.o
 * @author lukas jacinto
 */
public class TurnResultAnimationHandler {

    private ArrayDeque<AbstractAnimation> animationQueue;
    private boolean animationStarted;

    /**
     * Constructor
     *
     * @param turnResult {@link TurnResult} to animate
     * @param spriteMapper Object for getting the sprite corresponding to a {@link CharacterEntity}
     */
    public TurnResultAnimationHandler(TurnResult turnResult, Map<String,CharacterSprite> spriteMapper){
        animationQueue = new ArrayDeque<>();

        for (PairNoOrder<MovementDataModel,MovementDataModel> conflict : turnResult.getMovementConflicts()){
            animationQueue.add(new CollisionAnimation(
                conflict.getFirst(),spriteMapper.get(conflict.getFirst().getCharacterId()),
                conflict.getSecond(),spriteMapper.get(conflict.getSecond().getCharacterId()))
            );
        }

        for (MovementDataModel movementDataModel : turnResult.getValidMoves()) {
            animationQueue.add(new MovementAnimation(movementDataModel, spriteMapper.get(movementDataModel.getCharacterId())));
        }

        for (AttackDataModel attackDataModel : turnResult.getValidAttacks()) {
            animationQueue.add(new AttackAnimation(attackDataModel, spriteMapper.get(attackDataModel.getAttacker())));
        }

        animationStarted = false;
    }

    /**
     * Test if the animation has finished playing
     *
     * @return {@code true} if animation is over, else {@code false}
     */
    public boolean isDoneWithAnimation() {
        return animationQueue.isEmpty();
    }

    /**
     * Starts the next part of the animation if the last has finished.
     * <p>
     * Does nothing if the animation hasn't started yet
     */
    public void progressAnimation(){
        if (!animationStarted){
            return;
        }

        if (animationQueue.getFirst().isAnimationOver()){
            animationQueue.pop();
            if (!animationQueue.isEmpty()){
                animationQueue.getFirst().startAnimation();
            }
        }
    }

    /**
     * Starts the animation
     */
    public void startAnimation(){
        animationStarted = true;
        animationQueue.getFirst().startAnimation();
    }
}
