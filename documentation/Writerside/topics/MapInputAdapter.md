# Map Input Adapter

`Author: Lukas Walker`

The map input adapter contains the entire logic for the player interaction with the grid. It puts input events in context
with the current state of the game by tracking the current game phases and possible character/command-type selections
(attack/move). These states can be gotten from/set here. It extends the LibGDX class `InputAdapter`.

The inputs are caught using the `touchUp` event to evade all problems arising from holding down the mouse button.

Another event caught using the `keyUp` event opens the [](GameScreen.md#escape-menu) of the [](GameScreen.md).

Left-clicking is used for selecting characters and setting commands.
If a left click is registered outside the grid, the event will be dismissed.
If the click hits a tile, this information is passed on depending on the current phase of the game
(here: `gameScreenMode`):

````Java
if (gridPos == null){
    return false;
}

switch (gameScreenMode) {
    case TURN_OUTCOME:
        break;
    case GAME_INIT:
        processClickInGameInitMode(gridPos);
        break;
    case COMMAND_MODE:
        processClickInCommandMode(gridPos);
        break;
}
return true;
````

Right clicking resets all selections and can be used to cancel actions.

## Process click in game init mode

This method is called if the click hits a tile in the [](GameScreen.md#game-initialisation).

If no character is selected (`Command Mode` = NO_SELECTION), the Map Input Adapter tries to retrieve the one possible
standing on the clicked tile from the [](GridService.md).
If this succeeds, the `Command Mode` it set to "SWAP MODE". This only works if the clicked character belongs to the
player clicking it. At the same time, the character will become the selected
character and will be marked will a dark green tile (See [](GameScreen.md#selected-character-layer)).

In "SWAP MODE", the click results in an attempts to move the characters to or swap the character with the
character at the clicked position. This only works if the clicked tile is considered to be part of the starting rows
(See [](GameScreen.md#start-rows-layer)).

## Process click in command phase

Clicking on a tile while in command phase will too be handled differently depending on the current `Command Mode`:

1. `NO_SELECTION`: If a character is retrieved from the [](GridService.md) at this location, a menu will open asking the
   player if he want to plan an attack or movement command. Choosing an option will result in the `Command Mode` being set
   to the corresponding mode (`MOVE_MODE` or `ATTACK_MODE`). At the same time, the character will become the selected
   character and will be marked will a dark green tile (See [](GameScreen.md#selected-character-layer)). Choosing a mode
   will also result in all valid target options being marked according to their type
   (See [](GameScreen.md#command-option-preview-layer)).

2. `MOVE_MODE`: If the clicked tile is still empty and a valid destination for a movement command of the selected
   character, a command is set and the selection is reset. The options will disappear.

3. `ATTACK_MODE`: If the clicked tile is a valid destination for a attack command of the selected
   character, a command is set and the selection is reset. The options will disappear and the damage of the
   attack is added to the damage numbers on the grid displaying the damage results of sending the current set of commands.