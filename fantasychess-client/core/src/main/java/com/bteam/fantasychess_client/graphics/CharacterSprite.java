package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Vector2D;

public class CharacterSprite extends Sprite {
    private CharacterEntity character;

    Vector2D position;
    Vector2D destination;

    public CharacterSprite(TextureRegion textureRegion, Vector2D position, CharacterEntity character) {
        super(textureRegion);
        setPosition(position.getX(), position.getY());
        this.position = position;
        this.character = character;
    }

    public void moveTo(Vector2D destination) {
        this.destination = destination;

        Vector2D distanceVector = destination.subtract(position);
        float distance = (float)Math.sqrt(distanceVector.getX() * distanceVector.getX()
            + distanceVector.getY() * distanceVector.getY());
    }

    @Override
    public void draw (Batch batch) {
        if (destination != null) {

        }
    }
}
