package com.bteam.fantasychess_client.ui;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A lobby screen for waiting after lobby creation.
 * <p />
 * Shows players in lobby and offers leaving and starting game
 *
 * @author jacinto
 * @version 1.0
 */
public class LobbyScreen extends Dialog {

    private enum Status {
        READY {
            public String toString() {
                return "READY";
            }
        },
        NOT_READY {
            public String toString() {
                return "NOT READY";
            }
        }
    }

    private Status statusPlayer1 = Status.NOT_READY;
    private Status statusPlayer2 = Status.NOT_READY;
    String hostName;
    String clientName;

    public LobbyScreen(Skin skin, String lobbyName, String hostName) {
        super(lobbyName, skin);

        this.hostName = hostName;
        this.clientName = "Your Player Name";

        setModal(true);
        setMovable(false);
        setResizable(false);

        text(this.hostName);
        getContentTable().right();
        text(statusPlayer1.toString());

        getContentTable().row();

        text(clientName);
        getContentTable().right();
        text(statusPlayer2.toString());

        button("◀ LEAVE", "leave");
        button("READY", "ready");
        button("START ▶", "start");

    }

}
