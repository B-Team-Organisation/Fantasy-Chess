package com.bteam.fantasychess_client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.ui.SplashScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("placeholderSkin/skin.json"));
        setScreen(new SplashScreen(skin));
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
