package com.bteam.fantasychess_client.graphics;

import com.bteam.common.models.MovementDataModel;

public class MovementAnimation extends AbstractAnimation {

    private MovementDataModel movementDataModel;
    private CharacterSprite sprite;

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
