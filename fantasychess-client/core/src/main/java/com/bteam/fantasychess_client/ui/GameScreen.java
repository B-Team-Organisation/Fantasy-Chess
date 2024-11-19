package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Screen on which the game plays out.
 * <p>
 * The player gets to this screen directly from the main menu.
 *
 * @author lukas
 * @version 1.0
 */
public class GameScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;

    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;

    public GameScreen (Skin skin){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920,1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080,camera);
        extendViewport.apply();

        this.skin = skin;
        atlas = new TextureAtlas(Gdx.files.internal("tileset.atlas"));
    }

    @Override
    public void show() {
        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f,.12f,.16f,1);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
