package com.bteam.fantasychess_client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class MapInputProcessor implements InputProcessor {

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
    public MapInputProcessor(
        GameScreen gameScreen, GameScreenMode gameScreenmode, CommandMode commandMode, TileMathService mathService, OrthographicCamera gameCamera
    ) {
        this.gameScreen = gameScreen;
        this.gameScreenMode = gameScreenmode;
        this.commandMode = commandMode;
        this.mathService = mathService;
        this.gameCamera = gameCamera;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Vector3 worldPos3 = gameCamera.unproject(new Vector3(screenX,screenY,0));
        Vector2D gridPos = mathService.worldToGrid(worldPos3.x, worldPos3.y);

        boolean processed = false;

        switch (gameScreenMode){
            case TURN_OUTCOME:
                break;
            case GAME_INIT:
                processed = processInGameInitMode(gridPos,button);
            case LOBBY:
                break;
            case GAME_SUMMARY:
                break;
            case COMMAND_MODE:
                processed = processInCommandMode(gridPos,button);
        }

        return processed;
    }

    private boolean processInGameInitMode(Vector2D gridPos, int button) {
        if (button == Input.Buttons.LEFT){
            GridService gridService = Main.getGameStateService().getGridService();

            switch (commandMode){
                case NO_SELECTION:
                    try {
                        CharacterEntity character = gridService.getCharacterAt(gridPos);
                        gameScreen.setSelectedCharacter(character);
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
                            gameScreen.setSelectedCharacter(null);
                            commandMode = CommandMode.NO_SELECTION;
                            return true;
                        }

                        CharacterEntity otherCharacter = gridService.getCharacterAt(gridPos);
                        if (otherCharacter == null){
                            gridService.moveCharacter(gameScreen.getSelectedChracter().getPosition(),gridPos);
                            gameScreen.getMappedEntity(gameScreen.getSelectedChracter().getId()).moveToGridPos(gridPos);
                        } else {
                            gridService.swapCharacters(gameScreen.getSelectedChracter().getPosition(),otherCharacter.getPosition());
                            gameScreen.getMappedEntity(gameScreen.getSelectedChracter().getId()).moveToGridPos(gridPos);
                            gameScreen.getMappedEntity(otherCharacter.getId()).moveToGridPos(otherCharacter.getPosition());
                        }

                        gameScreen.setSelectedCharacter(null);
                        commandMode = CommandMode.NO_SELECTION;
                    } catch (Exception e){
                        Main.getLogger().log(Level.SEVERE,e.getMessage());
                    }
                    break;
            }
            return true;
        } else if (button == Input.Buttons.RIGHT){
            gameScreen.setSelectedCharacter(null);
            commandMode = CommandMode.NO_SELECTION;
            return true;
        }

        return false;
    }

    private boolean processInCommandMode(Vector2D gridPos, int button) {
        if (button == Input.Buttons.LEFT) {
            switch (commandMode){
                case NO_SELECTION: {
                    try {
                        CharacterEntity character = Main.getGameStateService().getGridService().getCharacterAt(gridPos);
                        gameScreen.setSelectedCharacter(character);
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

            return true;
        } else if (button == Input.Buttons.RIGHT){
            commandMode = CommandMode.NO_SELECTION;
            gameScreen.resetSelection();
            return true;
        }

        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public GameScreenMode getGameScreenMode(){
        return gameScreenMode;
    }

    public CommandMode getCommandMode(){
        return commandMode;
    }

    public TileMathService getMathService() {
        return mathService;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setGameScreenMode(GameScreenMode gameScreenMode) {
        this.gameScreenMode = gameScreenMode;
    }

    public void setCommandMode(CommandMode commandMode) {
        this.commandMode = commandMode;
    }

    public void setMathService(TileMathService mathService) {
        this.mathService = mathService;
    }

    public void setGameCamera(OrthographicCamera gameCamera) {
        this.gameCamera = gameCamera;
    }

}
