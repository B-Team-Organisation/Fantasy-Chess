package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

/** First screen of the application. Displayed after the application is created. */
public class MainMenu extends ScreenAdapter {

    private final OrthographicCamera camera;

    private final ExtendViewport extendViewport;

    private Stage stage;
    private Skin skin;

    public MainMenu(Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }
    @Override
    public void show() {
        stage = new Stage(extendViewport);
        //stage.setDebugAll(true);
        //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.gl.glClearColor(.1f,.12f,.16f,1);

        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("FantasyChess", skin);
        titleLabel.setFontScale(4f);

        Table topContent = new Table();
        topContent.setBackground(skin.getDrawable("round-dark-gray"));

        Label usernameLabel = new Label("Username", skin);
        TextField usernameInput = new TextField("", skin);
        TextButton refreshButton = new TextButton("Refresh lobbies!", skin);

        onChange(refreshButton,() -> {
            System.out.println("I want to refresh lobbies");
        });

        topContent.add(usernameLabel);
        topContent.add(usernameInput);
        topContent.add(refreshButton);

        Table centerContent = new Table();
        centerContent.setBackground(skin.getDrawable("round-dark-gray"));
        //centerContent.setSize(1000,600);

        Table bottomContent = new Table();

        table.add(titleLabel);
        table.row();
        table.add(topContent);
        table.row();
        table.add(centerContent).width(1000).height(520);
        table.row();
        table.add(bottomContent);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        extendViewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        // Destroy screen's assets here.
    }


}
