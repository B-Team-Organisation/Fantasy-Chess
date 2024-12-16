package com.bteam.fantasychess_client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.GridService;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.ui.CommandMode;
import com.bteam.fantasychess_client.ui.GameScreen;
import com.bteam.fantasychess_client.ui.GameScreenMode;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.Arrays;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.getLogger;
import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * Manages all inputs on the chess board
 * <p>
 * Allows the player to click on the characters to execute context dependent actions.
 *
 * @author lukas jacinto
 * @version 1.0
 */
public class MapInputAdapter extends InputAdapter {
    GameScreen gameScreen;
    GameScreenMode gameScreenMode;
    CommandMode commandMode;
    TileMathService mathService;
    OrthographicCamera gameCamera;

    /**
     * Creates an {@link InputProcessor} that manages mouse input on the grid
     * <p>
     * Left click selects tiles or sets commands, right click resets status.
     */
    public MapInputAdapter(
        GameScreen gameScreen, GameScreenMode gameScreenmode, CommandMode commandMode, TileMathService mathService, OrthographicCamera gameCamera
    ) {
        this.gameScreen = gameScreen;
        this.gameScreenMode = gameScreenmode;
        this.commandMode = commandMode;
        this.mathService = mathService;
        this.gameCamera = gameCamera;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Main.getLogger().log(Level.SEVERE, "Input on: " + button + " in Commandmode:\"" + commandMode + "\" in GameScreenMode:\"" + gameScreenMode + "\"");

        Vector3 worldPos3 = gameCamera.unproject(new Vector3(screenX, screenY, 0));
        Vector2D gridPos = mathService.worldToGrid(worldPos3.x, worldPos3.y);

        if (button == Input.Buttons.LEFT) {

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

        } else if (button == Input.Buttons.RIGHT) {

            gameScreen.resetSelection();
            commandMode = CommandMode.NO_SELECTION;
            return true;

        }

        return false;
    }

    /**
     * Process the click in the context of the init mode
     * <p>
     * Lets you select, move and swap characters in your initial board setup.
     *
     * @param gridPos the clicked position in the grid
     */
    private void processClickInGameInitMode(Vector2D gridPos) {

        GridService gridService = Main.getGameStateService().getGridService();

        switch (commandMode) {
            case NO_SELECTION:
                try {
                    CharacterEntity character = gridService.getCharacterAt(gridPos);
                    if (!character.getPlayerId().equals(getWebSocketService().getUserid())) {
                        Main.getLogger().log(Level.SEVERE, "Clicked character doesnt belong to player id!");
                        return;
                    }
                    gameScreen.updateSelectedCharacter(character);
                    if (character != null) {
                        commandMode = CommandMode.SWAP_MODE;
                    }

                } catch (DestinationInvalidException e) {
                    Main.getLogger().log(Level.SEVERE, e.getMessage());
                }
                break;
            case SWAP_MODE:
                try {
                    if (!gridService.getTileAt(gridPos).isStartTile()) {
                        return;
                    }

                    CharacterEntity otherCharacter = gridService.getCharacterAt(gridPos);
                    Main.getCommandManagementService().setCommand(new MovementDataModel(gameScreen.getSelectedCharacter().getId(), gridPos));

                    if (otherCharacter == null) {
                        gridService.moveCharacter(gameScreen.getSelectedCharacter().getPosition(), gridPos);

                        gameScreen.getMappedSprite(gameScreen.getSelectedCharacter().getId()).moveToGridPos(gridPos);
                    } else {
                        if (!otherCharacter.getPlayerId().equals(getWebSocketService().getUserid())) {
                            Main.getLogger().log(Level.SEVERE, "Clicked character doesnt belong to player id!");
                            return;
                        }

                        gridService.swapCharacters(gameScreen.getSelectedCharacter().getPosition(), otherCharacter.getPosition());

                        Main.getCommandManagementService().setCommand(new MovementDataModel(otherCharacter.getId(), otherCharacter.getPosition()));

                        gameScreen.getMappedSprite(gameScreen.getSelectedCharacter().getId()).moveToGridPos(gridPos);
                        gameScreen.getMappedSprite(otherCharacter.getId()).moveToGridPos(otherCharacter.getPosition());
                    }

                    gameScreen.resetSelection();
                    commandMode = CommandMode.NO_SELECTION;
                } catch (Exception e) {
                    Main.getLogger().log(Level.SEVERE, e.getMessage());
                }
                break;
        }
    }

    /**
     * Process the click in the context of the command mode
     * <p>
     * Lets you select characters and apply commands for them.
     *
     * @param gridPos the clicked position in the grid
     */
    private void processClickInCommandMode(Vector2D gridPos) {

        CharacterEntity character = null;
        try {
            character = Main.getGameStateService().getGridService().getCharacterAt(gridPos);
        } catch (DestinationInvalidException e) {
            Main.getLogger().log(Level.SEVERE, e.getMessage());
        }

        switch (commandMode) {
            case NO_SELECTION: {
                gameScreen.updateSelectedCharacter(character);
                if (character != null && character.getPlayerId().equals(getWebSocketService().getUserid())) {
                    getLogger().log(Level.SEVERE, "Selected character at: " + character.getPosition());
                    Gdx.app.postRunnable(() -> gameScreen.openCommandTypeDialog());
                }
                break;
            }
            case MOVE_MODE: {
                getLogger().log(Level.SEVERE, "Move pressed at:" + gridPos.toString());
                if (!Arrays.asList(gameScreen.getValidCommandDestinations()).contains(gridPos)) break;
                Main.getCommandManagementService().setCommand(new MovementDataModel(gameScreen.getSelectedCharacter().getId(), gridPos));
                commandMode = CommandMode.NO_SELECTION;
                gameScreen.resetSelection();
                break;
            }

            case ATTACK_MODE: {
                getLogger().log(Level.SEVERE, "Attack pressed at:" + gridPos.toString());
                if (!Arrays.asList(gameScreen.getValidCommandDestinations()).contains(gridPos)) break;
                Main.getCommandManagementService().setCommand(new AttackDataModel(gridPos, gameScreen.getSelectedCharacter().getId()));
                commandMode = CommandMode.NO_SELECTION;
                gameScreen.resetSelection();
                break;
            }
        }
    }


    /**
     * Getter for the {@link GameScreenMode}
     *
     * @return current {@code gameScreenMode}
     */
    public GameScreenMode getGameScreenMode() {
        return gameScreenMode;
    }

    /**
     * Setter for the {@code gameScreenMode}
     */
    public void setGameScreenMode(GameScreenMode gameScreenMode) {
        this.gameScreenMode = gameScreenMode;
    }

    /**
     * Getter for the {@link CommandMode}
     *
     * @return current {@code commandMode}
     */
    public CommandMode getCommandMode() {
        return commandMode;
    }

    /**
     * Setter for the {@code commandMode}
     */
    public void setCommandMode(CommandMode commandMode) {
        this.commandMode = commandMode;
    }

}
