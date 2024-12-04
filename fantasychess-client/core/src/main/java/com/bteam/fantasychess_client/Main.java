package com.bteam.fantasychess_client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bteam.fantasychess_client.manger.ScreenManager;
import com.bteam.fantasychess_client.networking.WebSocketClient;
import com.bteam.fantasychess_client.networking.WebSocketService;
import com.bteam.fantasychess_client.services.CommandManagementService;
import com.bteam.fantasychess_client.services.LobbyService;
import com.bteam.fantasychess_client.ui.SplashScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static final String WEBSOCKET_ADDRESS = "ws://127.0.0.1:5050/ws";

    private final WebSocketService socketService;
    private final LobbyService lobbyService;
    private final ScreenManager screenManager;
    private final CommandManagementService commandManagementService;

    private final Logger logger = Logger.getLogger("com.bteam.fantasychess_client");
    private Skin skin;

    public Main() {
        socketService = new WebSocketService(WEBSOCKET_ADDRESS, new WebSocketClient());
        lobbyService = new LobbyService();
        screenManager = new ScreenManager();
        commandManagementService = new CommandManagementService();
    }

    public static Main getInstance() {
        return (Main) Gdx.app.getApplicationListener();
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public static WebSocketService getWebSocketService() {
        return getInstance().socketService;
    }

    public static LobbyService getLobbyService() {
        return getInstance().lobbyService;
    }

    public static ScreenManager getScreenManager() {
        return getInstance().screenManager;
    }

    public static CommandManagementService getCommandManagementService() {
        return getInstance().commandManagementService;
    }

    @Override
    public void create() {
        logger.log(Level.SEVERE, "Creating Screen");
        skin = new Skin(Gdx.files.internal("placeholderSkin/skin.json"));
        screenManager.setSkin(skin);
        setScreen(new SplashScreen(skin));
        logger.log(Level.SEVERE, "Set Screen");
    }

    @Override
    public void dispose() {
        skin.dispose();
    }
}
