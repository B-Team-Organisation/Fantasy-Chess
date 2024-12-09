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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerReadyDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.models.*;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.graphics.CharacterSprite;
import com.bteam.fantasychess_client.input.FullscreenInputListener;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.*;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.getWebSocketService;
import static com.bteam.fantasychess_client.ui.UserInterfaceUtil.onChange;

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
    // Placeholder
    private final List<CharacterSprite> characterSprites = new ArrayList<>();
    private Stage stage;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private TiledMapTileLayer highlightLayer;
    private TiledMapTileLayer commandOptionLayer;
    private TiledMapTileLayer commandPreviewLayer;
    private TiledMapTileLayer damageLayer;

    private TileMathService mathService;

    private final Map<CharacterEntity,CharacterSprite> entityMapper = new HashMap<>();

    private Vector2D focussedTile;

    private CharacterEntity selectedPiece;
    private Vector2D[] validCommandDestinations = new Vector2D[0];

    private GameScreenMode gameScreenMode = GameScreenMode.COMMAND_MODE;
    private enum GameScreenMode {
        TURN_OUTCOME, GAME_INIT, LOBBY, GAME_SUMMARY, COMMAND_MODE
    }
    private Vector2D center;

    private CommandMode commandMode = CommandMode.NO_SELECTION;
    private enum CommandMode {
        NO_SELECTION, ATTACK_MODE, MOVE_MODE
    }

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

    private GridService gridService;


    /**
     * Creates a mock grid model for the purpose of showing the command preview
     * <p>
     * This will be replaced as soon as the client gets characters from the server
     */
    private void createMockGrid() {
        GridModel gridModel = new GridModel(9,9);
        GridService gridService = new GridService(gridModel);

        try {
            PatternService[] movePatterns = new PatternService[1];
            movePatterns[0] = new PatternService(new PatternModel("  .  \n  .  \n.. ..\n  .  \n  .  ",null,"crossMove"),null);
            PatternService[] attackPatterns = new PatternService[1];
            Map<Character,String> subpatternMapping = new HashMap<>();
            subpatternMapping.put('+',"cross");
            attackPatterns[0] = new PatternService(new PatternModel(".+++.\n+   +\n+   +\n+   +\n.+++.", subpatternMapping, "quadCross"), new PatternStore() {
                @Override
                public PatternModel getPatternByName(String patternName) {
                    if (patternName.equals("cross")){
                        return new PatternModel(" . \n...\n . ",null,"cross");
                    }
                    return null;
                }
            });

            CharacterDataModel characterDataModel = new CharacterDataModel("Goblin","Goblin",10,10,attackPatterns,movePatterns);
            CharacterEntity character = new CharacterEntity(characterDataModel,"",10,new Vector2D(4,4),"");

            gridService.setStartTiles(new int[]{0,1,2,3,4,5,6,7,8});

            gridService.setCharacterTo(new Vector2D(4,4),character);

            TextureRegion textureRegion = atlas.findRegion("badger/badger-front");
            CharacterSprite badgerSprite = new CharacterSprite(textureRegion,new Vector2D(4,4),character,mathService);
            characterSprites.add(badgerSprite);

            entityMapper.put(character,badgerSprite);

            this.gridService = gridService;
        } catch (Exception e){
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        stage = new Stage(uiViewport);


        readyButton = new TextButton("Commands ready: \n" + validMoves + "/" + totalMoves, skin);
        readyButton.setDisabled(true);
        readyButton.setSize(400, 150);
        readyButton.setPosition(stage.getWidth() - 450, 50);


        if (validMoves == totalMoves) {
            readyButton.setDisabled(false);
        }
        onChange(readyButton,()->{
            readyButton.setText("READY");
            turnTimer.stopTime();
        });
        turnTimer = new TurnTimer(skin, 15, readyButton );
        turnTimer.setFontScale(4f);
        turnTimer.setSize(200, 100);
        turnTimer.setAlignment(Align.center);


        turnTimer.setPosition((stage.getWidth() - turnTimer.getWidth()) / 2, stage.getHeight()-100);
        stage.addActor(turnTimer);
        stage.setDebugAll(true);


        stage.addActor(readyButton);

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

        createFreshHighlightLayer();
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();

        damageLayer = new TiledMapTileLayer(mapWidth,mapHeight,TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        tiledMap.getLayers().add(damageLayer);

        createMockGrid();

        mapRenderer.setView(gameCamera);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(createMapInputProcessor()); // Add when game starts
        Gdx.input.setInputProcessor(multiplexer);

        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        getWebSocketService().addPacketHandler("PLAYER_READY", str -> Main.getLogger().log(Level.SEVERE, "PLAYER_READY"));
        Gdx.app.postRunnable(() -> {
            Packet packet = new Packet(PlayerReadyDTO.ready(""), "PLAYER_READY");
            getWebSocketService().send(packet);
        });
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
            switch (commandMode){
                case MOVE_MODE: {
                    TiledMapTileLayer.Cell previewCell = new TiledMapTileLayer.Cell();
                    TextureRegion region = atlas.findRegion("special_tiles/filled-big-yellow-circle");
                    previewCell.setTile(new StaticTiledMapTile(region));

                    Vector2D tilePosition = gridToTiled(focussedTile);
                    commandPreviewLayer.setCell(tilePosition.getX(), tilePosition.getY(),previewCell);
                    break;
                }
                case ATTACK_MODE: {
                    Vector2D[] areaOfEffect = selectedPiece.getCharacterBaseModel().getAttackPatterns()[0].getAreaOfEffect(selectedPiece.getPosition(),focussedTile);

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
     * Creates an {@link InputProcessor} that manages mouse input on the grid
     * <p>
     * Left click selects tiles or sets commands, right click resets status.
     *
     * @return the {@link InputProcessor}
     */
    private InputProcessor createMapInputProcessor(){
        return new InputProcessor() {
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
                if (button == Input.Buttons.LEFT) {
                    if (gameScreenMode != GameScreenMode.COMMAND_MODE){
                        return false;
                    }
                    Vector3 worldPos3 = gameCamera.unproject(new Vector3(screenX,screenY,0));
                    Vector2D gridPos = mathService.worldToGrid(worldPos3.x, worldPos3.y);

                    switch (commandMode){
                        case NO_SELECTION: {
                            try {
                                CharacterEntity character = gridService.getCharacterAt(gridPos);
                                selectedPiece = character;
                                if (character != null){
                                    Gdx.app.postRunnable(() -> openCommandTypeDialog());
                                }
                            } catch (DestinationInvalidException e) {
                                Main.getLogger().log(Level.SEVERE,e.getMessage());
                            }
                            break;
                        }
                        case MOVE_MODE: {
                            break;
                        }
                        case ATTACK_MODE: {
                            break;
                        }
                    }
                    return true;
                } else if (button == Input.Buttons.RIGHT){
                    resetCommandMode();
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
        };
    }


    private void updateCommandCount(){
        totalMoves = Main.getGameStateService().getFriendlyCharacterCount();
        validMoves = Main.getCommandManagementService().getAttacksCommands().size()+
            Main.getCommandManagementService().getMovementsCommands().size();
    }

    /**
     * Populates the {@code commandOptionLayer} with the attack options of the piece
     */
    private void showAttackOptions() {
        validCommandDestinations = selectedPiece.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(selectedPiece.getPosition());

        for (Vector2D attackOption : validCommandDestinations) {
            Vector2D moveOptionTilePos = gridToTiled(attackOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/red-border");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(moveOptionTilePos.getX(), moveOptionTilePos.getY(),cell);
        }
    }

    /**
     * Populates the {@code commandOptionLayer} with the movement options of the piece
     */
    private void showMovementOptions() {
        validCommandDestinations = selectedPiece.getCharacterBaseModel().getMovementPatterns()[0].getPossibleTargetPositions(selectedPiece.getPosition());

        for (Vector2D moveOption : validCommandDestinations) {
            Vector2D moveOptionTilePos = gridToTiled(moveOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/small-yellow-circle");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(moveOptionTilePos.getX(), moveOptionTilePos.getY(),cell);
        }
    }

    /**
     * Resets all layers containing command information
     */
    private void resetCommandMode() {
        commandMode = CommandMode.NO_SELECTION;
        selectedPiece = null;
        validCommandDestinations = new Vector2D[0];
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();
    }

    /**
     * Opens a dialog for command type selection
     */
    private void openCommandTypeDialog() {
        Dialog dialog = new Dialog("Command Type",skin){
            @Override
            protected void result(Object object) {
                if ("attack".equals(object)) {
                    commandMode = CommandMode.ATTACK_MODE;
                    showAttackOptions();
                } else if ("move".equals(object)) {
                    commandMode = CommandMode.MOVE_MODE;
                    showMovementOptions();
                } else {
                    commandMode = CommandMode.NO_SELECTION;
                    selectedPiece = null;
                }
                Main.getLogger().log(Level.SEVERE,commandMode.name());
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
