package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.Main;

public class MainMenu extends ScreenAdapter {

    private final Skin skin;

    public MainMenu(Skin skin) {
        this.skin = skin;
    }

    @Override
    public void show() {
        super.show();
        Main.getWebSocketService().connect();
    }
}
