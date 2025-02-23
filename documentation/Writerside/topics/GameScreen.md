# Game Screen

`Author: Lukas Walker`

![](../img/client/GameScreenCommand.png)

The Game Screen is the main screen of the game. All gameplay happens here.

See [](Screens.md) for general screen implementation methods and details.

## Creation

The following things happen when the game screen gets created (combination of constructor and show methods):

- Creation of cameras and viewports for rendering
- Creating of font for displaying damage numbers
- Creation of important ui elements like the [readyButton](GameScreen.md#ready-button)
- Creation of the 
[atlas](https://libgdx.com/wiki/tools/texture-packer) for texture retrieval and 
[batch](https://libgdx.com/wiki/graphics/2d/spritebatch-textureregions-and-sprites) for sprite rendering
- Import of the tiled tmx map used for rendering the map + creation of its renderer
- Creation of the `TiledMathService` for all calculations. See [](Tilemap.md)
- Creation of custom map layers. See [](GameScreen.md#other-layers)
- Registration of input processors. See [](MapInputAdapter.md)
- Registration of event and packet handlers used for game state networking. See [](Networking.md#websocket-service)

The two stat panels are created later on in the [](GameScreen.md#game-initialisation). See [](GameScreen.md#stat-panels)
for information about them.

## Inputs

The inputs in this screen are processed using a `InputMultiplexer` that stacks multiple input processors on top of each 
other. The processors receive the InputEvent in the order they were added and if a processor returns `true`, the Event
is regarded as handled.

````Java
InputMultiplexer multiplexer = new InputMultiplexer();
multiplexer.addProcessor(new FullscreenInputListener());
multiplexer.addProcessor(stage);
multiplexer.addProcessor(mapInputProcessor);
````

See [](Input.md) about general information about input processing.
The mapInputProcessor contains the logic for interacting with the playing field itself.
See [](MapInputAdapter.md) or its [javadoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/client/core/com/bteam/fantasychess_client/input/MapInputAdapter.html) for more information

## Game Phases

The entire time a players game shows the Game Screen, it is in some kind of phase.
These phases represent the current context of the game screen and are used to regulate the flow of the game and
contextualize inputs.

The following diagram visualises the states and transitions:

![](../img/client/ComplexCycle.svg)

The states are saved in the `MapInputAdapter` which is where most calls referencing this state are happening.

When creating a lobby in the [](MainMenu.md), the game [navigates](Screens.md#screen-manager) to the Game Screen.
This puts the Game Screen in the [Lobby Phase](GameScreen.md#lobby-phase) by default.

### Lobby Phase

The lobby phase starts when the screen is created and ends when the game starts.
Both players have to be ready at this point.
They can set themselfs ready using the provided dialog, leading to their status being communicated with the other client
via the server.
Leaving the lobby phase puts the screen into the [Game Initialisation Phase](GameScreen.md#game-initialisation).

### Game Initialisation

![](../img/client/GameScreenInit.png)


Entering the Game Initialisation Phase brings the game to life.
This process gets initiated by receiving the `GAME_INIT` packet, which calls the `initializeGame` method.

````Java
getWebSocketService().addPacketHandler("GAME_INIT",
    str -> Gdx.app.postRunnable(() -> {
        getGameStateService().registerNewGame(9, 9);
        var characters = CharacterEntityMapper.fromListDTO(str);
        String gameId = new JsonReader().parse(str)
            .get("data").getString("gameId");
        getGameStateService().setGameId(gameId);
        getGameStateService().setCharacters(characters);
        initializeGame();
}));
````

This method updates the current phase in the [](MapInputAdapter.md), 
handels the configuration and presentation of the start tiles (See green area on the image) of the [](GridModel.md) and
creates the [Character Sprites](CharacterSprite.md) for all [](CharacterEntity.md).
This is also when the [](GameScreen.md#stat-panels) are created.

In this phase, the players are able to position their characters however they want. Their final placement is shown
to the other player only after both players logged in their positions. 

For more information about this, see [here](MapInputAdapter.md#process-click-in-game-init-mode).

When the player logs in his choice using the [](GameScreen.md#ready-button), the screen will be put into the `Waiting
for Turn Outcome` Phase.

### Waiting for Turn Outcome

This phase describes the phase between sending the commands and receiving a response from the server.
As soon as the [TurnResult](Turn-Logic.md) arrives, the screen starts the `Turn Outcome Animation`.

### Turn Outcome Animation

This phase is responsible for displaying the turn outcome animation. See [](TurnOutcomeAnimations.md).
As soon as the animation is over, the screen enter the Command Phase or Game Summary Phase (if a winner or
draw was communicated by the server).

### Command Phase

![](../img/client/CommandModeCommands.png)

This is the phase the player will spend most time in. All commands are planed and chosen here.

### Game Summary

This phase is the last phase of the game loop. Its shows both players the winner of the game in a `Dialog`.

## Map Rendering

The map was created using the tool [Tiled](http://www.mapeditor.org/) and exported as a tmx file.
This file can be loaded using the TmxMapLoader to directly manipulate and render it in game.
The map itself is stored in the base layer of this map, every single tile being saved with all necessary
information for its depiction.

## Other Layers

All information about the status of tiles or the possibility or effect of certain commands is delivered
using visual clues. Most of these are implemented using custom layers in the `TiledMap` using `TiledMapTileLayers`.
By creating these layers, configuring their tiles to display certain textures and adding them to the tiled map object,
they can be rendered to the screen.

The following custom layers are used in the game:

#### Start-Rows Layer

![](../img/client/StartTilesLayer.png)

This layers contains the transparent green tiles seen in the [](GameScreen.md#game-initialisation).
It shows the player the area in which the player can move or swap his characters during the phase.

#### Selected-Character Layer

![](../img/client/SelectedCharacter.png)

This layers contains a single green tile to show which character is currently selected when giving commands.

#### Highlight Layer

![](../img/client/HighlightLayer.png)

This layer contains a  highlight on the tile currently hovered by the player.

#### Command Option/Preview Layer

![](../img/client/MovePreview.png){width=400}

![](../img/client/AttackPreview.png){width=400}

The command option layer depicts all valid target tiles of an attack/movement command.
In the images, it is responsible for the yellow circles (movement options) and red squares (attack options).

The command preview layer visualizes the result of a command. Its responsible for all filled out tiles in yellow
(movement) and red (attack area).

## UI Elements

The Game Screen features many UI elements, each providing valuable functions to the player.

### Ready Button

The ready button lets the players send their commands. Its text gives some hints about the current state of the game,
displaying the amount of commands set in the current round or tells the player to wait for his opponent.
The button is updated using his `act(float delta)` method that's called together with `stage.act()` in the rendering 
method and in situations where it should not be pressed.

### Stat Panels

The stat panels are the two large panels on the top sides of the screen. They display the current status of
all characters currently alive on the board. The hp bars are colored according to their current hp.
Clicking on a character opens a `Dialog` that displays all information about the character.

### Escape menu

A small menu that opens thanks to the [](MapInputAdapter.md) that allows the player to abandon the match and
return to the main menu.