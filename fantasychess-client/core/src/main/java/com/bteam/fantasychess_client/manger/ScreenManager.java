package com.bteam.fantasychess_client.manger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.ui.GameScreen;
import com.bteam.fantasychess_client.ui.MainMenu;
import com.bteam.fantasychess_client.ui.SplashScreen;

import java.util.logging.Level;

/**
 * A class to easily manage what screen to navigate to and to combine the skin usage as to not have to
 * parse it every single time, also gives screens a single ID
 *
 * @author Marc
 */

public class ScreenManager {
    public static final String MAIN_MENU_SCREEN = "main_menu";
    public static final String SPLASH_SCREEN = "splash";
    public static final String GAME_SCREEN = "game";

    private Skin skin;

    public ScreenManager() {
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public void navigateTo(String screen) {
        Gdx.app.postRunnable(() -> navigateToNonRunnable(screen));
    }

    public void navigateToNonRunnable(String screen) {
        switch (screen) {
            case MAIN_MENU_SCREEN:
                (Main.getInstance()).setScreen(new MainMenu(skin));
                break;
            case SPLASH_SCREEN:
                (Main.getInstance()).setScreen(new SplashScreen(skin));
                break;
            case GAME_SCREEN:
                (Main.getInstance()).setScreen(new GameScreen(skin));
                break;
            default:
                Main.getLogger().log(Level.SEVERE, "Not able to navigate to " + screen);
                break;
        }
    }
}
