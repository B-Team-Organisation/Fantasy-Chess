package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerReadyDTO;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.graphics.CharacterSprite;
import com.bteam.fantasychess_client.input.FullscreenInputListener;
import com.bteam.fantasychess_client.input.MapInputAdapter;
import com.bteam.fantasychess_client.utils.GameMockStore;
import com.bteam.fantasychess_client.utils.SpriteSorter;
import com.bteam.fantasychess_client.utils.TileMathService;
import com.badlogic.gdx.utils.Timer;

import java.util.*;
import java.util.List;
import java.util.logging.Level;

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
    private static final int TILE_PIXEL_WIDTH = 32;
    private static final int TILE_PIXEL_HEIGHT = 16;
    private final OrthographicCamera gameCamera;
    private final ExtendViewport gameViewport;

    private final OrthographicCamera uiCamera;
    private final ExtendViewport uiViewport;
    private final Skin skin;

    private final List<CharacterSprite> characterSprites = new ArrayList<>();

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

    private final Map<String,CharacterSprite> spriteMapper = new HashMap<>();

    private Vector2D focussedTile;

    private CharacterEntity selectedCharacter;
    private Vector2D[] validCommandDestinations = new Vector2D[0];

    public Vector2D[] getValidCommandDestinations(){
        return validCommandDestinations;
    }

    private MapInputAdapter mapInputProcessor;

    private Table player1SideBar;
    private Table player2SideBar;


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

        readyButton = createReadyButton();
        readyButton.setPosition(stage.getWidth()-250,50);
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

        createFreshStartRowsLayer();
        createFreshSelectedCharacterLayer();
        createFreshHighlightLayer();
        createFreshCommandOptionLayer();
        createFreshCommandPreviewLayer();

        mapRenderer.setView(gameCamera);

        mapInputProcessor = new MapInputAdapter(
            this,GameScreenMode.GAME_INIT,CommandMode.NO_SELECTION,mathService,gameCamera
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

        player1SideBar = new Table();
        ScrollPane player1ScrollPane = new ScrollPane(player1SideBar, skin);
        player1ScrollPane.setScrollingDisabled(true, false);
        player1ScrollPane.setSize(400, 400);
        player1ScrollPane.setPosition(50, stage.getHeight() - 450);

        player2SideBar = new Table();
        ScrollPane player2ScrollPane = new ScrollPane(player2SideBar, skin);
        player2ScrollPane.setScrollingDisabled(true, false);
        player2ScrollPane.setSize(400, 400);
        player2ScrollPane.setPosition(stage.getWidth() - 450, stage.getHeight() - 450);

        stage.addActor(player1ScrollPane);
        stage.addActor(player2ScrollPane);

        initializeGame();
        updateSidebars();




    }

    private TextButton createReadyButton() {
        TextButton readyButton = new TextButton("",skin){
            @Override
            public void act(float delta) {
                int commandCount = 0;
                commandCount += Main.getCommandManagementService().getMovementsCommands().size();
                commandCount += Main.getCommandManagementService().getAttacksCommands().size();

                int requiredCommandCount = Main.getGameStateService().getFriendlyCharacterCount();

                if (commandCount == requiredCommandCount) {
                    setText("Send commands!");
                    setDisabled(false);
                } else if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.COMMAND_MODE)){
                    setText(commandCount + " of " + requiredCommandCount + "\nCommands set!");
                    setDisabled(true);
                }
            }
        };

        onChange(readyButton,()->{
            if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.GAME_INIT)){
                leaveInitPhase();
            } else if (mapInputProcessor.getGameScreenMode().equals(GameScreenMode.COMMAND_MODE)) {
                mapInputProcessor.setGameScreenMode(GameScreenMode.WAITING_FOR_TURN_OUTCOME);
            }

            Main.getCommandManagementService().sendCommandsToServer();
            Main.getCommandManagementService().clearAll();

            readyButton.setDisabled(true);
            readyButton.setText("Waiting for next\nturn to start!");
        });

        readyButton.setSize(200,100);

        return readyButton;
    }

    private void createFreshSelectedCharacterLayer() {
        if (selectedCharacterLayer != null) {
            tiledMap.getLayers().remove(selectedCharacterLayer);
        }

        selectedCharacterLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(),TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        selectedCharacterLayer.setOffsetY(-1f);
        selectedCharacterLayer.setOffsetX(1f);
        tiledMap.getLayers().add(selectedCharacterLayer);

        if (selectedCharacter != null) {
            TiledMapTileLayer.Cell selectedCharacterCell = new TiledMapTileLayer.Cell();
            TextureRegion region = atlas.findRegion("special_tiles/filled-dark-green");
            selectedCharacterCell.setTile(new StaticTiledMapTile(region));

            Vector2D tilePosition = gridToTiled(selectedCharacter.getPosition());
            selectedCharacterLayer.setCell(tilePosition.getX(), tilePosition.getY(),selectedCharacterCell);
        }
    }


    /**
     * Initialises the game
     * <p>
     * Takes the game parameters and character given by the server and initialises a new game with them.
     * Puts the player in the game initialization phase, in which he can move his characters on his prefered
     * starting positions.
     */
    public void initializeGame(){
        mapInputProcessor.setGameScreenMode(GameScreenMode.GAME_INIT);

        List<CharacterEntity> characters = GameMockStore.getCharacterMocks();

        Main.getGameStateService().registerNewGame(9,9);
        Main.getGameStateService().updateCharacters(characters);

        int[] startRows = new int[]{6,7,8};
        try {
            Main.getGameStateService().getGridService().setStartTiles(startRows);
            GridPlacementService.placeCharacters(Main.getGameStateService().getGridService(), characters, startRows);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (CharacterEntity character : characters){
            getCommandManagementService().setCommand(new MovementDataModel(character.getId(),character.getPosition()));
        }

        showStartRows(startRows);
        createSpritesForCharacters();

        updateSidebars();
    }

    /**
     * Transitions the game to the main phase
     */
    public void leaveInitPhase(){
        Main.getCommandManagementService().sendCommandsToServer();
        Main.getCommandManagementService().clearAll();

        createFreshStartRowsLayer();
        mapInputProcessor.setGameScreenMode(GameScreenMode.COMMAND_MODE);
    }

    /**
     * Creates a {@link CharacterSprite} for every {@link CharacterEntity}
     */
    private void createSpritesForCharacters() {
        for (CharacterEntity character : Main.getGameStateService().getCharacters()){
            String characterName = character.getCharacterBaseModel().getName();
            String direction = getWebSocketService().getUserid().equals(character.getPlayerId()) ? "back" : "front";
            TextureRegion textureRegion = atlas.findRegion("characters/"+characterName+"/"+characterName+"-"+direction);
            CharacterSprite sprite = new CharacterSprite(textureRegion,character.getPosition(),character,mathService);
            characterSprites.add(sprite);

            spriteMapper.put(character.getId(),sprite);
        }
    }

    /**
     * Creates a fresh {@link TiledMapTileLayer} for displaying the start rows
     */
    private void createFreshStartRowsLayer() {
        if (startRowsLayer != null) {
            tiledMap.getLayers().remove(startRowsLayer);
        }

        startRowsLayer = new TiledMapTileLayer(mathService.getMapWidth(), mathService.getMapHeight(),TILE_PIXEL_WIDTH,TILE_PIXEL_HEIGHT);
        startRowsLayer.setOffsetY(-1f);
        startRowsLayer.setOffsetX(1f);
        tiledMap.getLayers().add(startRowsLayer);
    }

    /**
     * Displays all start rows on the map
     */
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
        batch.enableBlending();
        batch.begin();

        Vector3 mouse = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2D grid = mathService.worldToGrid(mouse.x,mouse.y);

        if (!grid.equals(focussedTile)){
            focussedTile = grid;
            createFreshHighlightLayer();
            createFreshCommandPreviewLayer();
        }

        SpriteSorter.sortByY(characterSprites);
        for (CharacterSprite sprite : characterSprites) {
            sprite.update(delta).draw(batch);
        }

        if (!getCommandManagementService().getMovementsCommands().isEmpty()
             && !mapInputProcessor.getGameScreenMode().equals(GameScreenMode.GAME_INIT)){
            batch.setColor(255,255,255,0.3f);
            for (String moveId : getCommandManagementService().getMovementsCommands().keySet()) {
                MovementDataModel moveCommand = getCommandManagementService().getMovementsCommands().get(moveId);
                spriteMapper.get(moveId).drawAt(batch,mathService.gridToWorld(moveCommand.getMovementVector().getX(),moveCommand.getMovementVector().getY()));
            }
            batch.setColor(255,255,255,1f);
        }

        batch.end();
        batch.disableBlending();

        uiViewport.apply();

        stage.act();
        stage.draw();
    }

    /**
     * Populates the {@code commandOptionLayer} with the attack options of the piece
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

    /**
     * Updates the selected character in the scene.
     * <p>
     * Acts as a setter while also marking the character on the board.
     */
    public void updateSelectedCharacter(CharacterEntity selectedCharacter){
        this.selectedCharacter = selectedCharacter;
        createFreshSelectedCharacterLayer();
    }


    /**
     * Updates the content of the player and opponent sidebars.
     * <p>
     * This method shows in real time the actual Characters of Player 1 and Player2.
     * Player1 is shown on the left-side as "Your character" and Player2 as "Opponent's Characters".
     * Each character's statistics, including name and health,
     * are displayed using the {@link #createCharacterStat(CharacterEntity)} method.
     */
    private void updateSidebars() {
        player1SideBar.clearChildren();
        player2SideBar.clearChildren();

        player1SideBar.top();
        Label header1 = new Label("Your Characters", skin, "default");
        header1.setColor(Color.WHITE);
        header1.setFontScale(1.5f);
        player1SideBar.add(header1).padTop(10).row();

        player2SideBar.top();
        Label header2 = new Label("Opponent Characters", skin, "default");
        header2.setColor(Color.WHITE);
        header2.setFontScale(1.5f);
        player2SideBar.add(header2).padTop(10).row();

        Table player1CharactersTable = new Table();
        for (CharacterEntity character : Main.getGameStateService().getFriendlyCharacters()) {
            player1CharactersTable.add(createCharacterStat(character)).padBottom(10).row();
        }
        player1SideBar.add(player1CharactersTable).expandX().fillX();

        Table player2CharactersTable = new Table();
        for (CharacterEntity character : Main.getGameStateService().getEnemyCharacters()) {
            player2CharactersTable.add(createCharacterStat(character)).padBottom(10).row();
        }
        player2SideBar.add(player2CharactersTable).expandX().fillX();
    }



    /**
     * Creates a table row displaying the statistics of a given character.
     * <p>
     * This method constructs a table containing the character's name and health status.
     * The row also supports a callback to update the currently selected character when clicked.
     *
     * @param character The {@link CharacterEntity} whose statistics will be displayed.
     * @return A {@link Table} representing a row with the character's name and health information.
     */
    private Table createCharacterStat(CharacterEntity character) {
        Table row = new Table();
        row.setBackground(skin.newDrawable("white", Color.LIGHT_GRAY));

        Table nameContainer = new Table();
        nameContainer.setBackground(skin.newDrawable("white", Color.DARK_GRAY));
        Label nameLabel = new Label(character.getCharacterBaseModel().getName(), skin);
        nameLabel.setColor(Color.WHITE);
        nameContainer.add(nameLabel).pad(5).expandX().fillX();

        float healthPercentage = character.getHealth() / (float) character.getCharacterBaseModel().getHealth();
        Table healthBarContainer = new Table();
        ProgressBar healthBar = createHealthBar(healthPercentage);

        var healthText = character.getHealth() + " / " + character.getCharacterBaseModel().getHealth() + " HP";
        Label healthLabel = new Label(healthText, skin);
        healthLabel.setColor(Color.WHITE);

        healthBarContainer.add(healthLabel).expandX().center().padBottom(2).row();
        healthBarContainer.add(healthBar).width(200).height(25).row();
        row.add(nameContainer).width(150).height(50).pad(5);
        row.add(healthBarContainer).width(200).height(50).pad(5);

        onChange(row,()->{
            updateSelectedCharacter(character);
        });

        return row;
    }


    /**
     * Creates a progress bar representing the character's health status.
     * <p>
     *
     * @param healthPercentage A float value between 0.0 and 1.0 representing the percentage of health.
     * @return A {@link ProgressBar} configured to visually represent the health percentage.
     */
    private ProgressBar createHealthBar(float healthPercentage) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

        style.background = skin.newDrawable("white", Color.DARK_GRAY);
        style.knobBefore = skin.newDrawable("white", getHealthColor(healthPercentage));
        style.background.setMinWidth(300);
        style.background.setMinHeight(30);

        style.knobBefore = skin.newDrawable("white", getHealthColor(healthPercentage));
        style.knobBefore.setMinWidth(300);
        style.knobBefore.setMinHeight(30);

        style.knob = null;

        ProgressBar healthBar = new ProgressBar(0, 1, 0.01f, false, style);
        healthBar.setValue(healthPercentage);
        healthBar.setSize(300, 50);
        healthBar.invalidate();

        return healthBar;
    }


    /**
     * Displays a rudimentary gradient for the Colours shown in the Health-sidebar
     *
     * @param healthPercentage percentage of health left to a specific Character
     * @return the color to display in the health-sidebar
     */

    private Color getHealthColor(float healthPercentage) {
        if (healthPercentage > 0.7f) return Color.GREEN;
        if (healthPercentage > 0.4f) return Color.YELLOW;
        if (healthPercentage > 0.2f) return Color.ORANGE;
        return Color.RED;
    }
    public void showAttackEffect(CharacterEntity character, int damage) {

        CharacterSprite characterSprite = spriteMapper.get(character.getId());
        if (characterSprite != null) {
            characterSprite.setColor(new Color(1f, 0.2f, 0.2f, 1f)); // Luminanz-Rot

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    characterSprite.setColor(Color.WHITE);
                }
            }, 0.5f);
        }

        Label damageLabel = new Label("-" + damage, skin);
        damageLabel.setColor(Color.RED);
        damageLabel.setFontScale(1.5f);

        Vector2 worldPosition = mathService.gridToWorld(character.getPosition().getX(), character.getPosition().getY());
        float labelX = worldPosition.x;
        float labelY = worldPosition.y + 20;

        damageLabel.setPosition(labelX, labelY);


        stage.addActor(damageLabel);
        damageLabel.addAction(Actions.sequence(
            Actions.moveBy(0, 50, 1f),
            Actions.fadeOut(0.5f),
            Actions.run(damageLabel::remove)
        ));
    }



    /**
     * Getter for the selected {@link CharacterEntity}
     * @return the selected character
     */
    public CharacterEntity getSelectedCharacter(){
        return selectedCharacter;
    }

    /**
     * Returns the {@link CharacterSprite} representing the {@link CharacterEntity}
     *
     * @param id the id of the {@link CharacterEntity}
     * @return the corresponding {@link CharacterSprite}
     */
    public CharacterSprite getMappedSprite(String id){
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
