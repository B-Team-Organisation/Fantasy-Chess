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

    private final float UNIT_SCALE = 4.5f;
    private final int TILE_PIXEL_SIZE = 32;
    private int mapwidth;

    private final String DEFAULT_MAP_PATH = "maps/WaterMap1.tmx";

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
        camera.setToOrtho(false, 1920,1080);
        camera.update();

        extendViewport = new ExtendViewport(1920,1080,camera);
        extendViewport.apply();

        batch = new SpriteBatch();

        // Todo: Adjust mapwidth dynamicly as soon as we let the player choose maps
        tiledMap = new TmxMapLoader().load(DEFAULT_MAP_PATH);
        mapwidth = 37;

        getMapCenter();

        mapRenderer = new IsometricTiledMapRenderer(tiledMap,UNIT_SCALE);

        this.skin = skin;
        atlas = new TextureAtlas(Gdx.files.internal("tiles.atlas"));
    }

    private void getMapCenter() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        int mapWidthInTiles = layer.getWidth();
        int mapHeightInTiles = layer.getHeight();
        float tileWidth = layer.getTileWidth() * UNIT_SCALE;
        float tileHeight = layer.getTileHeight()/2f;
        float centerX = (mapWidthInTiles * tileWidth) / 2.0f;
        float centerY = (mapHeightInTiles * tileHeight) / 2.0f;
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
        camera.position.set(mapwidth/2f*TILE_PIXEL_SIZE*UNIT_SCALE,TILE_PIXEL_SIZE*UNIT_SCALE*1.5f,0);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        batch.draw(atlas.findRegion("boar-front"),mouse.x,mouse.y);

        Sprite boar = new Sprite(atlas.findRegion("boar-back"));
        float x = center.getX() - boar.getWidth()/2;
        float y = center.getY() - boar.getHeight()/2;
        batch.draw(boar,x,y,boar.getWidth()*2,boar.getHeight()*2);//,boar.getWidth()*UNIT_SCALE,boar.getHeight()*UNIT_SCALE);

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
    }
}
