package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
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

    private SpriteBatch batch;

    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;

    private TextureAtlas atlas;

    public GameScreen (Skin skin){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920,1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080,camera);
        extendViewport.apply();

        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load("maps/Map3.tmx");
        mapRenderer = new IsometricTiledMapRenderer(tiledMap);

        this.skin = skin;
        atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));
    }

    @Override
    public void show() {
        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f,.12f,.16f,1);

        mapRenderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

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
        atlas.dispose();
    }
}
