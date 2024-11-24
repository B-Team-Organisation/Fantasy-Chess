package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen on which the game plays out.
 * <p>
 * The player gets to this screen directly from the main menu.
 *
 * @author lukas adnan
 * @version 1.0
 */
public class GameScreen extends ScreenAdapter {

    private final int TILE_PIXEL_WIDTH = 32;
    private final int TILE_PIXEL_HEIGHT = 16;
    private int mapwidth;

    private final String DEFAULT_MAP_PATH = "maps/Map2.tmx";

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;

    private Stage stage;
    private Skin skin;

    private SpriteBatch batch;

    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;

    private TextureAtlas atlas;

    // Placeholder
    private List<CharacterEntity> characters = new ArrayList<CharacterEntity>();
    private Vector2D center;

    public GameScreen (Skin skin){

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 426,240);
        camera.update();

        extendViewport = new ExtendViewport(426,240,camera);
        extendViewport.apply();

        batch = new SpriteBatch();

        // Todo: Adjust mapwidth dynamicly as soon as we let the player choose maps
        tiledMap = new TmxMapLoader().load(DEFAULT_MAP_PATH);
        mapwidth = ((TiledMapTileLayer)(tiledMap.getLayers().get(0))).getWidth();

        getMapCenter();

        mapRenderer = new IsometricTiledMapRenderer(tiledMap);

        this.skin = skin;
        atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));
    }

    private void getMapCenter() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        int mapWidthInTiles = layer.getWidth();
        int mapHeightInTiles = layer.getHeight();
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight() / 2f;
        float centerX = (mapWidthInTiles * tileWidth)/2.0f;
        float centerY = (mapHeightInTiles * tileHeight)/4.0f;
        center = new Vector2D((int)centerX,(int)centerY);
    }

    @Override
    public void show() {
        stage = new Stage(extendViewport);
        Gdx.gl.glClearColor(.1f,.12f,.16f,1);

        CharacterDataModel characterDataModel = new CharacterDataModel("boar","A dummy for testing the graphical character representation",10,10,new PatternService[0],new PatternService[0]);
        CharacterEntity character = new CharacterEntity(characterDataModel,0,new Vector2D(10,5),"");
        characters.add(character);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.zoom = 1f;
        camera.position.set(center.getX(),center.getY()+TILE_PIXEL_HEIGHT,0);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Sprite badger = new Sprite(atlas.findRegion("badger-back"));
        float x = center.getX() - badger.getWidth()/2;
        float y = center.getY() - badger.getHeight()/2;
        batch.draw(badger,x,y);
        batch.draw(badger,mouse.x - badger.getWidth()/2,mouse.y - badger.getHeight()/2);

        batch.draw(badger,x-4.5f*TILE_PIXEL_WIDTH,y);

        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        batch.dispose();
    }
}
