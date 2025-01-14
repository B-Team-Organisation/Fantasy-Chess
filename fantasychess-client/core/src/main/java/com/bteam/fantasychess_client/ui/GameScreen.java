package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerStatusDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.services.TurnResult;
import com.bteam.common.models.*;
import com.bteam.common.services.TurnLogicService;
import com.bteam.common.services.TurnResult;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.data.mapper.CharacterEntityMapper;
import com.bteam.fantasychess_client.data.mapper.TurnResultMapper;
import com.bteam.fantasychess_client.graphics.CharacterSprite;
import com.bteam.fantasychess_client.graphics.CharacterStatsTable;
import com.bteam.fantasychess_client.graphics.TurnResultAnimationHandler;
import com.bteam.fantasychess_client.input.FullscreenInputListener;
import com.bteam.fantasychess_client.input.MapInputAdapter;
import com.bteam.fantasychess_client.utils.SpriteSorter;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.bteam.fantasychess_client.Main.*;
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
    public static final int TILE_PIXEL_WIDTH = 32;
    public static final int TILE_PIXEL_HEIGHT = 16;
    private final OrthographicCamera gameCamera;
    private final ExtendViewport gameViewport;

    private final OrthographicCamera uiCamera;
    private final ExtendViewport uiViewport;
    private final Skin skin;

    private List<CharacterSprite> characterSprites = new ArrayList<>();
    private final Map<String, CharacterSprite> spriteMapper = new HashMap<>();
    private final BitmapFont damageFont;
    private final Map<Vector2D, String> damagePreviewValues = new HashMap<>();
    private Stage stage;
    private TextButton readyButton;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private IsometricTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private TiledMapTileLayer startRowsLayer;
    private TiledMapTileLayer selectedCharacterLayer;
    private TiledMapTileLayer highlightLayer;
    private TiledMapTileLayer commandOptionLayer;
    private TiledMapTileLayer commandPreviewLayer;
    private TileMathService mathService;
    private Vector2D focussedTile;

    private CharacterEntity selectedCharacter;
    private Vector2D[] validCommandDestinations = new Vector2D[0];
    private MapInputAdapter mapInputProcessor;
    private TurnResultAnimationHandler animationHandler;

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

        damageFont = new BitmapFont();
        damageFont.getData().setScale(0.4f);
        damageFont.setColor(Color.WHITE);
    }

    public Vector2D[] getValidCommandDestinations() {
        return validCommandDestinations;
    }

    @Override
    public void show() {
        reset();
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        stage = new Stage(uiViewport);

        Table table = new Table();
        table.setFillParent(true);

        readyButton = createReadyButton();
        table.pad(0,30,30,30);
        readyButton.setSize(200,100);
        table.bottom().right().add(readyButton).size(200,100);

        stage.addActor(table);

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


        getLogger().log(Level.SEVERE, "Creating Layers");
        createFreshStartRowsLayer();
        createFreshSelectedCharacterLayer();
        createFreshHighlightLayer();
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();

        mapRenderer.setView(gameCamera);

        mapInputProcessor = new MapInputAdapter(
            this, GameScreenMode.LOBBY, CommandMode.NO_SELECTION, mathService, gameCamera
        );

        getLogger().log(Level.SEVERE, "Setting Input Processor");
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new FullscreenInputListener());
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(mapInputProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        getLogger().log(Level.SEVERE, "Registering PLAYER_READY packet handler");

        getLogger().log(Level.SEVERE, "Registering LOBBY_CLOSED packet handler");
        getWebSocketService().addPacketHandler("LOBBY_CLOSED", p -> Gdx.app.postRunnable(() -> {
            var content = getLobbyService().onLobbyClosed(p);
            GenericModal.Build("Lobby Closed!", content + "\nReturning to Main Menu.", skin,
                () -> getScreenManager().navigateTo(Screens.MainMenu), stage);
        }));


        getLogger().log(Level.SEVERE, "Registering GAME_INIT packet handler");
        getWebSocketService().addPacketHandler("GAME_INIT", str -> Gdx.app.postRunnable(() -> {
            getGameStateService().registerNewGame(9, 9);
            var characters = CharacterEntityMapper.fromListDTO(str);
            String gameId = new JsonReader().parse(str).get("data").getString("gameId");
            getGameStateService().setGameId(gameId);
            getGameStateService().setCharacters(characters);
            initializeGame();
        }));

        getWebSocketService().addPacketHandler("PLAYER_READY", str -> Main.getLogger().log(Level.SEVERE, "PLAYER_READY"));

        Gdx.app.postRunnable(() -> {
            Packet packet = new Packet(PlayerStatusDTO.ready(""), "PLAYER_READY");
            getWebSocketService().send(packet);
        });

        getLogger().log(Level.SEVERE, "Adding onApplyTurnResult listener");
        getGameStateService().onApplyTurnResult.addListener(turnResult -> {
            if (mapInputProcessor.getGameScreenMode() == GameScreenMode.WAITING_FOR_TURN_OUTCOME) {
                Main.getLogger().log(Level.SEVERE, "Starting turn outcome animation!");
                mapInputProcessor.setGameScreenMode(GameScreenMode.TURN_OUTCOME);
                Main.getLogger().log(Level.SEVERE, "Log 1");
            }
        });

        getGameStateService().onCharacterDeath.addListener(this::killCharacter);

        getWebSocketService().addPacketHandler("GAME_TURN_RESULT", str -> Gdx.app.postRunnable(() -> {
            Main.getLogger().log(Level.SEVERE, "Received Turn Result");
            TurnResult turnResult = TurnResultMapper.fromDTO(str);
            Main.getLogger().log(Level.SEVERE, turnResult.toString());
            Main.getGameStateService().applyTurnResult(turnResult);
        }));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (mapInputProcessor.getGameScreenMode() == GameScreenMode.TURN_OUTCOME) {
            if (animationHandler == null) {
                TurnResult turnResult = Main.getGameStateService().getTurnResult();
                if (turnResult.getValidMoves().isEmpty() &&
                    turnResult.getMovementConflicts().isEmpty() &&
                    turnResult.getValidAttacks().isEmpty()) {
                    mapInputProcessor.setGameScreenMode(GameScreenMode.COMMAND_MODE);
                    return;
                }

                animationHandler = new TurnResultAnimationHandler(turnResult, spriteMapper,tiledMap,mathService,atlas);
                animationHandler.startAnimation();
            }

            animationHandler.progressAnimation();

            if (animationHandler.isDoneWithAnimation()) {
                mapInputProcessor.setGameScreenMode(GameScreenMode.COMMAND_MODE);
                animationHandler = null;
                getGameStateService().syncCharacters(getGameStateService().getTurnResult());
            }
        }

        gameViewport.apply();

        gameCamera.zoom = 1f; // Debug tool
        Vector2D center = mathService.getMapCenter(tiledMap);
        gameCamera.position.set(center.getX(), center.getY() + TILE_PIXEL_HEIGHT, 0);
        gameCamera.update();
        mapRenderer.setView(gameCamera);

        mapRenderer.render();

        batch.setProjectionMatrix(gameCamera.combined);
        batch.enableBlending();
        batch.begin();

        Vector3 mouse = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2D grid = mathService.worldToGrid(mouse.x, mouse.y);

        if (grid != null && !grid.equals(focussedTile)) {
            focussedTile = grid;
            createFreshHighlightLayer();
            createFreshCommandPreviewLayer();
        }

        SpriteSorter.sortByY(characterSprites);
        for (CharacterSprite sprite : characterSprites) {
            sprite.update(delta).draw(batch);
        }

        if (!getCommandManagementService().getMovementsCommands().isEmpty()
            && !mapInputProcessor.getGameScreenMode().equals(GameScreenMode.GAME_INIT)) {
            batch.setColor(255, 255, 255, 0.3f);
            for (String moveId : getCommandManagementService().getMovementsCommands().keySet()) {
                MovementDataModel moveCommand = getCommandManagementService().getMovementsCommands().get(moveId);
                spriteMapper.get(moveId).drawAt(batch, mathService.gridToWorld(moveCommand.getMovementVector().getX(), moveCommand.getMovementVector().getY()));
            }
            batch.setColor(255, 255, 255, 1f);
        }

        if (damagePreviewValues.isEmpty()) {
            Map<Vector2D, Integer> finalDamageValues = new HashMap<>();
            for (Map<Vector2D, Integer> damageValues : Main.getCommandManagementService().getCommandDamageMappings().values()) {
                for (Vector2D pos : damageValues.keySet()) {
                    int dmg = damageValues.get(pos);
                    if (finalDamageValues.containsKey(pos)) {
                        dmg += finalDamageValues.get(pos);
                    }
                    finalDamageValues.put(pos, dmg);
                }
            }
            for (Vector2D pos : finalDamageValues.keySet()) {
                int dmg = finalDamageValues.get(pos);
                Vector2 worldPos = mathService.gridToWorld(pos.getX(), pos.getY());
                GlyphLayout layout = new GlyphLayout(damageFont, dmg + "");
                float textWidth = layout.width;
                float textHeight = layout.height;
                damageFont.draw(batch, layout, worldPos.x - textWidth / 2, worldPos.y + textHeight / 2);
            }
        } else {
            for (Vector2D pos : damagePreviewValues.keySet()) {
                String damageText = damagePreviewValues.get(pos);
                Vector2 worldPos = mathService.gridToWorld(pos.getX(), pos.getY());
                GlyphLayout layout = new GlyphLayout(damageFont, damageText);
                float textWidth = layout.width;
                float textHeight = layout.height;
                damageFont.draw(batch, layout, worldPos.x - textWidth / 2, worldPos.y + textHeight / 2);
            }
        }

        batch.end();
        batch.disableBlending();

        uiViewport.apply();

        stage.act();
        stage.draw();
    }

    private TextButton createReadyButton() {
        TextButton readyButton = new TextButton("", skin) {
            @Override
            public void act(float delta) {
                if (mapInputProcessor.getGameScreenMode() == GameScreenMode.COMMAND_MODE || mapInputProcessor.getGameScreenMode() == GameScreenMode.GAME_INIT) {
                    setDisabled(false);
                } else {
                    setDisabled(true);
                }

                int commandCount = 0;
                commandCount += Main.getCommandManagementService().getMovementsCommands().size();
                commandCount += Main.getCommandManagementService().getAttacksCommands().size();

                int requiredCommandCount = Main.getGameStateService().getFriendlyCharacterCount();

                if (commandCount == requiredCommandCount) {
                    setText("Send commands!");
                } else if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.COMMAND_MODE)) {
                    setText(commandCount + " of " + requiredCommandCount + "\nCommands set!");
                }
            }
        };

        onChange(readyButton, () -> {
            if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.GAME_INIT)) {
                leaveInitPhase();
            } else if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.COMMAND_MODE)) {
                mapInputProcessor.setGameScreenMode(GameScreenMode.WAITING_FOR_TURN_OUTCOME);
            }

            Main.getCommandManagementService().sendCommandsToServer();
            Main.getCommandManagementService().clearAll();

            resetSelection();

            readyButton.setDisabled(true);
            readyButton.setText("Waiting for next\nturn to start!");
        });

        return readyButton;
    }

    private void createFreshSelectedCharacterLayer() {
        if (selectedCharacterLayer != null) {
            tiledMap.getLayers().remove(selectedCharacterLayer);
        }

        selectedCharacterLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT);
        selectedCharacterLayer.setOffsetY(-1f);
        selectedCharacterLayer.setOffsetX(1f);
        tiledMap.getLayers().add(selectedCharacterLayer);

        if (selectedCharacter != null) {
            TiledMapTileLayer.Cell selectedCharacterCell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/filled-dark-green");
            selectedCharacterCell.setTile(new StaticTiledMapTile(region));

            Vector2D tilePosition = mathService.gridToTiled(selectedCharacter.getPosition());
            selectedCharacterLayer.setCell(tilePosition.getX(), tilePosition.getY(), selectedCharacterCell);
        }
    }

    /**
     * Initialises the game
     * <p>
     * Takes the game parameters and character given by the server and initialises a new game with them.
     * Puts the player in the game initialization phase, in which he can move his characters on his prefered
     * starting positions.
     */
    public void initializeGame() {
        mapInputProcessor.setGameScreenMode(GameScreenMode.GAME_INIT);
        int[] startRows = new int[]{6, 7, 8};
        try {
            Main.getGameStateService().getGridService().setStartTiles(startRows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (CharacterEntity character : getGameStateService().getFriendlyCharacters()) {
            getCommandManagementService().setCommand(new MovementDataModel(character.getId(), character.getPosition()));
        }

        showStartRows(startRows);
        createSpritesForCharacters();

        initializeStats();
    }

    public void initializeStats() {
        List<CharacterEntity> friendlyCharacters = Main.getGameStateService().getFriendlyCharacters();
        List<CharacterEntity> enemyCharacters = Main.getGameStateService().getEnemyCharacters();

        Table outerTable = new Table();
        outerTable.setFillParent(true);
        outerTable.top().left();

        if (friendlyCharacters != null && !friendlyCharacters.isEmpty()) {
            CharacterStatsTable player1StatsTable = new CharacterStatsTable("Your Characters", skin, this);
            player1StatsTable.updateContent(friendlyCharacters, "Your Characters");
            outerTable.add(player1StatsTable).align(Align.left).pad(30).expandX();
        }

        if (enemyCharacters != null && !enemyCharacters.isEmpty()) {
            CharacterStatsTable player2StatsTable = new CharacterStatsTable("Opponent's Characters", skin, this);
            player2StatsTable.updateContent(enemyCharacters, "Opponent's Characters");
            outerTable.add(player2StatsTable).align(Align.right).pad(30).expandX();
        }

        stage.addActor(outerTable);
    }


    /**
     * Transitions the game to the main phase
     */
    public void leaveInitPhase() {
        createFreshStartRowsLayer();
        mapInputProcessor.setGameScreenMode(GameScreenMode.WAITING_FOR_TURN_OUTCOME);
    }

    /**
     * Creates a {@link CharacterSprite} for every {@link CharacterEntity}
     */
    private void createSpritesForCharacters() {
        for (CharacterEntity character : Main.getGameStateService().getCharacters()) {
            String characterName = character.getCharacterBaseModel().getName();
            String direction = getWebSocketService().getUserid().equals(character.getPlayerId()) ? "back" : "front";
            TextureRegion textureRegion = atlas.findRegion("characters/" + characterName + "/" + characterName + "-" + direction);
            CharacterSprite sprite = new CharacterSprite(textureRegion, character.getPosition(), character, mathService);
            characterSprites.add(sprite);
            spriteMapper.put(character.getId(), sprite);
        }
    }

    /**
     * Kill a character
     * @param character
     */

    private void killCharacter(CharacterEntity character) {
        deleteSpriteForCharacter(character.getId());
    }

    /**
     * Removes a Sprite for a given Character
     */
    private void deleteSpriteForCharacter(String id){
        var sprite = spriteMapper.remove(id);
        if (sprite != null) {
            characterSprites.remove(sprite);
        }
    }

    /**
     * Creates a fresh {@link TiledMapTileLayer} for displaying the start rows
     */
    private void createFreshStartRowsLayer() {
        if (startRowsLayer != null) {
            tiledMap.getLayers().remove(startRowsLayer);
        }

        startRowsLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT);
        startRowsLayer.setOffsetY(-1f);
        startRowsLayer.setOffsetX(1f);
        tiledMap.getLayers().add(startRowsLayer);
    }

    /**
     * Displays all start rows on the map
     */
    private void showStartRows(int[] startrows) {

        TiledMapTileLayer.Cell startCell = new TiledMapTileLayer.Cell();
        TextureRegion region = atlas.findRegion("special_tiles/filled-green");
        startCell.setTile(new StaticTiledMapTile(region));

        for (int row : startrows) {
            row = mathService.getMapHeight() - 1 - row;
            for (int col = 0; col < mathService.getMapWidth(); col++) {
                startRowsLayer.setCell(col, row, startCell);
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

        commandPreviewLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT);
        commandPreviewLayer.setOffsetY(-1f);
        commandPreviewLayer.setOffsetX(1f);
        tiledMap.getLayers().add(commandPreviewLayer);

        if (focussedTile != null && Arrays.asList(validCommandDestinations).contains(focussedTile)) {
            switch (mapInputProcessor.getCommandMode()) {
                case MOVE_MODE: {
                    TiledMapTileLayer.Cell previewCell = new TiledMapTileLayer.Cell();
                    TextureRegion region = atlas.findRegion("special_tiles/filled-big-yellow-circle");
                    previewCell.setTile(new StaticTiledMapTile(region));

                    Vector2D tilePosition = mathService.gridToTiled(focussedTile);
                    commandPreviewLayer.setCell(tilePosition.getX(), tilePosition.getY(), previewCell);
                    break;
                }
                case ATTACK_MODE: {
                    Vector2D[] areaOfEffect = selectedCharacter.getCharacterBaseModel().getAttackPatterns()[0].getAreaOfEffect(selectedCharacter.getPosition(), focussedTile);

                    TiledMapTileLayer.Cell previewCell = new TiledMapTileLayer.Cell();

                    TextureRegion region = atlas.findRegion("special_tiles/filled-red");

                    previewCell.setTile(new StaticTiledMapTile(region));

                    damagePreviewValues.clear();
                    for (Vector2D position : areaOfEffect) {
                        Vector2D tilePosition = mathService.gridToTiled(position);
                        commandPreviewLayer.setCell(tilePosition.getX(), tilePosition.getY(), previewCell);

                        damagePreviewValues.put(position, selectedCharacter.getCharacterBaseModel().getAttackPower() + "");
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

        highlightLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT);
        highlightLayer.setOffsetY(-1f);
        highlightLayer.setOffsetX(1f);
        tiledMap.getLayers().add(highlightLayer);

        if (focussedTile != null) {
            TiledMapTileLayer.Cell highlightCell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/highlight");
            highlightCell.setTile(new StaticTiledMapTile(region));

            Vector2D tilePosition = mathService.gridToTiled(focussedTile);
            highlightLayer.setCell(tilePosition.getX(), tilePosition.getY(), highlightCell);
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

        commandOptionLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(), TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT);
        commandOptionLayer.setOffsetY(-1f);
        commandOptionLayer.setOffsetX(1f);
        tiledMap.getLayers().add(commandOptionLayer);
    }

    /**
     * Populates the {@code commandOptionLayer} with the attack options of the piece
     */
    private void showAttackOptions() {
        validCommandDestinations = selectedCharacter.getCharacterBaseModel().getAttackPatterns()[0].getPossibleTargetPositions(selectedCharacter.getPosition());

        for (Vector2D attackOption : validCommandDestinations) {
            Vector2D attackOptionTilePos = mathService.gridToTiled(attackOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/red-border");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(attackOptionTilePos.getX(), attackOptionTilePos.getY(), cell);
        }
    }

    /**
     * Populates the {@code commandOptionLayer} with the movement options of the character
     */
    private void showMovementOptions() {
        validCommandDestinations = selectedCharacter.getCharacterBaseModel().getMovementPatterns()[0].getPossibleTargetPositions(selectedCharacter.getPosition());

        for (Vector2D moveOption : validCommandDestinations) {
            Vector2D moveOptionTilePos = mathService.gridToTiled(moveOption);

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/small-yellow-circle");
            cell.setTile(new StaticTiledMapTile(region));
            commandOptionLayer.setCell(moveOptionTilePos.getX(), moveOptionTilePos.getY(), cell);
        }
    }

    /**
     * Updates the selected character in the scene.
     * <p>
     * Acts as a setter while also marking the character on the board.
     */
    public void updateSelectedCharacter(CharacterEntity selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
        createFreshSelectedCharacterLayer();
    }

    /**
     * Getter for the selected {@link CharacterEntity}
     *
     * @return the selected character
     */
    public CharacterEntity getSelectedCharacter() {
        return selectedCharacter;
    }

    /**
     * Returns the {@link CharacterSprite} representing the {@link CharacterEntity}
     *
     * @param id the id of the {@link CharacterEntity}
     * @return the corresponding {@link CharacterSprite}
     */
    public CharacterSprite getMappedSprite(String id) {
        return spriteMapper.get(id);
    }

    /**
     * Resets all layers containing command information
     */
    public void resetSelection() {
        updateSelectedCharacter(null);
        validCommandDestinations = new Vector2D[0];
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();
        damagePreviewValues.clear();
    }

    /**
     * Opens a dialog for command type selection
     */
    public void openCommandTypeDialog() {
        Dialog dialog = new Dialog("Command Type", skin) {
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
                Main.getLogger().log(Level.SEVERE, mapInputProcessor.getCommandMode().name());
            }
        };

        dialog.setResizable(false);
        dialog.setMovable(false);

        dialog.button("Attack!", "attack");
        dialog.button("Move!", "move");
        dialog.button("Cancel", null);

        dialog.pack();
        dialog.show(stage);
        dialog.setPosition((stage.getWidth() - dialog.getWidth()) / 2, (stage.getHeight() - dialog.getHeight()) / 2);
    }

    /**
     * opens a dialog to display the Winner of the Game
     *
     * @param winnerName represents the winner of the Game, if null or empty the game finished in a draw
     */
    public void showEndGameDialog(String winnerName) {

        String endMessage;

        if (winnerName.isEmpty() || winnerName == null) {
            endMessage = "Result: DRAW";
        } else {
            endMessage = "Result:" + winnerName;
        }


        Dialog endGameDialog = new Dialog("Game Over", skin) {
            @Override
            protected void result(Object object) {
                if ((boolean) object) {
                    Main.getScreenManager().navigateTo(Screens.MainMenu);
                }
            }
        };

        endGameDialog.text(endMessage, skin.get("default", Label.LabelStyle.class));
        endGameDialog.button("Back to Main Menu", true).align(Align.center);

        endGameDialog.setSize(400, 200);
        endGameDialog.setPosition((stage.getWidth() - endGameDialog.getWidth()) / 2, (stage.getHeight() - endGameDialog.getHeight()) / 2);

        endGameDialog.show(stage);
    }

    /**
     * Creates and opens a menu
     * <p>
     * Gives the player the possibility to exit a match and go back to the main menu
     */
    public void openEscapeMenu() {
        new EscapeMenu(skin).show(stage);
    }

    public void reset() {
        getGameStateService().resetGame();
        animationHandler = null;
        if (!characterSprites.isEmpty()) characterSprites.clear();
        if (!spriteMapper.isEmpty()) spriteMapper.clear();
        getCommandManagementService().clearAll();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width,height,true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        batch.dispose();
    }

    public Stage getStage() {
        return stage;
    }

}
