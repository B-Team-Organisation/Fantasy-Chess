# Game Screen

The `GameScreen` class is the main game interface where the chess-like strategy gameplay unfolds. 
Players enter this screen from the main menu to participate in matches, issue commands, and interact with the game board.

## Structure & Functionality

### 1. Initialization
When creating a `GameScreen` object, the following elements are set up:

- **Cameras and Viewports:**
    - A `gameCamera` for rendering the isometric game map.
    - A `uiCamera` for handling UI elements.
    - Extend viewports for both cameras.
    ```java
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
    ```
  - **Rendering Elements:**
      - A `SpriteBatch` for drawing game elements.
    ```java
    batch = new SpriteBatch();
    ```
  - A `TiledMap` to display the game board.
    ```java 
  tiledMap = new TmxMapLoader().load(DEFAULT_MAP_PATH);
  ``
  - `IsometricTiledMapRenderer` for rendering tiles.
    ```java
  mapRenderer = new IsometricTiledMapRenderer(tiledMap);
   ```

- **Game Logic Services:**
    - `TileMathService` for grid calculations.
  ```java
  int mapWidth = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getWidth();
        int mapHeight = ((TiledMapTileLayer) (tiledMap.getLayers().get(0))).getHeight();

        mathService = new TileMathService(
            mapWidth, mapHeight, tiledMap, TILE_PIXEL_WIDTH, TILE_PIXEL_HEIGHT
        );
  ```
    - `TurnResultAnimationHandler` for turn-based animations.
  ```java
  getLogger().log(Level.SEVERE, "Adding onApplyTurnResult listener");
        getGameStateService().onApplyTurnResult.addListener(turnResult -> {
            if (mapInputProcessor.getGameScreenMode() == GameScreenMode.WAITING_FOR_TURN_OUTCOME) {
                Main.getLogger().log(Level.SEVERE, "Starting turn outcome animation!");
                mapInputProcessor.setGameScreenMode(GameScreenMode.TURN_OUTCOME);
                Main.getLogger().log(Level.SEVERE, "Log 1");
            }
        });
  ```
    - Various input handlers to manage user interactions.

### 2. Displaying the Screen (`show()`)
When the screen is displayed, the following steps occur:

- **UI Setup:**
    - A `Stage` is created for handling UI components.
     ```java
     stage = new Stage(uiViewport);
     ```
    - A "Ready" button (`TextButton`) is placed at the bottom-right corner.
      ```java
       readyButton = createReadyButton();
        table.pad(0, 30, 30, 30);
        readyButton.setSize(200, 100);
        table.bottom().right().add(readyButton).size(200, 100);
       ```
    - Character statistics panels are added for players and opponents.

- **Game Map Setup:**
    - The `TiledMap` is loaded and rendered.
    - Layers are created for various gameplay elements, including:
        - Character positions
        - Valid move indicators
        - Attack highlights
        - Selected character highlights
- **Input Processing:**
    - `InputMultiplexer` is set up to handle keyboard, mouse, and fullscreen interactions.
- **WebSocket Event Registration:**
    - Event handlers for receiving game updates (e.g., player readiness, turn results, and lobby closure).
- **Game Initialization:**
    - The game receives its starting data and prepares the board for play.

### 3. User Interaction

#### Command Execution
- **Move Commands:** Players can move their characters to valid tiles.
- **Attack Commands:** Players can select attackable targets within range.
- **Turn Submission:** The "Ready" button submits commands to the server.
- **Turn Result Animations:** Displays the results of all players' commands.

#### UI Elements
- **Character Stats Tables:** Display information about friendly and enemy characters.
- **Game Board Highlights:** Shows movement options, attack range, and selected units.
- **Damage Indicators:** Displays damage values over characters when they are hit.

### 4. Event Handling
The game listens for WebSocket events to update the game state dynamically:

- `onDisconnect(reason)`: Notifies the user if the connection is lost.
- `onGameInit(packetJson)`: Initializes the game when a new match starts.
- `onTurnResult(packetJson)`: Processes and animates turn-based outcomes.
- `onCharacterDeath(character)`: Removes defeated characters from the board.
- `onLobbyClosed(packetJson)`: Returns players to the main menu when the match ends.


### 5. Additional Methods

#### `resize(int width, int height)`
- Adjusts the `Viewport` sizes when the game window is resized.
  ```java
  public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }
  ```

#### `render(float delta)`
- Clears the screen and updates all game elements each frame.
- Renders the game board, characters, animations, and UI components.
```java
public void reset() {
        getGameStateService().resetGame();
        animationHandler = null;
        if (!characterSprites.isEmpty()) characterSprites.clear();
        if (!spriteMapper.isEmpty()) spriteMapper.clear();
        getCommandManagementService().clearAll();
    }
```

#### `dispose()`
- Frees all allocated resources when the game screen is closed.
```java
public void dispose() {
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        batch.dispose();
    }
```
### 6. Disconnect
Disconnects and moves back to Splash Screen
```java
public void onDisconnect(String reason) {
        GenericModal.Build("Disconnected", "Connection to the server has been lost: " + reason,
            skin, () -> getScreenManager().navigateTo(Screens.Splash), stage);
    }
```

## Conclusion
The `GameScreen` class is the primary gameplay interface, integrating real-time rendering, 
turn-based command execution, and interactive game board elements. 
It manages player input, WebSocket communications, and in-game animations to provide an immersive experience.
