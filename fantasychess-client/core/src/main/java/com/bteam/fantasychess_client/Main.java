package com.bteam.fantasychess_client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.networking.WebSocketService;
import com.bteam.fantasychess_client.ui.SplashScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {

    Logger logger = Logger.getLogger("com.bteam.fantasychess_client");
    WebSocketService socketService;

    public static Main GetInstance() {
        return (Main) Gdx.app.getApplicationListener();
    }

    public static Logger GetLogger() {
        return GetInstance().logger;
    }

    @Override
    public void create() {
        var address = "";
        var port = 5050;
        socketService = new WebSocketService(address, port);
        logger.log(Level.SEVERE, "Websocket Address: " + address + port);
        var config = Gdx.files.internal("application.properties").readString();
        logger.log(Level.SEVERE, config);
        Skin skin = new Skin(Gdx.files.internal("placeholderSkin/skin.json"));
        setScreen(new SplashScreen(skin));
    }

    public WebSocketService getSocketService() {
        return socketService;
    }

    public Logger getLogger() {
        return logger;
    }
}
