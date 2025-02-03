package com.bteam.fantasychess_client.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerInfoDTO;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_client.Main;
import com.bteam.fantasychess_client.ui.Screens;

import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * End Game Dialog for displaying the winner of the game.
 *
 * @author albano
 */
public class EndGameDialog extends Dialog {

    private final Skin skin;

    /**
     * Constructor for the EndGameDialog
     *
     * @param skin       The UI skin used for styling the dialog.
     * @param winnerName Represents the winner of the game. If null or empty, the game finished in a draw.
     */
    public EndGameDialog(Skin skin, String winnerName) {
        super("Game Over", skin);
        this.skin = skin;

        String endMessage = (winnerName.equals("DRAW") || winnerName.isEmpty()) ? "Result: DRAW" : "Winner: " + winnerName;

        Label endMessageLabel = new Label(endMessage, skin);
        endMessageLabel.setAlignment(Align.center);

        getContentTable().add(endMessageLabel).pad(10).row();
        button("Back to Main Menu", true).align(Align.center);

        setSize(400, 200);

        getWebSocketService().removePacketHandler("LOBBY_CLOSED");
    }

    @Override
    protected void result(Object object) {
        if ((boolean) object) {
            var packet = new Packet(new PlayerInfoDTO("", "", Player.Status.NOT_READY), "PLAYER_LEAVE");
            getWebSocketService().send(packet);
            Main.getScreenManager().navigateTo(Screens.MainMenu);
        }
    }
}
