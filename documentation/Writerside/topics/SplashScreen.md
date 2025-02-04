# Splash Screen

The `SplashScreen` class is the first user interface of the *Fantasy-Chess* game. 
It serves an the entry point to the game, allowing users to enter a username and start playing.
The goal is to provide a clear, intuitive user experience and sets the visual style of the game.

// Splash Screen-Screenshot

## Structure & Functionality

### 1. Initialization
When creating a `SplashScreen` object, an orthographic camera and an `ExtendViewport` with a resolution of 1920x1080 pixels are set up. 
The `Skin` resource is also passed to enable the visual design of the UI elements.
```java
public SplashScreen(Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920, 1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }
```

### 2. Displaying the Screen (`show()`)
When the screen is displayed, the following steps are performed:
- Creation of a new `Stage` for UI elements.
- Setting the background color.
- Building a `Table` as the central layout element.
- Adding UI widgets:
    - A title (`Label`)
    - A username input field (`TextField`)
    - A "Play!" button (`TextButton`), initially disabled until a valid username is entered

- Checking user input and enabling the "Play!" button
- Storing the username in local settings and attempting to connect to the server
- Displaying additional UI elements such as version information and a fullscreen button
- Setting the `InputMultiplexer` to handle input processing
- Registering event listeners for WebSocket connection events

```java
public void show() {
        // Creation of a new `Stage` for UI elements.
        stage = new Stage(extendViewport);
        // Setting the background color.
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);

        // Main table as the central layout element
        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(20);
        
        // Adding UI widgets:
        // 1. Title Label
        Label titleLabel = new Label("Fantasy-Chess", skin, "title");
        titleLabel.setFontScale(1f);
        // 2. Username Input field
        TextField usernameInput = new TextField("Username", skin);
        // 3. A "Play!" button, initially disabled until a valid username is entered
        TextButton playButton = new TextButton("Play!", skin);
        playButton.setDisabled(true);

        // Checking user input and enabling the "Play!" button when correct
        onChange(usernameInput, () -> {
            playButton.setDisabled(usernameInput.getText().isEmpty() || usernameInput.getText().length() > 4);
        });

        // Storing the username in local settings and attempting to connect to the server
        onChange(usernameInput, () -> {
            playButton.setDisabled(usernameInput.getText().length() < 4);
        });
        usernameInput.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (focused) {
                    if (usernameInput.getText().equals("Username")) {
                        usernameInput.setText("");
                    }
                }
            }
        });
```
<!-- Klasse zu lang/kurz ? -->


### 3. 
The Splash Screen gives information about the games developers and its version.
```java
        // Version label
        Label versionLabel = new Label("Version 0.1", skin);
        versionLabel.setPosition(20, 40, Align.bottomLeft);
        stage.addActor(versionLabel);

        // Credit label
        Label creditLabel = new Label("Brought to you by the B-Team!", skin);
        creditLabel.setPosition(20, 20, Align.bottomLeft);
        stage.addActor(creditLabel);
```

You start the game with the play button:
```java
        onChange(playButton, () -> {
            Gdx.app.getPreferences("userinfo").putString("username", usernameInput.getText());
            Gdx.app.getPreferences("userinfo").flush();
            Gdx.app.postRunnable(() -> getWebSocketService().registerAndConnect(usernameInput.getText()));
            getWebSocketService().getClient().onOpenEvent.addListener(s -> {
                Main.getScreenManager().navigateTo(Screens.MainMenu);
            });
        });
```

It holds the option to continue the game in fullscreen.
```java
TextButton fullscreenButton = new TextButton("Fullscreen", skin);
        fullscreenTable.add(fullscreenButton).pad(20);
        stage.addActor(fullscreenTable);
        onChange(fullscreenButton, () -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()));
```
### 4. Event Handling
If the connection to the server fails or is interrupted, the game displays an appropriate message:

- `onDisconnect(reason)`: Shows a modal notifying the user that the connection was lost.
- `onConnectionCancelled(Void v)`: Shows a modal if the server is unreachable.
- `onConnectionError(Throwable t)`: Displays a modal with the error message if an unexpected error occurs.

### 5. Additional Methods

#### `resize(int width, int height)`
- Dynamically adjusts the `Viewport` size when the window is resized.

#### `render(float delta)`
- Updates and renders the UI elements of the screen.

#### `dispose()`
- Releases all resources when the screen is no longer needed.

## Conclusion
The `SplashScreen` class serves as the first interaction interface of the game and ensures that the player enters a username before proceeding.
It integrates basic UI elements and handles connection issues with informative popups.