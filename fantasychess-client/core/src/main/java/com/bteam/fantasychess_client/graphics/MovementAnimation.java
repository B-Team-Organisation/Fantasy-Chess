package com.bteam.fantasychess_client.graphics;

import com.bteam.common.models.MovementDataModel;
import com.bteam.common.entities.CharacterEntity;

/**
 * An animation object for animating a valid move
 * <p>
 * Animates the movement of a piece on the grid.
 *
 * @author jacinto lukas
 */
public class MovementAnimation extends AbstractAnimation {

    private MovementDataModel movementDataModel;
    private CharacterSprite sprite;

    /**
     * Constructor
     * @param movementDataModel {@link MovementDataModel} to animate
     * @param sprite {@link CharacterSprite} of the {@link CharacterEntity} performing the movoe
     */
    public MovementAnimation(MovementDataModel movementDataModel, CharacterSprite sprite){
        this.movementDataModel = movementDataModel;
        this.sprite = sprite;
    }

    @Override
    public boolean isAnimationOver(){
        return !sprite.isInAnimation();
    }

    @Override
    public void startAnimation(){
        sprite.moveToGridPos(movementDataModel.getMovementVector());
    }

}
