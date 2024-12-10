package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.bteam.fantasychess_client.Main;

public class GameEndScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final ExtendViewport extendViewport;
    private  Stage stage;
    private final Skin skin;

    public GameEndScreen( Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        camera.update();

        extendViewport = new ExtendViewport(1920, 1080, camera);
        extendViewport.apply();

        this.skin = skin;
    }

    /**
     * Displays the end game dialog.
     *
     * @param winnerName Name of the winner, or null for a draw.
     */
    public void showEndGameDialog(String winnerName) {

        String endMessage;

        if ( winnerName.isEmpty() || winnerName == null) {
            endMessage = "Result: DRAW";
        } else {
            endMessage = "Result:" + winnerName;
        }



        Dialog endGameDialog = new Dialog("Game Over", skin) {
            @Override
            protected void result(Object object) {
                if ((boolean) object) {
                    Main.getScreenManager().navigateToNonRunnable(Screens.MainMenu);
                }
            }
        };

        endGameDialog.text(endMessage, skin.get("default", Label.LabelStyle.class));
        endGameDialog.button("Back to Main Menu", true).align(Align.center);

        endGameDialog.setSize(400, 200);
        endGameDialog.setPosition((stage.getWidth() - endGameDialog.getWidth()) / 2, (stage.getHeight() - endGameDialog.getHeight()) / 2);

        endGameDialog.show(stage);
    }
}
