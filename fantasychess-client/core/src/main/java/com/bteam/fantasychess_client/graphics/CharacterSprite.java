package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.utils.TileMathService;

/**
 * Combination of a {@link CharacterEntity} and a {@link Sprite}
 * <p>
 * Makes it convenient to render and move the entity on the {@link com.bteam.fantasychess_client.ui.GameScreen}
 *
 *
 * @author lukas
 */
public class CharacterSprite extends Sprite {
    private CharacterEntity character;
    private TileMathService mathService;

    private final float MOVEMENT_SPEED = 40f^^;
    Vector2 destination;
    Vector2 direction;
    float distance;

    /**
     * Constructor for CharacterSprites
     *
     * @param textureRegion the {@link TextureRegion which contains the texture for the Sprite}
     * @param position the {@link Vector2D} grid Position of the entity
     * @param character the underlying {@link CharacterEntity} that this sprite represents
     */
    public CharacterSprite(TextureRegion textureRegion, Vector2D position, CharacterEntity character, TileMathService mathService) {
        super(textureRegion);
        this.mathService = mathService;
        setPositionInWorld(mathService.gridToWorld(position.getX(), position.getY()));
        this.character = character;
    }

    /**
     * Makes the sprite move from its position to the specified grid position
     *
     * @param destination the destination in grid coordinates
     */
    public void moveToGridPos(Vector2D destination) {
        Vector2 worldDestination = mathService.gridToWorld(destination.getX(), destination.getY());
        moveToWorldPos(worldDestination);
    }

    /**
     * Makes the sprite move from its position the specified world position
     *
     * @param destination the destination in world coordinates
     */
    public void moveToWorldPos(Vector2 destination) {
        this.destination = new Vector2(destination.x, destination.y);

        Vector2 distanceVector = this.destination.cpy().sub(new Vector2(getX(), getY()));
        distance = (float)Math.sqrt(distanceVector.x * distanceVector.x + distanceVector.y * distanceVector.y);

        direction = distanceVector.nor();

        //steps = (int)distance;
        //step = distanceVector.scl(1/distance);
    }

    /**
     * Sets the position of the character in world coordinates
     * <p>
     * This method makes it possible to call setPosition using a Vector2 as input.
     *
     * @param position the {@link Vector2} that contains the x and y coordinates passed through to the setPosition method
     */
    public void setPositionInWorld(Vector2 position) {
        setPosition(position.x, position.y);
    }

    /**
     * Updates the sprite
     * <p>
     * Updates the sprites position if its moving
     *
     * @param delta the time in seconds since the last frame
     *
     * @return the {@link CharacterEntity} for chaining
     */
    public CharacterSprite update(float delta){
        if (destination != null) {
            if (distance > 0){
                distance -= MOVEMENT_SPEED * delta;
                setPosition(getX()+direction.x*delta*MOVEMENT_SPEED,getY()+direction.y*delta*MOVEMENT_SPEED);
            }
            if (distance <= 0){
                setPosition(destination.x, destination.y);
                destination = null;
                distance = 0;
                direction = null;
            }
        }
        return this;
    }

    /**
     * Draws the sprite using the batch
     * <p>
     * The offset is calculated using the width and height of the sprite so it's centered around its position.
     *
     * @param batch the {@link Batch} object used for drawing
     */
    @Override
    public void draw (Batch batch) {
        batch.draw(this, getX()-getWidth()/2, getY()-getHeight()/2);
    }
}
