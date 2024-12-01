package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.models.*;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.graphics.CharacterSprite;
import com.bteam.fantasychess_client.input.FullscreenInputListener;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Screen on which the game plays out.
 * <p>
 * The player gets to this screen directly from the main menu.
 *
 * @author lukas adnan jacinto
 * @version 1.0
 */
public class GameScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;

    private Stage stage;
    private final Skin skin;
    private TextureAtlas atlas;

    private SpriteBatch batch;

    private static final String DEFAULT_MAP_PATH = "maps/Map2.tmx";

    private static final int TILE_PIXEL_WIDTH = 32;
    private static final int TILE_PIXEL_HEIGHT = 16;

    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    TiledMapTileLayer highlightLayer;
    TiledMapTileLayer previewLayer;
    TiledMapTileLayer damageLayer;

    private TileMathService mathService;

    // Placeholder
    private final List<CharacterSprite> characterSprites = new ArrayList<>();
    private Vector2D center;

    private Vector2D focussedTile;

    public GameScreen (Skin skin){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 426,240);
        camera.update();

        extendViewport = new ExtendViewport(426,240,camera);
        extendViewport.apply();

        this.skin = skin;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.1f,.12f,.16f,1);

        stage = new Stage(extendViewport);

        atlas = new TextureAtlas(Gdx.files.internal("auto-generated-atlas.atlas"));
        batch = new SpriteBatch();

        // Todo: Keep in mind that this method of dimension retrieval is
        //  bound to run into issues as soon as the map also contains surrounding foliage.
        tiledMap = new TmxMapLoader().load(DEFAULT_MAP_PATH);
        mapRenderer = new IsometricTiledMapRenderer(tiledMap);

        int mapWidth = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getWidth();
        int mapHeight = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getHeight();

        highlightLayer = new TiledMapTileLayer(mapWidth,mapHeight,TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        tiledMap.getLayers().add(highlightLayer);
        previewLayer = new TiledMapTileLayer(mapWidth,mapHeight,TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        tiledMap.getLayers().add(previewLayer);
        damageLayer = new TiledMapTileLayer(mapWidth,mapHeight,TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        tiledMap.getLayers().add(damageLayer);

        mathService = new TileMathService(
            mapWidth, mapHeight, tiledMap, TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT
        );
        center = mathService.getMapCenter();

        mapRenderer.setView(camera);

        CharacterSprite clickBadger = new CharacterSprite(atlas.findRegion("badger/badger-front"),new Vector2D(4,4),null,mathService);
        clickBadger.setPositionInWorld(mathService.gridToWorld(4,4));
        characterSprites.add(clickBadger);

        CharacterSprite movingBadger = new CharacterSprite(atlas.findRegion("badger/badger-front"),new Vector2D(4,4),null, mathService);
        characterSprites.add(movingBadger);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT){
                    Main.getLogger().log(Level.SEVERE,"Click");
                    Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                    Vector2D grid = mathService.worldToGrid(pos.x,pos.y);
                    clickBadger.moveToGridPos(grid);
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.zoom = 1f; // Debug tool
        camera.position.set(center.getX(),center.getY()+TILE_PIXEL_HEIGHT,0);
        camera.update();
        mapRenderer.setView(camera);

        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        Vector3 mouse = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2D grid = mathService.worldToGrid(mouse.x,mouse.y);

        if (!grid.equals(focussedTile)){
            changeFocussedTile(grid);
        }

        characterSprites.get(1).setPositionInWorld(mathService.gridToWorld(grid.getX(),grid.getY()));

        for (CharacterSprite sprite : characterSprites) {
            sprite.update(delta).draw(batch);
        }

        batch.end();

        stage.act();
        stage.draw();
    }

    public void changeFocussedTile(Vector2D newFocussedPos){
        if (focussedTile == null){
            focussedTile = newFocussedPos;
            TiledMapTileLayer.Cell highlightCell = new TiledMapTileLayer.Cell();
            TextureRegion highlightRegion = atlas.findRegion("special_tiles/highlight");
            highlightCell.setTile(new StaticTiledMapTile(highlightRegion));
            highlightLayer.setCell(focussedTile.getX(), mathService.getMapHeight()-1-focussedTile.getY(),highlightCell);
        } else {
            TiledMapTileLayer.Cell highlightCell = highlightLayer.getCell(focussedTile.getX(), mathService.getMapHeight()-1-focussedTile.getY());
            highlightLayer.setCell(focussedTile.getX(), mathService.getMapHeight()-1-focussedTile.getY(),null);
            focussedTile = newFocussedPos;
            highlightLayer.setCell(focussedTile.getX(), mathService.getMapHeight()-1-focussedTile.getY(),highlightCell);
        }
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
