package com.bteam.fantasychess_client.ui;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

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

    private Label hostLabel;
    private Label clientLabel;
    private Label hostStatusLabel;
    private Label clientStatusLabel;

    public LobbyScreen(Skin skin, String lobbyName, String hostName) {
        super(lobbyName, skin);
        getTitleLabel().setAlignment(Align.center);
        getTitleTable().align(Align.center);

        setModal(true);
        setMovable(false);
        setResizable(false);

        this.hostLabel = new Label(hostName, skin);
        this.clientLabel = new Label("Client Name", skin);
        this.hostStatusLabel = new Label(Status.NOT_READY.toString(), skin);
        this.clientStatusLabel = new Label(Status.NOT_READY.toString(), skin);

        getContentTable().add(hostLabel)
            .expandX().fillX().padLeft(10).align(Align.right).padRight(10);
        getContentTable().right();
        getContentTable().add(hostStatusLabel).padRight(10);

        getContentTable().row();

        getContentTable().add(clientLabel)
            .expandX().fillX().padLeft(10).align(Align.right).padRight(10);
        getContentTable().right();
        getContentTable().add(clientStatusLabel).padRight(10);

        button("< LEAVE", "leave");
        button("READY", "ready");
        button("START >", "start");

    }

}
