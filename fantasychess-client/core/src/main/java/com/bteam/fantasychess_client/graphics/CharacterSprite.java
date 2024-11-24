package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Vector2D;

public class CharacterSprite extends Sprite {
    private CharacterEntity character;

    Vector2 destination;
    Vector2 step;
    int steps = 0;

    public CharacterSprite(TextureRegion textureRegion, Vector2D position, CharacterEntity character) {
        super(textureRegion);
        setPosition(position.getX(), position.getY());
        this.character = character;
    }

    public void moveTo(Vector2D destination) {
        this.destination = new Vector2(destination.getX(), destination.getY());

        Vector2 distanceVector = this.destination.cpy().sub(new Vector2(getX(), getY()));
        float distance = (float)Math.sqrt(distanceVector.x * distanceVector.x
            + distanceVector.y * distanceVector.y);

        steps = (int)distance;
        step = distanceVector.scl(1/distance);
    }

    @Override
    public void draw (Batch batch) {
        if (destination != null) {
            if (steps > 0){
                setPosition(getX()+step.x,getY()+step.y);
                steps--;
            }
            if (steps == 0){
                setPosition(destination.x, destination.y);
                destination = null;
            }
        }
        batch.draw(this, getX()-getWidth()/2, getY()-getHeight()/2);
    }
}
