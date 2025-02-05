# Splash Screen

`Author: Lukas Walker`

The Splash Screen is the first screen seen by the user when loading the game. It prompts the user to
enter his username and press the play button.

Additionally, this screen provides information about the team behind the game and its current version in the buttom
left corner.

## Username Input

The username input field displayes "Username" until clicked to communicate the user what to write into it.
Clicking on it allows the user to type both letters and numbers into it. All other symbols will be declined.

All usernames are required to be at least 4 Characters long.

When a valid username is set, the play button comes active. If the username later fails its requirements, it gets
disabled again.

Clicking the `play button` prompts the client to save the username and connect with the server.

## Register and Connect

The client tries to connect to the server using its [](Networking.md#websocket-service).

````Java
Gdx.app.postRunnable(() -> getWebSocketService()
.registerAndConnect(usernameInput.getText()));
getWebSocketService().getClient().onOpenEvent.addListener(s -> {
    Main.getScreenManager().navigateTo(Screens.MainMenu);
});
````

See [](Networking.md#auth) for more information.