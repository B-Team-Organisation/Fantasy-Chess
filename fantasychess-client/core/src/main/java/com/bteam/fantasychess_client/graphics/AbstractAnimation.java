package com.bteam.fantasychess_client.graphics;

/**
 * Abstract class for a turn outcome animation
 * <p>
 * Extended by all animations of the turn outcome animation phase.
 * Provides methods to start and track the progress of the element of the animation.
 *
 * @author jacinto lukas
 */
public abstract class AbstractAnimation {
    /**
     * Method to test if the animated has finished playing
     *
     * @return {@code true} if the animation is over, else {@code false}
     */
    public abstract boolean isAnimationOver();

    /**
     * Method to start the animation
     */
    public abstract void startAnimation();
}
