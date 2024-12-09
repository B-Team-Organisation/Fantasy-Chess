package com.bteam.fantasychess_client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.models.GridService;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.ui.CommandMode;
import com.bteam.fantasychess_client.ui.GameScreen;
import com.bteam.fantasychess_client.ui.GameScreenMode;
import com.bteam.fantasychess_client.utils.TileMathService;

import java.util.logging.Level;

/**
 * Manages all inputs on the chess board
 * <p>
 * Allows the player to click on the characters to execute context dependent actions.
 *
 * @version 1.0
 * @author lukas jacinto
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
     *
     * @return the {@link InputProcessor}
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

        Vector3 worldPos3 = gameCamera.unproject(new Vector3(screenX,screenY,0));
        Vector2D gridPos = mathService.worldToGrid(worldPos3.x, worldPos3.y);

        if (button == Input.Buttons.LEFT) {

            switch (gameScreenMode){
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

        switch (commandMode){
            case NO_SELECTION:
                try {
                    CharacterEntity character = gridService.getCharacterAt(gridPos);
                    gameScreen.updateSelectedCharacter(character);
                    if (character != null){
                        commandMode = CommandMode.SWAP_MODE;
                    }

                } catch (DestinationInvalidException e) {
                    Main.getLogger().log(Level.SEVERE,e.getMessage());
                }
                break;
            case SWAP_MODE:
                try {
                    if (!gridService.getTileAt(gridPos).isStartTile()){
                        return;
                    }

                    CharacterEntity otherCharacter = gridService.getCharacterAt(gridPos);
                    if (otherCharacter == null){
                        gridService.moveCharacter(gameScreen.getSElectedCharacter().getPosition(),gridPos);

                        gameScreen.getMappedSprite(gameScreen.getSElectedCharacter().getId()).moveToGridPos(gridPos);
                    } else {
                        gridService.swapCharacters(gameScreen.getSElectedCharacter().getPosition(),otherCharacter.getPosition());

                        gameScreen.getMappedSprite(gameScreen.getSElectedCharacter().getId()).moveToGridPos(gridPos);
                        gameScreen.getMappedSprite(otherCharacter.getId()).moveToGridPos(otherCharacter.getPosition());
                    }

                    gameScreen.resetSelection();
                    commandMode = CommandMode.NO_SELECTION;
                } catch (Exception e){
                    Main.getLogger().log(Level.SEVERE,e.getMessage());
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
        switch (commandMode){
            case NO_SELECTION: {
                try {
                    CharacterEntity character = Main.getGameStateService().getGridService().getCharacterAt(gridPos);
                    gameScreen.updateSelectedCharacter(character);
                    if (character != null){
                        Gdx.app.postRunnable(() -> gameScreen.openCommandTypeDialog());
                    }
                } catch (DestinationInvalidException e) {
                    Main.getLogger().log(Level.SEVERE,e.getMessage());
                }
                break;
            }
            case MOVE_MODE: {
                // Todo save move command
                break;
            }
            case ATTACK_MODE: {
                break;
                // Todo save attack command
            }
        }
    }


    /**
     * Getter for the {@link GameScreenMode}
     *
     * @return current {@code gameScreenMode}
     */
    public GameScreenMode getGameScreenMode(){
        return gameScreenMode;
    }

    /**
     * Getter for the {@link CommandMode}
     *
     * @return current {@code commandMode}
     */
    public CommandMode getCommandMode(){
        return commandMode;
    }

    /**
     * Setter for the {@code gameScreenMode}
     */
    public void setGameScreenMode(GameScreenMode gameScreenMode) {
        this.gameScreenMode = gameScreenMode;
    }

    /**
     * Setter for the {@code commandMode}
     */
    public void setCommandMode(CommandMode commandMode) {
        this.commandMode = commandMode;
    }

}
