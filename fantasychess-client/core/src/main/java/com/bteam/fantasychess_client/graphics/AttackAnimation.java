package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.Vector2D;

public class AttackAnimation extends AbstractAnimation {
    private AttackDataModel attackDataModel;
    private CharacterSprite sprite;

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
