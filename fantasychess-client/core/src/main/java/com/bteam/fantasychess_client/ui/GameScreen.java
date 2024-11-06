package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends ScreenAdapter {

    private Stage stage;
    private Skin skin;

    public GameScreen(Skin skin) {
        this.skin = skin;
        stage = new Stage(new ScreenViewport());

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);


        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                goToMainMenu();
                return true;
            }
            return false;
        });

        table.add(backButton);
        stage.addActor(table);
    }

    private void goToMainMenu() {
        Gdx.app.postRunnable(() -> {
            ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(skin));
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
