package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.input.FullscreenInputListener;

import static com.bteam.fantasychess_client.Main.getScreenManager;
import static com.bteam.fantasychess_client.Main.getWebSocketService;
import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

/**
 * Splash-Screen
 * <p>
 * This screen is the first screen you see when opening the game.
 * It forces the player to choose a username to proceed.
 * It also displays basic information about the game.
 *
 * @author lukas dania
 * @version 1.1
 */
public class SplashScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;
    private final Skin skin;
    private Stage stage;

    public SplashScreen(Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920, 1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }

    @Override
    public void show() {
        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        // Main table
        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(20);

        // Title label
        Label titleLabel = new Label("Fantasy-Chess", skin, "title");
        titleLabel.setFontScale(1f);

        // Main widgets
        TextField usernameInput = new TextField("Username", skin);
        TextButton playButton = new TextButton("Play!", skin);
        playButton.setDisabled(true);

        // User input logic
        onChange(usernameInput, () -> {
            playButton.setDisabled(usernameInput.getText().length() < 4);
        });

        usernameInput.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    if (usernameInput.getText().equals("Username")) {
                        usernameInput.setText("");
                    }
                }
            }
        });

        usernameInput.setTextFieldFilter((textField, c) -> Character.isLetterOrDigit(c));

        // Playbutton logic
        onChange(playButton, () -> {
            Gdx.app.getPreferences("userinfo").putString("username", usernameInput.getText());
            Gdx.app.getPreferences("userinfo").flush();
            Gdx.app.postRunnable(() -> getWebSocketService().registerAndConnect(usernameInput.getText()));
            getWebSocketService().getClient().onOpenEvent.addListener(s -> {
                Main.getScreenManager().navigateTo(Screens.MainMenu);
            });
        });

        // Main scene composition
        table.add(titleLabel).row();
        table.add(usernameInput).row();
        table.add(playButton);
        stage.addActor(table);

        // Version label
        Label versionLabel = new Label("Version 0.9.0", skin);
        versionLabel.setPosition(20, 40, Align.bottomLeft);
        stage.addActor(versionLabel);

        // Credit label
        Label creditLabel = new Label("Brought to you by the B-Team!", skin);
        creditLabel.setPosition(20, 20, Align.bottomLeft);
        stage.addActor(creditLabel);

        // Fullscreen button
        Table fullscreenTable = new Table();
        fullscreenTable.setFillParent(true);
        fullscreenTable.left().top();

        TextButton fullscreenButton = new TextButton("Fullscreen", skin);
        fullscreenTable.add(fullscreenButton).pad(20);
        stage.addActor(fullscreenTable);
        onChange(fullscreenButton, () -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()));

        // Input handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        getWebSocketService().getClient().onCloseEvent.clear();
        getWebSocketService().getClient().onCloseEvent.addListener(this::onDisconnect);

        getWebSocketService().onRequestCanceled.clear();
        getWebSocketService().onRequestCanceled.addListener(this::onConnectionCancelled);

        getWebSocketService().onRequestError.clear();
        getWebSocketService().onRequestError.addListener(this::onConnectionError);
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void onDisconnect(String reason) {
        GenericModal.Build("Disconnected",
            "Connection to the server has been lost: " + reason,
            skin, () -> getScreenManager().navigateTo(Screens.Splash), stage);
    }

    private void onConnectionCancelled(Void v) {
        GenericModal.Build("Request cancelled",
            "Unable to connect to server, it seems to be offline.",
            skin, null, stage);
    }

    private void onConnectionError(Throwable t) {
        GenericModal.Build("Request failed",
            "Unable to connect to server: " + t.getMessage(),
            skin, null, stage);
    }
}
