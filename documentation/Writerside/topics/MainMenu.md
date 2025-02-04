# Main Menu

The `MainMenu` class is the primary user interface after launching the *Fantasy-Chess* game. 
It allows players to browse, search, and join available game lobbies or create new ones.

## Structure & Functionality

### 1. Initialization
When creating a `MainMenu` object, an orthographic camera and an `ExtendViewport` with a resolution of 1920x1080 pixels are set up. 
The `Skin` resource is also passed to enable the visual design of the UI elements.
```java
public MainMenu(Skin skin) {
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
    - A username display (`Label`)
    - A search bar (`TextField`) for filtering lobbies
    - A "Refresh Lobbies" button (`TextButton`)
    - A "Create Lobby" button (`TextButton`)
    - A scrollable lobby list (`ScrollPane`)
- Setting up event listeners for user interactions
- Registering WebSocket event handlers for lobby updates
- Fetching initial lobby data from the server

### 3. User Interaction

#### Lobby Search (`TextField`)
- Default text is "Search Lobby name!".
- When the field gains focus, the placeholder is removed.
- The user can only enter letters, numbers, and a few allowed characters.
- Lobbies are dynamically filtered based on input.

#### "Refresh Lobbies" Button (`TextButton`)
- Fetches the latest list of available lobbies from the server.
```java
TextButton refreshButton = new TextButton("Refresh lobbies", skin);
        onChange(refreshButton, () -> {
            Gdx.app.postRunnable(() -> Main.getWebSocketService().send(new Packet(null, "LOBBY_ALL")));
            stage.setKeyboardFocus(null);
        });
```

#### "Create Lobby" Button (`TextButton`)
- Opens a dialog for creating a new lobby.
- Default lobby name is `{username}'s Lobby`.
- The "Create" button is only enabled if the input is at least 4 characters long.
- On confirmation, sends a lobby creation request to the server.
```java
TextButton createLobby = new TextButton("Create Lobby", skin);
        onChange(createLobby, this::createLobbyDialog);

        topContent.add(lobbyNameInput).growX().pad(10);
        topContent.add(refreshButton).pad(10);
        topContent.add(createLobby).pad(10);
```

#### Lobby List (`ScrollPane` with `Table` entries)
- Displays available lobbies in a scrollable view.
- Clicking on a lobby entry attempts to join it.
```java
ScrollPane scrollPane = new ScrollPane(centerContent, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);


        table.add(titleLabel).padBottom(20);
        table.row();
        table.add(topContent).width(1000).padBottom(20);
        table.row();
        table.add(scrollPane).width(1000).height(520).top();
        table.row();
        table.add(usernameLabel).padTop(10);
        table.row();
```

### 4. Event Handling
If the connection to the server fails or is interrupted, the game displays an appropriate message:
- `onDisconnect(reason)`: Shows a modal notifying the user that the connection was lost.
- `onLobbyInfo(packetJson)`: Updates the lobby list with data received from the server.
- `onLobbyCreated(packetJson)`: Navigates to the game screen when a lobby is successfully created.
- `onLobbyJoined(packetJson)`: Navigates to the game screen if joining a lobby succeeds, otherwise displays an error message.


### 5. Additional Methods

#### `resize(int width, int height)`
- Dynamically adjusts the `Viewport` size when the window is resized.
#### `render(float delta)`
- Updates and renders the UI elements of the screen.
#### `dispose()`
- Releases all resources when the screen is no longer needed.

## Conclusion
The `MainMenu` class serves as the primary interaction interface for players to browse, search, create, and join game lobbies. 
It integrates dynamic lobby updates and handles connectivity issues with informative popups.

