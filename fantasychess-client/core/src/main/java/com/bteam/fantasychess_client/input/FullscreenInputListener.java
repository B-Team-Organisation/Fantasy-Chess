package com.bteam.fantasychess_client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Fullscreen input-handling
 * <p>
 * This {@link InputAdapter} can be used in an {@link com.badlogic.gdx.InputMultiplexer} to allow the
 * user to enter fullscreen by pressing the F11 key.
 */
public class FullscreenInputListener extends InputAdapter {
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.F11){
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            return true;
        }
        return false;
    }
}
