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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerReadyDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.graphics.CharacterSprite;
import com.bteam.fantasychess_client.input.FullscreenInputListener;
import com.bteam.fantasychess_client.input.MapInputProcessor;
import com.bteam.fantasychess_client.utils.GameMockStore;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.*;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * Screen on which the game plays out.
 * <p>
 * The player gets to this screen directly from the main menu.
 *
 * @author lukas adnan jacinto
 * @version 1.0
 */
public class GameScreen extends ScreenAdapter {

    private static final String DEFAULT_MAP_PATH = "maps/Map2.tmx";
    private static final int TILE_PIXEL_WIDTH = 32;
    private static final int TILE_PIXEL_HEIGHT = 16;
    private final OrthographicCamera gameCamera;
    private final ExtendViewport gameViewport;

    private final OrthographicCamera uiCamera;
    private final ExtendViewport uiViewport;
    private final Skin skin;

    private final List<CharacterSprite> characterSprites = new ArrayList<>();
    private Stage stage;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private TiledMapTileLayer startRowsLayer;
    private TiledMapTileLayer highlightLayer;
    private TiledMapTileLayer commandOptionLayer;
    private TiledMapTileLayer commandPreviewLayer;
    private TiledMapTileLayer damageLayer;

    private TileMathService mathService;

    private final Map<String,CharacterSprite> spriteMapper = new HashMap<>();

    private Vector2D focussedTile;

    private CharacterEntity selectedCharacter;
    private Vector2D[] validCommandDestinations = new Vector2D[0];

    private MapInputProcessor mapInputProcessor;



    /**
     * Constructor of GameScreen
     *
     * @param skin the projects menu skin
     */
    public GameScreen(Skin skin) {
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, 426, 240);
        gameCamera.update();

        gameViewport = new ExtendViewport(426, 240, gameCamera);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, 1920, 1080);
        uiCamera.update();
        uiViewport = new ExtendViewport(1920, 1080, uiCamera);

        this.skin = skin;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        stage = new Stage(uiViewport);

        atlas = new TextureAtlas(Gdx.files.internal("auto-generated-atlas.atlas"));
        batch = new SpriteBatch();

        // Todo: Keep in mind that this method of dimension retrieval is
        //  bound to run into issues as soon as the map also contains surrounding foliage.
        tiledMap = new TmxMapLoader().load(DEFAULT_MAP_PATH);
        mapRenderer = new IsometricTiledMapRenderer(tiledMap);

        int mapWidth = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getWidth();
        int mapHeight = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getHeight();

        mathService = new TileMathService(
            mapWidth, mapHeight, tiledMap, TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT
        );

        createFreshStartRowsLayer();
        createFreshHighlightLayer();
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();
        createFreshDamageLayer();

        mapRenderer.setView(gameCamera);

        mapInputProcessor = new MapInputProcessor(
            this,GameScreenMode.COMMAND_MODE,CommandMode.NO_SELECTION,mathService,gameCamera
        );

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(mapInputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        getWebSocketService().addPacketHandler("PLAYER_READY", str -> Main.getLogger().log(Level.SEVERE, "PLAYER_READY"));
        Gdx.app.postRunnable(() -> {
            Packet packet = new Packet(PlayerReadyDTO.ready(""), "PLAYER_READY");
            getWebSocketService().send(packet);
        });

        initializeGame();
    }

    public void initializeGame(){
        mapInputProcessor.setGameScreenMode(GameScreenMode.GAME_INIT);

        int[] startRows = new int[]{6,7,8};
        List<CharacterEntity> characters = GameMockStore.getCharacterMocks();

        Main.getGameStateService().registerNewGame(9,9);
        Main.getGameStateService().updateCharacters(characters);

        try {
            Main.getGameStateService().getGridService().setStartTiles(startRows);
            GridPlacementService.placeCharacters(Main.getGameStateService().getGridService(), characters, startRows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        showStartRows(startRows);
        createSpritesForCharacters();
    }

    private void createSpritesForCharacters() {
        for (CharacterEntity character : Main.getGameStateService().getCharacters()){
            String characterName = character.getCharacterBaseModel().getName();
            String direction = "player1".equals(character.getPlayerId()) ? "back" : "front";
            TextureRegion textureRegion = atlas.findRegion("characters/"+characterName+"/"+characterName+"-"+direction);
            CharacterSprite sprite = new CharacterSprite(textureRegion,character.getPosition(),character,mathService);
            characterSprites.add(sprite);

            spriteMapper.put(character.getId(),sprite);
        }
    }

    private void createFreshStartRowsLayer() {
        startRowsLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(),TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        startRowsLayer.setOffsetY(-1f);
        startRowsLayer.setOffsetX(1f);
        tiledMap.getLayers().add(startRowsLayer);
    }

    private void createFreshDamageLayer() {
        damageLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(),TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        damageLayer.setOffsetY(-1f);
        damageLayer.setOffsetX(1f);
        tiledMap.getLayers().add(damageLayer);
    }

    private void showStartRows(int[] startrows){

        TiledMapTileLayer.Cell startCell = new TiledMapTileLayer.Cell();
        TextureRegion region = atlas.findRegion("special_tiles/filled-green");
        startCell.setTile(new StaticTiledMapTile(region));

        for (int row : startrows){
            row = mathService.getMapHeight()-1-row;
            for (int col = 0; col < mathService.getMapWidth(); col++){
                startRowsLayer.setCell(col,row,startCell);
            }
        }
    }

    /**
     * Creates a fresh layer in the tiled map that previews the specific hovered command
     * <p>
     * The layer is added to or replaces the old in the tiled map.
     * In move mode, it highlights the tile if it's a valid move destination
     * In attack mode, it shows the area of effect if it's a valid attack target
     */
    private void createFreshCommandPreviewLayer() {
        if (commandPreviewLayer != null) {
            tiledMap.getLayers().remove(commandPreviewLayer);
        }

        commandPreviewLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        commandPreviewLayer.setOffsetY(-1f);
        commandPreviewLayer.setOffsetX(1f);
        tiledMap.getLayers().add(commandPreviewLayer);

        if (focussedTile != null && Arrays.asList(validCommandDestinations).contains(focussedTile)) {
            switch (mapInputProcessor.getCommandMode()){
                case MOVE_MODE: {
                    TiledMapTileLayer.Cell previewCell = new TiledMapTileLayer.Cell();
                    TextureRegion region = atlas.findRegion("special_tiles/filled-big-yellow-circle");
                    previewCell.setTile(new StaticTiledMapTile(region));

                    Vector2D tilePosition = gridToTiled(focussedTile);
                    commandPreviewLayer.setCell(tilePosition.getX(), tilePosition.getY(),previewCell);
                    break;
                }
                case ATTACK_MODE: {
                    Vector2D[] areaOfEffect = selectedCharacter.getCharacterBaseModel().getAttackPatterns()[0].getAreaOfEffect(selectedCharacter.getPosition(),focussedTile);

                    TiledMapTileLayer.Cell previewCell = new TiledMapTileLayer.Cell();

                    TextureRegion region = atlas.findRegion("special_tiles/filled-red");

                    previewCell.setTile(new StaticTiledMapTile(region));

                    for (Vector2D position : areaOfEffect) {
                        Vector2D tilePosition = gridToTiled(position);
                        commandPreviewLayer.setCell(tilePosition.getX(), tilePosition.getY(),previewCell);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Creates a fresh layer that highlights the current focussed tile
     * <p>
     * The layer is added to or replaces the old in the tiled map.
     * The tile that the mouse if over or is the next tile relative to the cursor is assigned a special overlay.
     */
    private void createFreshHighlightLayer() {
        if (highlightLayer != null) {
            tiledMap.getLayers().remove(highlightLayer);
        }

        highlightLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        highlightLayer.setOffsetY(-1f);
        highlightLayer.setOffsetX(1f);
        tiledMap.getLayers().add(highlightLayer);

        if (focussedTile != null) {
            TiledMapTileLayer.Cell highlightCell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/highlight");
            highlightCell.setTile(new StaticTiledMapTile(region));

            Vector2D tilePosition = gridToTiled(focussedTile);
            highlightLayer.setCell(tilePosition.getX(), tilePosition.getY(),highlightCell);
        }
    }

    /**
     * Creates a fresh layer with all valid targets of the current command mode
     * <p>
     * The layer is added to or replaces the old in the tiled map.
     */
    private void createFreshCommandOptionLayer() {
        if (commandOptionLayer != null) {
            tiledMap.getLayers().remove(commandOptionLayer);
        }
        commandOptionLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        commandOptionLayer.setOffsetY(-1f);
        commandOptionLayer.setOffsetX(1f);
        tiledMap.getLayers().add(commandOptionLayer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        gameViewport.apply();

        gameCamera.zoom = 1f; // Debug tool
        Vector2D center = mathService.getMapCenter(tiledMap);
        gameCamera.position.set(center.getX(),center.getY()+TILE_PIXEL_HEIGHT,0);
        gameCamera.update();
        mapRenderer.setView(gameCamera);

        mapRenderer.render();

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        Vector3 mouse = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2D grid = mathService.worldToGrid(mouse.x,mouse.y);

        if (!grid.equals(focussedTile)){
            focussedTile = grid;
            createFreshHighlightLayer();
            createFreshCommandPreviewLayer();
        }

        for (CharacterSprite sprite : characterSprites) {
            sprite.update(delta).draw(batch);
        }

        batch.end();

        uiViewport.apply();

        stage.act();
        stage.draw();
    }

    /**
     * Populates the {@code commandOptionLayer} with the attack options of the character
     */
    private void showAttackOptions() {
        validCommandDestinations = selectedCharacter.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(selectedCharacter.getPosition());

        for (Vector2D attackOption : validCommandDestinations) {
            Vector2D moveOptionTilePos = gridToTiled(attackOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/red-border");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(moveOptionTilePos.getX(), moveOptionTilePos.getY(),cell);
        }
    }

    /**
     * Populates the {@code commandOptionLayer} with the movement options of the character
     */
    private void showMovementOptions() {
        validCommandDestinations = selectedCharacter.getCharacterBaseModel().getMovementPatterns()[0].getPossibleTargetPositions(selectedCharacter.getPosition());

        for (Vector2D moveOption : validCommandDestinations) {
            Vector2D moveOptionTilePos = gridToTiled(moveOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/small-yellow-circle");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(moveOptionTilePos.getX(), moveOptionTilePos.getY(),cell);
        }
    }

    public void setSelectedCharacter(CharacterEntity selectedCharacter){
        this.selectedCharacter = selectedCharacter;
    }

    public CharacterEntity getSelectedChracter(){
        return selectedCharacter;
    }

    public CharacterSprite getMappedEntity(String id){
       Main.getLogger().log(Level.SEVERE,"Getting mapped entity. "+spriteMapper.get(id));
       return spriteMapper.get(id);
    }

    /**
     * Resets all layers containing command information
     */
    public void resetSelection() {
        selectedCharacter = null;
        validCommandDestinations = new Vector2D[0];
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();
    }

    /**
     * Opens a dialog for command type selection
     */
    public void openCommandTypeDialog() {
        Dialog dialog = new Dialog("Command Type",skin){
            @Override
            protected void result(Object object) {
                if ("attack".equals(object)) {
                    mapInputProcessor.setCommandMode(CommandMode.ATTACK_MODE);
                    showAttackOptions();
                } else if ("move".equals(object)) {
                    mapInputProcessor.setCommandMode(CommandMode.MOVE_MODE);
                    showMovementOptions();
                } else {
                    mapInputProcessor.setCommandMode(CommandMode.NO_SELECTION);
                    selectedCharacter = null;
                }
                Main.getLogger().log(Level.SEVERE,mapInputProcessor.getCommandMode().name());
            }
        };

        dialog.setResizable(false);
        dialog.setMovable(false);

        dialog.button("Attack!","attack");
        dialog.button("Move!","move");
        dialog.button("Cancel",null);

        dialog.pack();
        dialog.show(stage);
        dialog.setPosition((stage.getWidth() - dialog.getWidth()) / 2, (stage.getHeight() - dialog.getHeight()) / 2);
    }

    /**
     * Transforms grid coordinates into tiled map coordinates
     *
     * @param grid the grid coordinates
     * @return the tiled map coordinates
     */
    private Vector2D gridToTiled(Vector2D grid){
        return new Vector2D(grid.getX(),mathService.getMapHeight()-1-grid.getY());
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        batch.dispose();
    }
}
