package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Game;
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
import com.bteam.fantasychess_client.input.FullscreenInputListener;

import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

public class SplashScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;

    private Stage stage;
    private final Skin skin;

    public SplashScreen(Skin skin){
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1920,1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080,camera);
        extendViewport.apply();

        this.skin = skin;
    }

    @Override
    public void show() {
        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(20);

        Label titleLabel = new Label("Fantasy-Chess", skin);
        titleLabel.setFontScale(10f);

        TextField usernameInput = new TextField("Username", skin);

        TextButton playButton = new TextButton("Play!", skin);
        playButton.setDisabled(true);
        onChange(playButton, () -> {
            Gdx.app.getPreferences("usersettings").putString("username", usernameInput.getText());
            Gdx.app.postRunnable(() -> ((Game)(Gdx.app.getApplicationListener())).setScreen(new MainMenu(skin)));
        });


        onChange(usernameInput,() -> {
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

        table.add(titleLabel).row();
        table.add(usernameInput).row();
        table.add(playButton);
        stage.addActor(table);

        Label versionLabel = new Label("Version 0.1", skin);
        versionLabel.setPosition(20,40, Align.bottomLeft);
        stage.addActor(versionLabel);

        Label creditLabel = new Label("Brought to you by the B-Team!", skin);
        creditLabel.setPosition(20,20,Align.bottomLeft);
        stage.addActor(creditLabel);

        TextButton fullscreenButton = new TextButton("Fullscreen", skin);
        fullscreenButton.setPosition(20,1060,Align.topLeft);
        stage.addActor(fullscreenButton);

        onChange(fullscreenButton, () -> {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
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
}
