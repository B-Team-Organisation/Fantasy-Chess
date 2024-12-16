package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.MovementDataModel;

/**
 * An animation object for animating a movement collision
 *
 * @author lukas
 */
public class CollisionAnimation extends AbstractAnimation {
    private MovementDataModel movementDataModel1;
    private CharacterSprite sprite1;

    private MovementDataModel movementDataModel2;
    private CharacterSprite sprite2;

    /**
     * Constructor
     *
     * @param movementDataModel1 {@link MovementDataModel} of the first piece in the collision
     * @param sprite1 {@link CharacterSprite} of the first piece in the collision
     * @param movementDataModel2 {@link MovementDataModel} of the second piece in the collision
     * @param sprite2 {@link CharacterSprite} of the second piece in the collision
     */
    public CollisionAnimation(MovementDataModel movementDataModel1, CharacterSprite sprite1, MovementDataModel movementDataModel2, CharacterSprite sprite2){
        this.movementDataModel1 = movementDataModel1;
        this.sprite1 = sprite1;

        this.movementDataModel2 = movementDataModel2;
        this.sprite2 = sprite2;
    }

    @Override
    public boolean isAnimationOver(){
        return !(sprite1.isInAnimation() && sprite2.isInAnimation());
    }

    @Override
    public void startAnimation(){
        Vector2 origin = new Vector2(sprite1.getX(),sprite1.getY());
        sprite1.moveToGridPos(movementDataModel1.getMovementVector());
        sprite1.moveToWorldPos(origin);

        origin = new Vector2(sprite2.getX(),sprite2.getY());
        sprite2.moveToGridPos(movementDataModel2.getMovementVector());
        sprite2.moveToWorldPos(origin);
    }
}
