package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.entities.CharacterEntity;

/**
 * An animation object for animating a valid attack
 * <p>
 * Animates the attack of a piece on the grid.
 *
 * @author jacinto lukas
 */
public class AttackAnimation extends AbstractAnimation {
    private AttackDataModel attackDataModel;
    private CharacterSprite sprite;

    /**
     * Constructor
     *
     * @param attackDataModel {@link AttackDataModel} of the animated attack
     * @param sprite {@link CharacterSprite} of the {@link CharacterEntity} performing the attack
     */
    public AttackAnimation(AttackDataModel attackDataModel, CharacterSprite sprite) {
        this.attackDataModel = attackDataModel;
        this.sprite = sprite;
    }

    @Override
    public boolean isAnimationOver(){
        return !sprite.isInAnimation();
    }

    @Override
    public void startAnimation(){
        Vector2 initialPos = new Vector2(sprite.getX(), sprite.getY());
        sprite.moveToGridPos(attackDataModel.getAttackPosition());
        sprite.moveToWorldPos(initialPos);
    }

}
